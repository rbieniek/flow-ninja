/**
 * 
 */
package org.flowninja.rspl.definitions.types;

import java.io.Serializable;
import java.net.Inet4Address;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Model a IPv4 CIDR address. A CIDR address consists of a network address and a netmask ranging from 0 to 32.
 * 
 * @author rainer
 *
 */
public class CIDR4Address implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3373843352752229083L;

	private static final Map<Integer, byte[]> MASK_MAP;
	
	static {
		Map<Integer, byte[]> map = new HashMap<Integer, byte[]>();

		map.put(0,  new byte[] { (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00 });
		map.put(1,  new byte[] { (byte)0x80, (byte)0x00, (byte)0x00, (byte)0x00 });
		map.put(2,  new byte[] { (byte)0xc0, (byte)0x00, (byte)0x00, (byte)0x00 });
		map.put(3,  new byte[] { (byte)0xe0, (byte)0x00, (byte)0x00, (byte)0x00 });
		map.put(4,  new byte[] { (byte)0xf0, (byte)0x00, (byte)0x00, (byte)0x00 });
		map.put(5,  new byte[] { (byte)0xf8, (byte)0x00, (byte)0x00, (byte)0x00 });
		map.put(6,  new byte[] { (byte)0xfc, (byte)0x00, (byte)0x00, (byte)0x00 });
		map.put(7,  new byte[] { (byte)0xfe, (byte)0x00, (byte)0x00, (byte)0x00 });
		map.put(8,  new byte[] { (byte)0xff, (byte)0x00, (byte)0x00, (byte)0x00 });

		map.put(9,   new byte[] { (byte)0xff, (byte)0x80, (byte)0x00, (byte)0x00 });
		map.put(10,  new byte[] { (byte)0xff, (byte)0xc0, (byte)0x00, (byte)0x00 });
		map.put(11,  new byte[] { (byte)0xff, (byte)0xe0, (byte)0x00, (byte)0x00 });
		map.put(12,  new byte[] { (byte)0xff, (byte)0xf0, (byte)0x00, (byte)0x00 });
		map.put(13,  new byte[] { (byte)0xff, (byte)0xf8, (byte)0x00, (byte)0x00 });
		map.put(14,  new byte[] { (byte)0xff, (byte)0xfc, (byte)0x00, (byte)0x00 });
		map.put(15,  new byte[] { (byte)0xff, (byte)0xfe, (byte)0x00, (byte)0x00 });
		map.put(16,  new byte[] { (byte)0xff, (byte)0xff, (byte)0x00, (byte)0x00 });

		map.put(17,  new byte[] { (byte)0xff, (byte)0xff, (byte)0x80, (byte)0x00 });
		map.put(18,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xc0, (byte)0x00 });
		map.put(19,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xe0, (byte)0x00 });
		map.put(20,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xf0, (byte)0x00 });
		map.put(21,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xf8, (byte)0x00 });
		map.put(22,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xfc, (byte)0x00 });
		map.put(23,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xfe, (byte)0x00 });
		map.put(24,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x00 });

		map.put(25,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0x80 });
		map.put(26,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xc0 });
		map.put(27,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xe0 });
		map.put(28,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xf0 });
		map.put(29,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xf8 });
		map.put(30,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xfc });
		map.put(31,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xfe });
		map.put(32,  new byte[] { (byte)0xff, (byte)0xff, (byte)0xff, (byte)0xff });

		MASK_MAP = Collections.unmodifiableMap(map);
	}
	
	private byte[] address;
	private int netmask;
	
	/**
	 * 
	 */
	public CIDR4Address() {
	}

	/**
	 * Constructs a CIDR address from a given network address and a mask.
	 * 
	 * The constructors normalizes the network address by masking trailing address bits. 
	 * 
	 * @param address
	 * @param mask
	 */
	public CIDR4Address(Inet4Address address, int mask)  {
		if(address == null || address.getAddress() == null || address.getAddress().length != 4)
			throw new IllegalArgumentException("Invalid network address passed");

		if(!MASK_MAP.containsKey(mask))
			throw new IllegalArgumentException("Invalid network mask passed");

		this.address = maskAddress(address.getAddress(), mask);
		this.netmask = mask;
	}

	/**
	 * Constructs a CIDR address from a given network address and a mask.
	 * 
	 * The constructors normalizes the network address by masking trailing address bits. 
	 * 
	 * @param address
	 * @param mask
	 */
	public CIDR4Address(byte[] address, int mask)  {
		if(address == null || address.length != 4)
			throw new IllegalArgumentException("Invalid network address passed");
		
		if(!MASK_MAP.containsKey(mask))
			throw new IllegalArgumentException("Invalid network mask passed");

		this.address = maskAddress(address, mask);
		this.netmask = mask;
	}


	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the address
	 */
	public byte[] getAddress() {
		return address;
	}

	/**
	 * @return the netmask
	 */
	public int getNetmask() {
		return netmask;
	}

	/**
	 * Check if an address lies witthin this CIDR range
	 * 
	 * @param address
	 * @return
	 */
	public boolean isInRange(byte[] address) {
		boolean result = true;
		
		if(address == null || address.length != this.address.length)
			throw new IllegalArgumentException("Invalid network address passed");

		byte[] maskBits = MASK_MAP.get(this.netmask);
		
		for(int i=0; i<this.address.length; i++)
			result &= ((address[i] & maskBits[i]) == this.address[i]);
				
		return result;
	}

	/**
	 * Check if an address lies witthin this CIDR range
	 * 
	 * @param address
	 * @return
	 */
	public boolean isInRange(Inet4Address address) {
		if(address == null)
			throw new IllegalArgumentException("Invalid network address passed");
		
		return isInRange(address.getAddress());
	}
	
	@Override
	public String toString() {
		return String.format("%d.%d.%d.%d/%d", address[0] & 0xff, address[1] & 0xff, address[2] & 0xff, address[3] & 0xff, netmask);
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(address).append(netmask).toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof CIDR4Address))
			return false;
		
		CIDR4Address o = (CIDR4Address)obj;
		
		return (new EqualsBuilder()).append(this.address, o.address).append(this.netmask, o.netmask).isEquals();
	}
	
	private byte[] maskAddress(byte[] srcAddress, int mask) {
		byte[] dstAddress = new byte[srcAddress.length];
		byte[] maskBits = MASK_MAP.get(mask);		

		for(int i=0; i<srcAddress.length; i++)
			dstAddress[i] = (byte)(srcAddress[i] & maskBits[i]);

		return dstAddress;
	}
	
}
