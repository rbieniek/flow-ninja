/**
 * 
 */
package org.flowninja.netflow9.storer.flat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.apache.commons.codec.binary.Hex;
import org.flowninja.collector.common.netflow9.services.FlowStoreService;
import org.flowninja.collector.common.netflow9.types.DataFlow;
import org.flowninja.collector.common.netflow9.types.DataFlowRecord;
import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.Header;
import org.flowninja.collector.common.netflow9.types.OptionsFlow;
import org.flowninja.collector.common.netflow9.types.OptionsFlowRecord;
import org.flowninja.collector.common.netflow9.types.ScopeFlowRecord;
import org.flowninja.collector.common.protocol.types.ICMPTypeCode;
import org.flowninja.collector.common.types.Counter;
import org.flowninja.collector.common.types.EncodedData;
import org.flowninja.collector.common.types.EnumCodeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

/**
 * @author rainer
 *
 */
public class FlatFileStorerService implements FlowStoreService {
	private static final Logger logger = LoggerFactory.getLogger(FlowStoreService.class);

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
	private FileSystemHandler fileHandler;
	private JsonFactory factory = new JsonFactory();
	
	/* (non-Javadoc)
	 * @see org.flowninja.collector.common.netflow9.services.FlowStoreService#activateFlowStorer()
	 */
	@Override
	public void activateFlowStorer() {
	}

	/* (non-Javadoc)
	 * @see org.flowninja.collector.common.netflow9.services.FlowStoreService#passivateFlowStorer()
	 */
	@Override
	public void passivateFlowStorer() {
	}

	/* (non-Javadoc)
	 * @see org.flowninja.collector.common.netflow9.services.FlowStoreService#storeDataFlow(java.net.InetAddress, org.flowninja.collector.common.netflow9.types.DataFlow)
	 */
	@Override
	public void storeDataFlow(DataFlow msg) {
		logger.info("storing data flow with uuid {} for peer address {}", msg.getUuid(), msg.getPeerAddress());
		
		try {
			PrintWriter pw = fileHandler.getDataFlowWriter();
			JsonGenerator jg = factory.createGenerator(pw);

			jg.writeStartObject();

			jg.writeStringField("peer", msg.getPeerAddress().getHostAddress());
			jg.writeStringField("uuid", msg.getUuid().toString());
			
			jg.writeFieldName("header");
			writeHeader(jg, msg.getHeader());

			jg.writeFieldName("flow");
			jg.writeStartObject();
			
			for(DataFlowRecord record : msg.getRecords()) {
				writeFlowElement(jg, record.getType(), record.getValue());
			}
			
			jg.writeEndObject();
			
			jg.writeEndObject();
			jg.close();
			
			fileHandler.getDataFlowWriter().println();
		} catch(Exception e) {
			logger.warn("failed to write data flow record for peer {}", msg.getPeerAddress(), e);
		}
	}

	@SuppressWarnings("rawtypes")
	private void writeFlowElement(JsonGenerator jg, FieldType type, Object o) throws IOException {
		jg.writeFieldName(type.toString());
		
		if(o instanceof Counter) {
			jg.writeNumber(((Counter)o).printableValue());
		} else if(o instanceof Number) {
			jg.writeNumber(((Number)o).toString());
		} else if(o instanceof String) {
			jg.writeString((String)o);
		} else if(o instanceof EnumCodeValue) {
			jg.writeStartObject();
			jg.writeStringField("value", ((EnumCodeValue)o).getValue().toString());
			jg.writeNumberField("code", ((EnumCodeValue)o).getCode());			
			jg.writeEndObject();
		} else if(o instanceof InetAddress) {
			jg.writeString(((InetAddress)o).getHostAddress());
		} else if(o instanceof Set) {
			jg.writeStartArray();
			for(Object v : (Set<?>)o)
				jg.writeString(v.toString());
			jg.writeEndArray();
		} else if(o instanceof ICMPTypeCode) {
			jg.writeStartObject();
			jg.writeStringField("type", ((ICMPTypeCode)o).getType().toString());
			jg.writeStringField("code", ((ICMPTypeCode)o).getCode().toString());			
			jg.writeEndObject();			
		} else if(o instanceof byte[]) {
			jg.writeString(new String(Hex.encodeHex((byte[])o)));
		} else if(o instanceof EncodedData) {
			jg.writeString(((EncodedData)o).getEncodedData());
		}
	}

	/* (non-Javadoc)
	 * @see org.flowninja.collector.common.netflow9.services.FlowStoreService#storeOptionsFlow(java.net.InetAddress, org.flowninja.collector.common.netflow9.types.OptionsFlow)
	 */
	@Override
	public void storeOptionsFlow(OptionsFlow msg) {
		logger.info("storing options flow with UUID {} for peer address {}", msg.getUuid(), msg.getPeerAddress());

		try {
			PrintWriter pw = fileHandler.getOptionsFlowWriter();
			JsonGenerator jg = factory.createGenerator(pw);

			jg.writeStartObject();

			jg.writeStringField("peer", msg.getPeerAddress().getHostAddress());
			jg.writeStringField("uuid", msg.getUuid().toString());

			jg.writeFieldName("header");
			writeHeader(jg, msg.getHeader());
			
			jg.writeFieldName("scope");
			jg.writeStartObject();
			for(ScopeFlowRecord record : msg.getScopes()) {
				if(record.getValue() != null)
					jg.writeStringField(record.getType().toString(), record.getValue().printableValue());
				else
					jg.writeNullField(record.getType().toString());
			}
			jg.writeEndObject();
			
			
			jg.writeFieldName("flow");
			jg.writeStartObject();
			for(OptionsFlowRecord record : msg.getRecords()) {
				writeFlowElement(jg, record.getType(), record.getValue());
			}
			jg.writeEndObject();

			jg.writeEndObject();
			jg.close();
			
			fileHandler.getOptionsFlowWriter().println();
		} catch(Exception e) {
			logger.warn("failed to write options flow record for peer {}", msg.getPeerAddress(), e);
		}
	}

	/**
	 * @param fileHandler the fileHandler to set
	 */
	public void setFileHandler(FileSystemHandler fileHandler) {
		this.fileHandler = fileHandler;
	}

	private void writeHeader(JsonGenerator jg, Header header) throws IOException {
		jg.writeStartObject();
		
		jg.writeNumberField("recordCount", header.getRecordCount());
		jg.writeNumberField("sysUpTime", header.getSysUpTime());
		jg.writeNumberField("sequenceNumber", header.getSequenceNumber());
		jg.writeNumberField("sourceId", header.getSourceId());
		
		jg.writeFieldName("timestamp");
		jg.writeStartObject();
		jg.writeNumberField("value", header.getUnixSeconds());
		jg.writeStringField("date", sdf.format(header.getTimestamp()));
		jg.writeEndObject();
		
		jg.writeEndObject();
	}
	
}
