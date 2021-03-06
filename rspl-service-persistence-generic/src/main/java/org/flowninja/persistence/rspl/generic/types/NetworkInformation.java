/**
 * 
 */
package org.flowninja.persistence.rspl.generic.types;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowninja.rspl.definitions.types.ENetworkRegistry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rainer
 *
 */
public class NetworkInformation {

	@JsonProperty(required=true, value="name")
	private String name;
	
	@JsonProperty(required=false, value="country")
	private String country;
	
	@JsonProperty(required=true, value="rir") @JsonFormat(shape=Shape.STRING)
	private ENetworkRegistry regionalInternetRegistry;

	public NetworkInformation() {
		
	}
	
	@JsonCreator
	public NetworkInformation(@JsonProperty("name") String name, @JsonProperty("country") String country, 
			@JsonProperty("rir") @JsonFormat(shape=Shape.STRING) ENetworkRegistry regionalInternetRegistry) {
		this.name = name;
		this.country = country;
		this.regionalInternetRegistry = regionalInternetRegistry;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the regionalInternetRegistry
	 */
	public ENetworkRegistry getRegionalInternetRegistry() {
		return regionalInternetRegistry;
	}

	/**
	 * @param regionalInternetRegistry the regionalInternetRegistry to set
	 */
	public void setRegionalInternetRegistry(
			ENetworkRegistry regionalInternetRegistry) {
		this.regionalInternetRegistry = regionalInternetRegistry;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof NetworkInformation))
			return false;
		
		NetworkInformation o = (NetworkInformation)obj;
		
		return (new EqualsBuilder())
				.append(this.country, o.country)
				.append(this.name, o.name)
				.append(this.regionalInternetRegistry, o.regionalInternetRegistry)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(this.country)
				.append(this.name)
				.append(this.regionalInternetRegistry)
				.toHashCode();
	}
}
