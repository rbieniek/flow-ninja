/**
 * 
 */
package org.flowninja.rspl.client.whois.common;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * @author rainer
 *
 */
public class InetnumRecord implements IWhoisRecord {
	private String inetnum;
	private String netname;
	private List<String> descr = new LinkedList<String>();
	private String adminC;
	private String techC;
	private String source;
	private String country;
	private String org;
	private String owner;
	
	public InetnumRecord() {}
	
	public InetnumRecord(String inetnum, String netname, String[] descr, String adminC, String techC, String source, String country, String org, String owner) {
		this.inetnum = inetnum;
		this.netname = netname;
		this.adminC = adminC;
		this.techC = techC;
		this.source = source;
		this.country = country;
		this.org = org;
		this.owner = owner;
		
		if(descr != null)
			this.descr = Arrays.asList(descr);
	}
	
	/**
	 * @return the inetnum
	 */
	public String getInetnum() {
		return inetnum;
	}
	/**
	 * @param inetnum the inetnum to set
	 */
	public void setInetnum(String inetnum) {
		this.inetnum = inetnum;
	}
	/**
	 * @return the netname
	 */
	public String getNetname() {
		return netname;
	}
	/**
	 * @param netname the netname to set
	 */
	public void setNetname(String netname) {
		this.netname = netname;
	}
	/**
	 * @return the descr
	 */
	public List<String> getDescr() {
		return descr;
	}
	/**
	 * @param descr the descr to set
	 */
	public void setDescr(List<String> descr) {
		this.descr = descr;
	}
	/**
	 * @return the adminC
	 */
	public String getAdminC() {
		return adminC;
	}
	/**
	 * @param adminC the adminC to set
	 */
	public void setAdminC(String adminC) {
		this.adminC = adminC;
	}
	/**
	 * @return the techC
	 */
	public String getTechC() {
		return techC;
	}
	/**
	 * @param techC the techC to set
	 */
	public void setTechC(String techC) {
		this.techC = techC;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
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
	 * @return the org
	 */
	public String getOrg() {
		return org;
	}
	/**
	 * @param org the org to set
	 */
	public void setOrg(String org) {
		this.org = org;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof InetnumRecord))
			return false;
		
		InetnumRecord o = (InetnumRecord)obj;
		
		return (new EqualsBuilder())
				.append(this.adminC, o.adminC)
				.append(this.country, o.country)
				.append(this.descr, o.descr)
				.append(this.inetnum, o.inetnum)
				.append(this.netname, o.netname)
				.append(this.org, o.org)
				.append(this.source, o.source)
				.append(this.techC, o.techC)
				.append(this.owner, o.owner)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(this.adminC)
				.append(this.country)
				.append(this.descr)
				.append(this.inetnum)
				.append(this.netname)
				.append(this.org)
				.append(this.source)
				.append(this.techC)
				.append(this.owner)
				.toHashCode();		
	}

	/**
	 * @return the owner
	 */
	public String getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(String owner) {
		this.owner = owner;
	}
}
