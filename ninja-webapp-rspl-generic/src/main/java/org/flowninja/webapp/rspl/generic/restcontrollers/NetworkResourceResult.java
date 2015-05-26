/**
 * 
 */
package org.flowninja.webapp.rspl.generic.restcontrollers;

import org.flowninja.rspl.definitions.types.ENetworkRegistry;
import org.flowninja.rspl.definitions.types.NetworkResource;
import org.flowninja.types.net.CIDR4Address;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

/**
 * @author rainer
 *
 */
public class NetworkResourceResult {

	private EResultSource source;

	private NetworkResource resource;
	
	public NetworkResourceResult() {}
	
	public NetworkResourceResult(NetworkResource resource, EResultSource source) {
		this.resource = resource;
		this.source = source;
	}

	/**
	 * @return the source
	 */
	@JsonProperty("source") @JsonFormat(shape=Shape.STRING)
	public EResultSource getSource() {
		return source;
	}

	/**
	 * @return
	 * @see org.flowninja.rspl.definitions.types.NetworkResource#getNetworkAddress()
	 */
	@JsonProperty("cidr")
	public CIDR4Address getNetworkAddress() {
		return resource.getNetworkAddress();
	}

	/**
	 * @return
	 * @see org.flowninja.rspl.definitions.types.NetworkResource#getName()
	 */
	@JsonProperty("name")
	public String getName() {
		return resource.getName();
	}

	/**
	 * @return
	 * @see org.flowninja.rspl.definitions.types.NetworkResource#getCountry()
	 */
	@JsonProperty("country")
	public String getCountry() {
		return resource.getCountry();
	}

	/**
	 * @return
	 * @see org.flowninja.rspl.definitions.types.NetworkResource#getRegionalInternetRegistry()
	 */
	@JsonProperty("registry") @JsonFormat(shape=Shape.STRING)
	public ENetworkRegistry getRegionalInternetRegistry() {
		return resource.getRegionalInternetRegistry();
	}
	
}
