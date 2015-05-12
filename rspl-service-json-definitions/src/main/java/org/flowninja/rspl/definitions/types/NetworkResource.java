/**
 * 
 */
package org.flowninja.rspl.definitions.types;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author rainer
 *
 */
public class NetworkResource implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1827511882719877411L;

	private CIDR4Address networkAddress;
	private String name;
	private String country;
	private ENetworkRegistry regionalInternetRegistry;
	
	/**
	 * 
	 */
	public NetworkResource() {
	}

	public NetworkResource(CIDR4Address networkAddress, String name, String country, ENetworkRegistry regionalInternetRegistry) {
		this.name = name;
		this.networkAddress = networkAddress;
		this.country = country;
		this.regionalInternetRegistry = regionalInternetRegistry;
	}

	/**
	 * @return the networkAddress
	 */
	public CIDR4Address getNetworkAddress() {
		return networkAddress;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @return the regionalInternetRegistry
	 */
	public ENetworkRegistry getRegionalInternetRegistry() {
		return regionalInternetRegistry;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder()).append(networkAddress).append(name).append(country).append(regionalInternetRegistry).toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof NetworkResource))
			return false;
		
		NetworkResource o = (NetworkResource)obj;
		
		return (new EqualsBuilder())
				.append(this.name, o.name)
				.append(this.networkAddress, o.networkAddress)
				.append(this.country, o.country)
				.append(this.regionalInternetRegistry, o.regionalInternetRegistry)
				.isEquals();
	}
}
