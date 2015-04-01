/**
 * 
 */
package org.flowninja.collector.common.types;

/**
 * @author rainer
 *
 */
public class EncodedData {
	private byte[] data;
	private String encodedData;
	
	public EncodedData(byte[] data, String encodedData) {
		this.data = data;
		this.encodedData = encodedData;
	}

	/**
	 * @return the data
	 */
	public byte[] getData() {
		return data;
	}

	/**
	 * @return the encodedData
	 */
	public String getEncodedData() {
		return encodedData;
	}
}
