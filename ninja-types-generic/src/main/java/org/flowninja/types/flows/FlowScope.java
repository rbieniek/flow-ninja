/**
 * 
 */
package org.flowninja.types.flows;

import java.io.Serializable;
import java.math.BigInteger;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rainer
 *
 */
public class FlowScope implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4696615406378141694L;

	public static class Builder {
		private FlowScope scope = new FlowScope();
		
		private Builder() {}
		
		public static Builder newBuilder() {
			return new Builder();
		}
		
		public FlowScope build() {
			return scope;
		}
		
		public Builder withType(ScopeType type) {
			scope.setType(type);
			
			return this;
		}
		
		public Builder withCounter(BigInteger counter) {
			scope.setCounter(counter);
			
			return this;
		}
	}
	
	@JsonProperty(required=true, value="type")
	@JsonFormat(shape=Shape.STRING)
	private ScopeType type;
	
	@JsonProperty(required=false, value="type")
	private BigInteger counter;

	/**
	 * @return the type
	 */
	public ScopeType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(ScopeType type) {
		this.type = type;
	}

	/**
	 * @return the counter
	 */
	public BigInteger getCounter() {
		return counter;
	}

	/**
	 * @param counter the counter to set
	 */
	public void setCounter(BigInteger counter) {
		this.counter = counter;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FlowScope))
			return false;
		
		FlowScope o = (FlowScope)obj;
		
		return (new EqualsBuilder())
				.append(this.type, o.type)
				.append(this.counter, o.counter)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(this.type)
				.append(this.counter)
				.toHashCode();
	}
}
