/**
 * 
 */
package org.flowninja.collector.common.netflow9.types;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author rainer
 *
 */
public class DataFlow {

	private Header header;
	private List<DataFlowRecord> records = new LinkedList<DataFlowRecord>();
	
	public DataFlow(Header header, List<DataFlowRecord> records) {
		this.header = header;
		
		if(records != null) 
			this.records.addAll(records);
	}

	/**
	 * @return the header
	 */
	public Header getHeader() {
		return header;
	}

	/**
	 * @return the records
	 */
	public List<DataFlowRecord> getRecords() {
		return Collections.unmodifiableList(records);
	}
}