/**
 * 
 */
package org.flowninja.rspl.definitions.types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * @author rainer
 *
 */
public class ResultDocument {

	private NetworkResource networkResource;
	private ENetworkRegistry forwardedToRegistry;
	
	/**
	 * 
	 */
	public ResultDocument(NetworkResource networkResource) {
		this.networkResource = networkResource;
	}

	public ResultDocument(ENetworkRegistry forwardedToRegistry) {
		this.forwardedToRegistry = forwardedToRegistry;
	}

	/**
	 * @return the networkResource
	 */
	public NetworkResource getNetworkResource() {
		return networkResource;
	}

	/**
	 * @return the forwardedToRegistry
	 */
	public ENetworkRegistry getForwardedToRegistry() {
		return forwardedToRegistry;
	}
	
	public boolean isForwarded() {
		return forwardedToRegistry != null;
	}
	
	public boolean isResolved() {
		return networkResource != null;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(networkResource)
				.append(forwardedToRegistry)
				.toHashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ResultDocument))
			return false;
		
		ResultDocument o = (ResultDocument)obj;
		
		return (new EqualsBuilder())
				.append(this.networkResource, o.networkResource)
				.append(this.forwardedToRegistry, o.forwardedToRegistry)
				.isEquals();
	}
}
