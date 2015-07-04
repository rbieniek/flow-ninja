/**
 * 
 */
package org.flowninja.shell.transferrer.integration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonValue;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.types.flows.FlowHeader;
import org.flowninja.types.flows.FlowScope;
import org.flowninja.types.flows.OptionsData;
import org.flowninja.types.flows.OptionsFlow;
import org.flowninja.types.flows.ScopeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;

/**
 * @author rainer
 *
 */
@Component
public class SourceFileOptionsFlowParser implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(SourceFileOptionsFlowParser.class);

	private ObjectMapper mapper;

	private static class FlowDataMapper {
		private OptionsData data = new OptionsData();
		
		private BigInteger asBigInteger(JsonValue value) {
			return ((JsonNumber)value).bigIntegerValue();
		}

		public void map(String name, JsonValue value) {
			switch(name) {
			case "TOTAL_FLOWS_EXP":
				data.setTotalFlowsExported(asBigInteger(value));
				break;
			case "TOTAL_PKTS_EXP":
				data.setTotalPacketsExported(asBigInteger(value));
				break;
			}
		}

		/**
		 * @return the data
		 */
		public OptionsData getData() {
			return data;
		}

	}
	
	
	@Transformer(inputChannel="sourceOptionsFileChannel", outputChannel="sourceOptionsFlowChannel")
	public List<OptionsFlow> parseDataFlows(Message<File> flowFile) throws IOException {
		List<OptionsFlow> flows = new LinkedList<OptionsFlow>();
		
		LineNumberReader lnr = null;
		String currentLine;
		
		try {
			lnr = new LineNumberReader(new FileReader(flowFile.getPayload()));
			
			while(StringUtils.isNotEmpty(currentLine = StringUtils.strip(lnr.readLine()))) {
				JsonObject root = mapper.readValue(new StringReader(currentLine), JsonObject.class);

				flows.add(parseFlowRoot(root));
			}
		} catch(IOException e) {
			logger.error("failed to parse data flows file {}", flowFile.getPayload().getAbsolutePath(), e);
			
			throw e;
		} catch(RuntimeException e) {
			logger.error("failed to parse data flows file {}", flowFile.getPayload().getAbsolutePath(), e);
			
			throw e;
		} finally {
			if(lnr != null) {
				try {
					lnr.close();
				} catch(IOException e) {
					
				}
			}
		}

		return flows;
	}

	private OptionsFlow parseFlowRoot(JsonObject root) {
		OptionsFlow flow = new OptionsFlow();
		
		flow.setPeerIp(root.getString("peer"));
		flow.setFlowUuid(root.getString("uuid"));
		
		flow.setHeader(parseFlowHeader(root.getJsonObject("header")));

		if(root.containsKey("scope")) {
			JsonObject json = root.getJsonObject("scope");

			flow.setScopes(new HashSet<FlowScope>());
			
			for(ScopeType scopeType : ScopeType.values()) {
				if(json.containsKey(scopeType.toString())) {
					FlowScope.Builder builder = FlowScope.Builder.newBuilder();
					
					builder.withType(scopeType);
					
					if(!json.isNull(scopeType.toString()))
						builder.withCounter(new BigInteger(json.getString(scopeType.toString())));
					
					flow.getScopes().add(builder.build());
				}
			}
		}
		
		if(root.containsKey("flow")) {
			FlowDataMapper flowMapper = new FlowDataMapper();
			
			root.getJsonObject("flow").forEach((n,v) -> flowMapper.map(n,v));
			
			flow.setData(flowMapper.getData());
		}
		
		return flow;
	}
	
	private FlowHeader parseFlowHeader(JsonObject header)  {
		if(header == null)
			throw new IllegalArgumentException("null header passed");
		
		JsonObject timestamp = header.getJsonObject("timestamp");
		
		if(timestamp == null)
			throw new IllegalArgumentException("null timestamp passed");
		
		return new FlowHeader(
				header.getInt("recordCount"),
				header.getInt("sysUpTime"),
				timestamp.getInt("value"),
				header.getInt("sequenceNumber"),
				header.getInt("sourceId"));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		mapper = new ObjectMapper();
		mapper.registerModule(new JSR353Module());
	}
}
