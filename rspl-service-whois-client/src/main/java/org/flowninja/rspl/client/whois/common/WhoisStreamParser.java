/**
 * 
 */
package org.flowninja.rspl.client.whois.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author rainer
 *
 */
@Component
public class WhoisStreamParser {
	private static final Logger logger = LoggerFactory.getLogger(WhoisStreamParser.class);
	
	private static class Line {
		String key;
		String value;
		
		Line(String key, String value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
	}
	
	private Pattern blockPattern;
	
	public WhoisStreamParser() {
		blockPattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_-]*[:].*$");
	}
	
	public List<IWhoisRecord> parseWhoisResponse(InputStream stream) {
		List<IWhoisRecord> records = new LinkedList<IWhoisRecord>();
		LineNumberReader reader = null;
		
		try {
			String line = null;
			List<Line> block = null;
			
			reader = new LineNumberReader(new InputStreamReader(stream, Charset.forName("ASCII")));

			while((line = reader.readLine()) != null) {
				line = StringUtils.trimToEmpty(line);
				
				if(StringUtils.startsWith(line, "%"))
					continue;

				if(StringUtils.isEmpty(line)) {
					if(block != null) {
						IWhoisRecord record = parseBlock(block);
						
						if(record != null)
							records.add(record);
						
						block = null;
					}
				} else if(blockPattern.matcher(line).matches()) {
					if(block == null)
						block = new LinkedList<Line>();
					
					block.add(breakupLine(line));
				} else {
					logger.warn("Encountered unparseable line: '{}'", line);
				}
			}
		} catch(IOException e) {
			logger.error("exception while processing WHOIS service response stream", e);
		} finally {
			if(reader != null) {
				try {
					reader.close();
				} catch(IOException e) {
					logger.warn("exception while closing WHOIS service response stream", e);					
				}
			}
		}
				
		return records;
	}
	
	private Line breakupLine(String line) {
		int idx = StringUtils.indexOf(line, ":");
		
		return new Line(StringUtils.substring(line, 0, idx), StringUtils.trimToEmpty(StringUtils.substring(line, idx+1)));
	}
	
	private IWhoisRecord parseBlock(List<Line> block) {
		IWhoisRecord record = null;
		
		if(StringUtils.equals(block.get(0).getKey(), "inetnum"))
			record = parseInetnumBlock(block);
		
		return record;
	}
	
	private InetnumRecord parseInetnumBlock(List<Line> block) {
		InetnumRecord record = new InetnumRecord();
		
		for(Line line : block) {
			if(StringUtils.equals(line.getKey(), "inetnum"))
				record.setInetnum(line.getValue());
			else if(StringUtils.equals(line.getKey(), "netname"))
				record.setNetname(line.getValue());
			else if(StringUtils.equals(line.getKey(), "descr"))
				record.getDescr().add(line.getValue());
			else if(StringUtils.equals(line.getKey(), "country"))
				record.setCountry(line.getValue());
			else if(StringUtils.equals(line.getKey(), "admin-c"))
				record.setAdminC(line.getValue());
			else if(StringUtils.equals(line.getKey(), "tech-c"))
				record.setTechC(line.getValue());
			else if(StringUtils.equals(line.getKey(), "org"))
				record.setOrg(line.getValue());
			else if(StringUtils.equals(line.getKey(), "source"))
				record.setSource(line.getValue());
			else if(StringUtils.equals(line.getKey(), "owner"))
				record.setOwner(line.getValue());
		}
		
		return record;
	}
}
