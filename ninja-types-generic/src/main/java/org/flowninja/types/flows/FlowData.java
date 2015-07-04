/**
 * 
 */
package org.flowninja.types.flows;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.flowninja.types.net.ICMPCode;
import org.flowninja.types.net.ICMPType;
import org.flowninja.types.net.IGMPType;
import org.flowninja.types.net.IPProtocol;
import org.flowninja.types.net.IPProtocolVersion;
import org.flowninja.types.net.IPTypeOfService;
import org.flowninja.types.net.MPLSTopLabelType;
import org.flowninja.types.net.TCPFLags;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author rainer
 *
 */
public class FlowData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1477405973933128454L;
	
	public static class Builder {
		private FlowData data = new FlowData();
		private SortedSet<TCPFLags> tcpFlags = null;
		
		private Builder() {}
		
		public static Builder newBuilder() {
			return new Builder();
		}
		
		public FlowData build() {
			if(tcpFlags != null) {
				ArrayList<TCPFLags> flags = new ArrayList<TCPFLags>();
				
				flags.addAll(tcpFlags);
				
				data.setTcpFlags(flags.toArray(new TCPFLags[0]));
			}
			
			return data;
		}
		
		public Builder withDestinationAs(int destinationAs) {
			data.destinationAs = destinationAs;
			
			return this;
		}
		
		public Builder withDestinationMac(String destinationMac) {
			data.destinationMac = destinationMac;
			
			return this;
		}
		
		public Builder withDestinationTypeOfService(IPTypeOfService tos) {
			data.destinationTypeOfService = tos;
			
			return this;
		}
		
		public Builder withDestinationVlan(int vlan) {
			data.destinationVlan = vlan;
			
			return this;
		}
		
		public Builder withFirstSwitched(int firstSwitched) {
			data.firstSwitched = firstSwitched;
			
			return this;
		}
		
		public Builder withFlowClass(int flowClasss) {
			data.flowClass = flowClasss;
			
			return this;
		}
		
		public Builder withFlowDirection(FlowDirection direction) {
			data.flowDirection = direction;
			
			return this;
		}
		
		public Builder withFlows(BigInteger flows) {
			data.flows = flows;
			
			return this;
		}
		
		public Builder withFlowSamplerID(int flowSamplerId) {
			data.flowSamplerID = flowSamplerId;
			
			return this;
		}
		
		public Builder withIcmpTypeCode(ICMPType type, ICMPCode code) {
			data.icmpCode = code;
			data.icmpType = type;
			
			return this;
		}
		
		public Builder withIgmpType(IGMPType type) {
			data.igmpType = type;
			
			return this;
		}
		
		public Builder withInputBytes(BigInteger inpputBytes) {
			data.inputBytes = inpputBytes;
			
			return this;
		}
		
		public Builder withInputPackets(BigInteger inputPackets) {
			data.inputPkts = inputPackets;
			
			return this;
		}
		
		public Builder withInputSnmpIndex(int inputSnmpIndex) {
			data.inputSnmpIndex = inputSnmpIndex;
			
			return this;
		}
		
		public Builder withIpProtocolVersion(IPProtocolVersion version) {
			data.ipProtocolVersion = version;
			
			return this;
		}
		
		public Builder withIpV4BgpNextHopAddress(String addr) {
			data.ipv4BgpNextHopAddress = addr;
			
			return this;
		}
		
		public Builder withIpV4DestinationAddress(String addr) {
			data.ipv4DestinationAddress = addr;
			
			return this;
		}
		
		public Builder withIpV4DestinationMask(int mask) {
			data.ipv4DestinationMask = mask;
			
			return this;
		}
		
		public Builder withIpV4Identity(int identity) {
			data.ipv4Identity = identity;
			
			return this;
		}
		
		public Builder withIpV4NextHopAddress(String addr) {
			data.ipv4NextHopAddress = addr;
			
			return this;
		}
		
		public Builder withIpV4SourceAddress(String addr) {
			data.ipv4SourceAddress = addr;
			
			return this;
		}
		
		public Builder withIpV4SourceMask(int mask) {
			data.ipv4SourceMask = mask;
			
			return this;
		}

		public Builder withIpV6BgpNextHopAddress(String addr) {
			data.ipv6BgpNextHopAddress = addr;
			
			return this;
		}
		
		public Builder withIpV6DestinationAddress(String addr) {
			data.ipv6DestinationAddress = addr;
			
			return this;
		}
		
		public Builder withIpV6DestinationMask(int mask) {
			data.ipv6DestinationMask = mask;
			
			return this;
		}
		
		public Builder withIpV6FlowLabel(int flowLabel) {
			data.ipv6FlowLabel = flowLabel;
			
			return this;
		}
		
		public Builder withIpV6NextHopAddress(String addr) {
			data.ipv6NextHopAddress = addr;
			
			return this;
		}
		
		public Builder withIpV6SourceAddress(String addr) {
			data.ipv6SourceAddress = addr;
			
			return this;
		}
		
		public Builder withIpV6SourceMask(int mask) {
			data.ipv6SourceMask = mask;
			
			return this;
		}
		
		public Builder withLastSwitched(int date) {
			data.lastSwitched = date;
			
			return this;
		}
		
		public Builder withLayer4DestinationPort(int port) {
			data.layer4DestinationPort = port;
			
			return this;
		}

		public Builder withLayer4SourcePort(int port) {
			data.layer4SourcePort = port;
			
			return this;
		}
		
		public Builder withMaximumPacketLength(int length) {
			data.maximumPacketLength = length;
			
			return this;
		}
		
		public Builder withMaximumTimeToLive(int ttl) {
			data.maximumTimeToLive = ttl;
			
			return this;
		}

		public Builder withMinimumPacketLength(int length) {
			data.minimumPacketLength = length;
			
			return this;
		}
		
		public Builder withMinimumTimeToLive(int ttl) {
			data.minimumTimeToLive = ttl;
			
			return this;
		}
		
		public Builder withMplsLabel1(int label) {
			data.mplsLabel1 = label;
			
			return this;
		}
		
		public Builder withMplsLabel2(int label) {
			data.mplsLabel2 = label;
			
			return this;
		}
		
		public Builder withMplsLabel3(int label) {
			data.mplsLabel3 = label;
			
			return this;
		}
		
		public Builder withMplsLabel4(int label) {
			data.mplsLabel4 = label;
			
			return this;
		}
		
		public Builder withMplsLabel5(int label) {
			data.mplsLabel5 = label;
			
			return this;
		}
		
		public Builder withMplsLabel6(int label) {
			data.mplsLabel6 = label;
			
			return this;
		}
		
		public Builder withMplsLabel7(int label) {
			data.mplsLabel7 = label;
			
			return this;
		}
		
		public Builder withMplsLabel8(int label) {
			data.mplsLabel8 = label;
			
			return this;
		}
		
		public Builder withMplsLabel9(int label) {
			data.mplsLabel9 = label;
			
			return this;
		}
		
		public Builder withMplsLabel10(int label) {
			data.mplsLabel10 = label;
			
			return this;
		}
		
		public Builder withMplsTopLabelIpAddress(String address) {
			data.mplsTopLabelIpAddress = address;
			
			return this;
		}
		
		public Builder withMplsTopLabelType(MPLSTopLabelType type) {
			data.mplsTopLabelType = type;
			
			return this;
		}
		
		public Builder withOutputBytes(BigInteger outputBytes) {
			data.outputBytes = outputBytes;
			
			return this;
		}
		
		public Builder withOutputPackets(BigInteger outputPackets) {
			data.outputPkts = outputPackets;
			
			return this;
		}

		public Builder withOutputSnmpIndex(int outputSnmpIndex) {
			data.outputSnmpIndex = outputSnmpIndex;
			
			return this;
		}
		
		public Builder withProtocol(IPProtocol protocol) {
			data.protocol = protocol;
			
			return this;
		}
		
		public Builder withSourceAs(int as) {
			data.sourceAs = as;
			
			return this;
		}
		
		public Builder withSourceMac(String mac) {
			data.sourceMac = mac;
			
			return this;
		}
		
		public Builder withSourceVlan(int vlan) {
			data.sourceVlan = vlan;
			
			return this;
		}
		
		public Builder withTcpFlags(TCPFLags flag) {
			if(this.tcpFlags == null)
				this.tcpFlags = new TreeSet<TCPFLags>();

			this.tcpFlags.add(flag);
			
			return this;
		}
		
		public Builder withTypeOfService(IPTypeOfService tos) {
			data.typeOfService = tos;
			
			return this;
		}
	}
 
	@JsonProperty(value="inputByte")
	private BigInteger inputBytes;

	@JsonProperty(value="inputPackets")
	private BigInteger inputPkts;

	@JsonProperty(value="flows")
	private BigInteger flows;

	@JsonProperty(value="protocol")	
	@JsonFormat(shape=Shape.STRING)
	private IPProtocol protocol;
	
	@JsonProperty(value="tos")	
	@JsonFormat(shape=Shape.STRING)
	private IPTypeOfService typeOfService;

	@JsonProperty(value="tcpFlags")	
	@JsonFormat(shape=Shape.STRING)
	private TCPFLags[] tcpFlags;
	
	@JsonProperty(value="l4SrcPort")
	private Integer layer4SourcePort;

	@JsonProperty(value="ipv4SrcAddress")
	private String ipv4SourceAddress;

	@JsonProperty(value="srcMask")
	private Integer ipv4SourceMask;

	@JsonProperty(value="inputSnmpIndex")
	private Integer inputSnmpIndex;

	@JsonProperty(value="l4DstPort")
	private Integer layer4DestinationPort;

	@JsonProperty(value="ipv4DstAddress")
	private String ipv4DestinationAddress;

	@JsonProperty(value="dstMask")
	private Integer ipv4DestinationMask;

	@JsonProperty(value="outputSnmpIndex")
	private Integer outputSnmpIndex;

	@JsonProperty(value="ipv4NextHop")
	private String ipv4NextHopAddress;

	@JsonProperty(value="srcAs")
	private Integer sourceAs;

	@JsonProperty(value="dstAs")
	private Integer destinationAs;

	@JsonProperty(value="bgpIpv4NextHop")
	private String ipv4BgpNextHopAddress;

	@JsonProperty(value="mulDstPkts")
	private BigInteger multicastDestinationPackets;

	@JsonProperty(value="mulDstBytes")
	private BigInteger multicastDestinationBytes;

	@JsonProperty(value="lastSwitched")
	private int lastSwitched;

	@JsonProperty(value="firstSwitched")
	private int firstSwitched;
			
	@JsonProperty(value="outputByte")
	private BigInteger outputBytes;

	@JsonProperty(value="outputPackets")
	private BigInteger outputPkts;
	
	@JsonProperty(value="ipv6SrcAddress")
	private String ipv6SourceAddress;

	@JsonProperty(value="ipv6DstAddress")
	private String ipv6DestinationAddress;
	
	@JsonProperty(value="ipv6SrcMask")
	private Integer ipv6SourceMask;
	
	@JsonProperty(value="ipv6SrcMask")
	private Integer ipv6DestinationMask;
	
	@JsonProperty(value="ipv6FlowLabel")
	private Integer ipv6FlowLabel;

	@JsonProperty(value="icmpType")
	private ICMPType icmpType;
	
	@JsonProperty(value="icmpCode")
	private ICMPCode icmpCode;
	
	@JsonProperty(value="igmpType")
	private IGMPType igmpType;
	
	@JsonProperty(value="mplsTopLabelType")
	@JsonFormat(shape=Shape.STRING)
	private MPLSTopLabelType mplsTopLabelType;
	
	@JsonProperty(value="mplsTopLabelIpAddr")
	private String mplsTopLabelIpAddress;

	@JsonProperty(value="dstTos")
	@JsonFormat(shape=Shape.STRING)
	private IPTypeOfService destinationTypeOfService;
	
	@JsonProperty(value="srcMac")
	private String sourceMac;
	
	@JsonProperty(value="dstMax")
	private String destinationMac;
	
	@JsonProperty(value="srcVlan")
	private Integer sourceVlan;
	
	@JsonProperty(value="dstVlan")
	private Integer destinationVlan;
	
	@JsonProperty(value="ipProtocolVersion")
	@JsonFormat(shape=Shape.STRING)
	private IPProtocolVersion ipProtocolVersion;
	
	@JsonProperty(value="direction")
	@JsonFormat(shape=Shape.STRING)
	private FlowDirection flowDirection;
	
	@JsonProperty(value="ipv6NextHop")
	private String ipv6NextHopAddress;

	@JsonProperty(value="bgpIpv6NextHop")
	private String ipv6BgpNextHopAddress;
	
	@JsonProperty(value="ipv6OptionHeaders")
	private Integer ipv6OptionHeaders;
	
	@JsonProperty(value="mplsLabel1")
	private Integer mplsLabel1;
	
	@JsonProperty(value="mplsLabel2")
	private Integer mplsLabel2;

	@JsonProperty(value="mplsLabel3")
	private Integer mplsLabel3;
	
	@JsonProperty(value="mplsLabel4")
	private Integer mplsLabel4;
	
	@JsonProperty(value="mplsLabel5")
	private Integer mplsLabel5;
	
	@JsonProperty(value="mplsLabel6")
	private Integer mplsLabel6;
	
	@JsonProperty(value="mplsLabel7")
	private Integer mplsLabel7;
	
	@JsonProperty(value="mplsLabe8")
	private Integer mplsLabel8;
	
	@JsonProperty(value="mplsLabel9")
	private Integer mplsLabel9;
	
	@JsonProperty(value="mplsLabel10")
	private Integer mplsLabel10;

	@JsonProperty(value="flowSamplerID")
	private Integer flowSamplerID;
	
	@JsonProperty(value="flowClass")
	private Integer flowClass;
	
	@JsonProperty(value="minPktLength")
	private Integer minimumPacketLength;
	
	@JsonProperty(value="maxPktLength")
	private Integer maximumPacketLength;
	
	@JsonProperty(value="minTtl")
	private Integer minimumTimeToLive;

	@JsonProperty(value="maxTttl")
	private Integer maximumTimeToLive;

	@JsonProperty(value="ipv4Ident")
	private Integer ipv4Identity;
	
	/**
	 * @return the inputBytes
	 */
	public BigInteger getInputBytes() {
		return inputBytes;
	}

	/**
	 * @param inputBytes the inputBytes to set
	 */
	public void setInputBytes(BigInteger inputBytes) {
		this.inputBytes = inputBytes;
	}

	/**
	 * @return the inputPkts
	 */
	public BigInteger getInputPkts() {
		return inputPkts;
	}

	/**
	 * @param inputPkts the inputPkts to set
	 */
	public void setInputPkts(BigInteger inputPkts) {
		this.inputPkts = inputPkts;
	}

	/**
	 * @return the flows
	 */
	public BigInteger getFlows() {
		return flows;
	}

	/**
	 * @param flows the flows to set
	 */
	public void setFlows(BigInteger flows) {
		this.flows = flows;
	}

	/**
	 * @return the protocol
	 */
	public IPProtocol getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(IPProtocol protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the typeOfService
	 */
	public IPTypeOfService getTypeOfService() {
		return typeOfService;
	}

	/**
	 * @param typeOfService the typeOfService to set
	 */
	public void setTypeOfService(IPTypeOfService typeOfService) {
		this.typeOfService = typeOfService;
	}

	/**
	 * @return the tcpFlags
	 */
	public TCPFLags[] getTcpFlags() {
		return tcpFlags;
	}

	/**
	 * @param tcpFlags the tcpFlags to set
	 */
	public void setTcpFlags(TCPFLags[] tcpFlags) {
		this.tcpFlags = tcpFlags;
	}

	/**
	 * @return the layer4SourcePort
	 */
	public Integer getLayer4SourcePort() {
		return layer4SourcePort;
	}

	/**
	 * @param layer4SourcePort the layer4SourcePort to set
	 */
	public void setLayer4SourcePort(Integer layer4SourcePort) {
		this.layer4SourcePort = layer4SourcePort;
	}

	/**
	 * @return the ipv4SourceAddress
	 */
	public String getIpv4SourceAddress() {
		return ipv4SourceAddress;
	}

	/**
	 * @param ipv4SourceAddress the ipv4SourceAddress to set
	 */
	public void setIpv4SourceAddress(String ipv4SourceAddress) {
		this.ipv4SourceAddress = ipv4SourceAddress;
	}

	/**
	 * @return the ipv4SourceMask
	 */
	public Integer getIpv4SourceMask() {
		return ipv4SourceMask;
	}

	/**
	 * @param ipv4SourceMask the ipv4SourceMask to set
	 */
	public void setIpv4SourceMask(Integer ipv4SourceMask) {
		this.ipv4SourceMask = ipv4SourceMask;
	}

	/**
	 * @return the inputSnmpIndex
	 */
	public Integer getInputSnmpIndex() {
		return inputSnmpIndex;
	}

	/**
	 * @param inputSnmpIndex the inputSnmpIndex to set
	 */
	public void setInputSnmpIndex(Integer inputSnmpIndex) {
		this.inputSnmpIndex = inputSnmpIndex;
	}

	/**
	 * @return the layer4DestinationPort
	 */
	public Integer getLayer4DestinationPort() {
		return layer4DestinationPort;
	}

	/**
	 * @param layer4DestinationPort the layer4DestinationPort to set
	 */
	public void setLayer4DestinationPort(Integer layer4DestinationPort) {
		this.layer4DestinationPort = layer4DestinationPort;
	}

	/**
	 * @return the ipv4DestinationAddress
	 */
	public String getIpv4DestinationAddress() {
		return ipv4DestinationAddress;
	}

	/**
	 * @param ipv4DestinationAddress the ipv4DestinationAddress to set
	 */
	public void setIpv4DestinationAddress(String ipv4DestinationAddress) {
		this.ipv4DestinationAddress = ipv4DestinationAddress;
	}

	/**
	 * @return the ipv4DestinationMask
	 */
	public Integer getIpv4DestinationMask() {
		return ipv4DestinationMask;
	}

	/**
	 * @param ipv4DestinationMask the ipv4DestinationMask to set
	 */
	public void setIpv4DestinationMask(Integer ipv4DestinationMask) {
		this.ipv4DestinationMask = ipv4DestinationMask;
	}

	/**
	 * @return the outputSnmpIndex
	 */
	public Integer getOutputSnmpIndex() {
		return outputSnmpIndex;
	}

	/**
	 * @param outputSnmpIndex the outputSnmpIndex to set
	 */
	public void setOutputSnmpIndex(Integer outputSnmpIndex) {
		this.outputSnmpIndex = outputSnmpIndex;
	}

	/**
	 * @return the ipv4NextHopAddress
	 */
	public String getIpv4NextHopAddress() {
		return ipv4NextHopAddress;
	}

	/**
	 * @param ipv4NextHopAddress the ipv4NextHopAddress to set
	 */
	public void setIpv4NextHopAddress(String ipv4NextHopAddress) {
		this.ipv4NextHopAddress = ipv4NextHopAddress;
	}

	/**
	 * @return the sourceAs
	 */
	public Integer getSourceAs() {
		return sourceAs;
	}

	/**
	 * @param sourceAs the sourceAs to set
	 */
	public void setSourceAs(Integer sourceAs) {
		this.sourceAs = sourceAs;
	}

	/**
	 * @return the destinationAs
	 */
	public Integer getDestinationAs() {
		return destinationAs;
	}

	/**
	 * @param destinationAs the destinationAs to set
	 */
	public void setDestinationAs(Integer destinationAs) {
		this.destinationAs = destinationAs;
	}

	/**
	 * @return the bgpIpv4NextHopAddress
	 */
	public String getIpv4BgpNextHopAddress() {
		return ipv4BgpNextHopAddress;
	}

	/**
	 * @param bgpIpv4NextHopAddress the bgpIpv4NextHopAddress to set
	 */
	public void setIpv4BgpNextHopAddress(String bgpIpv4NextHopAddress) {
		this.ipv4BgpNextHopAddress = bgpIpv4NextHopAddress;
	}

	/**
	 * @return the multicastDestinationPackets
	 */
	public BigInteger getMulticastDestinationPackets() {
		return multicastDestinationPackets;
	}

	/**
	 * @param multicastDestinationPackets the multicastDestinationPackets to set
	 */
	public void setMulticastDestinationPackets(
			BigInteger multicastDestinationPackets) {
		this.multicastDestinationPackets = multicastDestinationPackets;
	}

	/**
	 * @return the multicastDestinationBytes
	 */
	public BigInteger getMulticastDestinationBytes() {
		return multicastDestinationBytes;
	}

	/**
	 * @param multicastDestinationBytes the multicastDestinationBytes to set
	 */
	public void setMulticastDestinationBytes(BigInteger multicastDestinationBytes) {
		this.multicastDestinationBytes = multicastDestinationBytes;
	}

	/**
	 * @return the lastSwitched
	 */
	public int getLastSwitched() {
		return lastSwitched;
	}

	/**
	 * @param lastSwitched the lastSwitched to set
	 */
	public void setLastSwitched(int lastSwitched) {
		this.lastSwitched = lastSwitched;
	}

	/**
	 * @return the firstSwitched
	 */
	public int getFirstSwitched() {
		return firstSwitched;
	}

	/**
	 * @param firstSwitched the firstSwitched to set
	 */
	public void setFirstSwitched(int firstSwitched) {
		this.firstSwitched = firstSwitched;
	}

	/**
	 * @return the outputBytes
	 */
	public BigInteger getOutputBytes() {
		return outputBytes;
	}

	/**
	 * @param outputBytes the outputBytes to set
	 */
	public void setOutputBytes(BigInteger outputBytes) {
		this.outputBytes = outputBytes;
	}

	/**
	 * @return the outputPkts
	 */
	public BigInteger getOutputPkts() {
		return outputPkts;
	}

	/**
	 * @param outputPkts the outputPkts to set
	 */
	public void setOutputPkts(BigInteger outputPkts) {
		this.outputPkts = outputPkts;
	}

	/**
	 * @return the ipv6SourceAddress
	 */
	public String getIpv6SourceAddress() {
		return ipv6SourceAddress;
	}

	/**
	 * @param ipv6SourceAddress the ipv6SourceAddress to set
	 */
	public void setIpv6SourceAddress(String ipv6SourceAddress) {
		this.ipv6SourceAddress = ipv6SourceAddress;
	}

	/**
	 * @return the ipv6DestinationAddress
	 */
	public String getIpv6DestinationAddress() {
		return ipv6DestinationAddress;
	}

	/**
	 * @param ipv6DestinationAddress the ipv6DestinationAddress to set
	 */
	public void setIpv6DestinationAddress(String ipv6DestinationAddress) {
		this.ipv6DestinationAddress = ipv6DestinationAddress;
	}

	/**
	 * @return the ipv6SourceMask
	 */
	public Integer getIpv6SourceMask() {
		return ipv6SourceMask;
	}

	/**
	 * @param ipv6SourceMask the ipv6SourceMask to set
	 */
	public void setIpv6SourceMask(Integer ipv6SourceMask) {
		this.ipv6SourceMask = ipv6SourceMask;
	}

	/**
	 * @return the ipv6DestinationMask
	 */
	public Integer getIpv6DestinationMask() {
		return ipv6DestinationMask;
	}

	/**
	 * @param ipv6DestinationMask the ipv6DestinationMask to set
	 */
	public void setIpv6DestinationMask(Integer ipv6DestinationMask) {
		this.ipv6DestinationMask = ipv6DestinationMask;
	}

	/**
	 * @return the ipv6FlowLabel
	 */
	public Integer getIpv6FlowLabel() {
		return ipv6FlowLabel;
	}

	/**
	 * @param ipv6FlowLabel the ipv6FlowLabel to set
	 */
	public void setIpv6FlowLabel(Integer ipv6FlowLabel) {
		this.ipv6FlowLabel = ipv6FlowLabel;
	}

	/**
	 * @return the icmpType
	 */
	public ICMPType getIcmpType() {
		return icmpType;
	}

	/**
	 * @param icmpType the icmpType to set
	 */
	public void setIcmpType(ICMPType icmpType) {
		this.icmpType = icmpType;
	}

	/**
	 * @return the icmpCode
	 */
	public ICMPCode getIcmpCode() {
		return icmpCode;
	}

	/**
	 * @param icmpCode the icmpCode to set
	 */
	public void setIcmpCode(ICMPCode icmpCode) {
		this.icmpCode = icmpCode;
	}

	/**
	 * @return the igmpType
	 */
	public IGMPType getIgmpType() {
		return igmpType;
	}

	/**
	 * @param igmpType the igmpType to set
	 */
	public void setIgmpType(IGMPType igmpType) {
		this.igmpType = igmpType;
	}

	/**
	 * @return the mplsTopLabelType
	 */
	public MPLSTopLabelType getMplsTopLabelType() {
		return mplsTopLabelType;
	}

	/**
	 * @param mplsTopLabelType the mplsTopLabelType to set
	 */
	public void setMplsTopLabelType(MPLSTopLabelType mplsTopLabelType) {
		this.mplsTopLabelType = mplsTopLabelType;
	}

	/**
	 * @return the mplsTopLabelIpAddress
	 */
	public String getMplsTopLabelIpAddress() {
		return mplsTopLabelIpAddress;
	}

	/**
	 * @param mplsTopLabelIpAddress the mplsTopLabelIpAddress to set
	 */
	public void setMplsTopLabelIpAddress(String mplsTopLabelIpAddress) {
		this.mplsTopLabelIpAddress = mplsTopLabelIpAddress;
	}

	/**
	 * @return the destinationTypeOfService
	 */
	public IPTypeOfService getDestinationTypeOfService() {
		return destinationTypeOfService;
	}

	/**
	 * @param destinationTypeOfService the destinationTypeOfService to set
	 */
	public void setDestinationTypeOfService(IPTypeOfService destinationTypeOfService) {
		this.destinationTypeOfService = destinationTypeOfService;
	}

	/**
	 * @return the sourceMac
	 */
	public String getSourceMac() {
		return sourceMac;
	}

	/**
	 * @param sourceMac the sourceMac to set
	 */
	public void setSourceMac(String sourceMac) {
		this.sourceMac = sourceMac;
	}

	/**
	 * @return the destinationMac
	 */
	public String getDestinationMac() {
		return destinationMac;
	}

	/**
	 * @param destinationMac the destinationMac to set
	 */
	public void setDestinationMac(String destinationMac) {
		this.destinationMac = destinationMac;
	}

	/**
	 * @return the sourceVlan
	 */
	public Integer getSourceVlan() {
		return sourceVlan;
	}

	/**
	 * @param sourceVlan the sourceVlan to set
	 */
	public void setSourceVlan(Integer sourceVlan) {
		this.sourceVlan = sourceVlan;
	}

	/**
	 * @return the destinationVlan
	 */
	public Integer getDestinationVlan() {
		return destinationVlan;
	}

	/**
	 * @param destinationVlan the destinationVlan to set
	 */
	public void setDestinationVlan(Integer destinationVlan) {
		this.destinationVlan = destinationVlan;
	}

	/**
	 * @return the ipProtocolVersion
	 */
	public IPProtocolVersion getIpProtocolVersion() {
		return ipProtocolVersion;
	}

	/**
	 * @param ipProtocolVersion the ipProtocolVersion to set
	 */
	public void setIpProtocolVersion(IPProtocolVersion ipProtocolVersion) {
		this.ipProtocolVersion = ipProtocolVersion;
	}

	/**
	 * @return the flowDirection
	 */
	public FlowDirection getFlowDirection() {
		return flowDirection;
	}

	/**
	 * @param flowDirection the flowDirection to set
	 */
	public void setFlowDirection(FlowDirection flowDirection) {
		this.flowDirection = flowDirection;
	}

	/**
	 * @return the ipv6NextHopAddress
	 */
	public String getIpv6NextHopAddress() {
		return ipv6NextHopAddress;
	}

	/**
	 * @param ipv6NextHopAddress the ipv6NextHopAddress to set
	 */
	public void setIpv6NextHopAddress(String ipv6NextHopAddress) {
		this.ipv6NextHopAddress = ipv6NextHopAddress;
	}

	/**
	 * @return the bgpIpv6NextHopAddress
	 */
	public String getIpv6BgpNextHopAddress() {
		return ipv6BgpNextHopAddress;
	}

	/**
	 * @param bgpIpv6NextHopAddress the bgpIpv6NextHopAddress to set
	 */
	public void setIpv6BgpNextHopAddress(String bgpIpv6NextHopAddress) {
		this.ipv6BgpNextHopAddress = bgpIpv6NextHopAddress;
	}

	/**
	 * @return the ipv6OptionHeaders
	 */
	public Integer getIpv6OptionHeaders() {
		return ipv6OptionHeaders;
	}

	/**
	 * @param ipv6OptionHeaders the ipv6OptionHeaders to set
	 */
	public void setIpv6OptionHeaders(Integer ipv6OptionHeaders) {
		this.ipv6OptionHeaders = ipv6OptionHeaders;
	}

	/**
	 * @return the mplsLabel1
	 */
	public Integer getMplsLabel1() {
		return mplsLabel1;
	}

	/**
	 * @param mplsLabel1 the mplsLabel1 to set
	 */
	public void setMplsLabel1(Integer mplsLabel1) {
		this.mplsLabel1 = mplsLabel1;
	}

	/**
	 * @return the mplsLabel2
	 */
	public Integer getMplsLabel2() {
		return mplsLabel2;
	}

	/**
	 * @param mplsLabel2 the mplsLabel2 to set
	 */
	public void setMplsLabel2(Integer mplsLabel2) {
		this.mplsLabel2 = mplsLabel2;
	}

	/**
	 * @return the mplsLabel3
	 */
	public Integer getMplsLabel3() {
		return mplsLabel3;
	}

	/**
	 * @param mplsLabel3 the mplsLabel3 to set
	 */
	public void setMplsLabel3(Integer mplsLabel3) {
		this.mplsLabel3 = mplsLabel3;
	}

	/**
	 * @return the mplsLabel4
	 */
	public Integer getMplsLabel4() {
		return mplsLabel4;
	}

	/**
	 * @param mplsLabel4 the mplsLabel4 to set
	 */
	public void setMplsLabel4(Integer mplsLabel4) {
		this.mplsLabel4 = mplsLabel4;
	}

	/**
	 * @return the mplsLabel5
	 */
	public Integer getMplsLabel5() {
		return mplsLabel5;
	}

	/**
	 * @param mplsLabel5 the mplsLabel5 to set
	 */
	public void setMplsLabel5(Integer mplsLabel5) {
		this.mplsLabel5 = mplsLabel5;
	}

	/**
	 * @return the mplsLabel6
	 */
	public Integer getMplsLabel6() {
		return mplsLabel6;
	}

	/**
	 * @param mplsLabel6 the mplsLabel6 to set
	 */
	public void setMplsLabel6(Integer mplsLabel6) {
		this.mplsLabel6 = mplsLabel6;
	}

	/**
	 * @return the mplsLabel7
	 */
	public Integer getMplsLabel7() {
		return mplsLabel7;
	}

	/**
	 * @param mplsLabel7 the mplsLabel7 to set
	 */
	public void setMplsLabel7(Integer mplsLabel7) {
		this.mplsLabel7 = mplsLabel7;
	}

	/**
	 * @return the mplsLabel8
	 */
	public Integer getMplsLabel8() {
		return mplsLabel8;
	}

	/**
	 * @param mplsLabel8 the mplsLabel8 to set
	 */
	public void setMplsLabel8(Integer mplsLabel8) {
		this.mplsLabel8 = mplsLabel8;
	}

	/**
	 * @return the mplsLabel9
	 */
	public Integer getMplsLabel9() {
		return mplsLabel9;
	}

	/**
	 * @param mplsLabel9 the mplsLabel9 to set
	 */
	public void setMplsLabel9(Integer mplsLabel9) {
		this.mplsLabel9 = mplsLabel9;
	}

	/**
	 * @return the mplsLabel10
	 */
	public Integer getMplsLabel10() {
		return mplsLabel10;
	}

	/**
	 * @param mplsLabel10 the mplsLabel10 to set
	 */
	public void setMplsLabel10(Integer mplsLabel10) {
		this.mplsLabel10 = mplsLabel10;
	}

	/**
	 * @return the flowSamplerID
	 */
	public Integer getFlowSamplerID() {
		return flowSamplerID;
	}

	/**
	 * @param flowSamplerID the flowSamplerID to set
	 */
	public void setFlowSamplerID(Integer flowSamplerID) {
		this.flowSamplerID = flowSamplerID;
	}

	/**
	 * @return the flowClass
	 */
	public Integer getFlowClass() {
		return flowClass;
	}

	/**
	 * @param flowClass the flowClass to set
	 */
	public void setFlowClass(Integer flowClass) {
		this.flowClass = flowClass;
	}

	/**
	 * @return the minimumPacketLength
	 */
	public Integer getMinimumPacketLength() {
		return minimumPacketLength;
	}

	/**
	 * @param minimumPacketLength the minimumPacketLength to set
	 */
	public void setMinimumPacketLength(Integer minimumPacketLength) {
		this.minimumPacketLength = minimumPacketLength;
	}

	/**
	 * @return the maximumPacketLength
	 */
	public Integer getMaximumPacketLength() {
		return maximumPacketLength;
	}

	/**
	 * @param maximumPacketLength the maximumPacketLength to set
	 */
	public void setMaximumPacketLength(Integer maximumPacketLength) {
		this.maximumPacketLength = maximumPacketLength;
	}

	/**
	 * @return the minimumTimeToLive
	 */
	public Integer getMinimumTimeToLive() {
		return minimumTimeToLive;
	}

	/**
	 * @param minimumTimeToLive the minimumTimeToLive to set
	 */
	public void setMinimumTimeToLive(Integer minimumTimeToLive) {
		this.minimumTimeToLive = minimumTimeToLive;
	}

	/**
	 * @return the maximumTimeToLive
	 */
	public Integer getMaximumTimeToLive() {
		return maximumTimeToLive;
	}

	/**
	 * @param maximumTimeToLive the maximumTimeToLive to set
	 */
	public void setMaximumTimeToLive(Integer maximumTimeToLive) {
		this.maximumTimeToLive = maximumTimeToLive;
	}

	/**
	 * @return the ipv4Identity
	 */
	public Integer getIpv4Identity() {
		return ipv4Identity;
	}

	/**
	 * @param ipv4Identity the ipv4Identity to set
	 */
	public void setIpv4Identity(Integer ipv4Identity) {
		this.ipv4Identity = ipv4Identity;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public int hashCode() {
		return (new HashCodeBuilder())
				.append(destinationAs)
				.append(destinationMac)
				.append(destinationTypeOfService)
				.append(destinationVlan)
				.append(firstSwitched)
				.append(flowClass)
				.append(flowDirection)
				.append(flows)
				.append(flowSamplerID)
				.append(icmpCode)
				.append(icmpType)
				.append(igmpType)
				.append(inputBytes)
				.append(inputPkts)
				.append(inputSnmpIndex)
				.append(ipProtocolVersion)
				.append(ipv4BgpNextHopAddress)
				.append(ipv4DestinationAddress)
				.append(ipv4DestinationMask)
				.append(ipv4Identity)
				.append(ipv4NextHopAddress)
				.append(ipv4SourceAddress)
				.append(ipv4SourceMask)
				.append(ipv6BgpNextHopAddress)
				.append(ipv6DestinationAddress)
				.append(ipv6DestinationMask)
				.append(ipv6FlowLabel)
				.append(ipv6NextHopAddress)
				.append(ipv6SourceAddress)
				.append(ipv6SourceMask)
				.append(lastSwitched)
				.append(layer4DestinationPort)
				.append(layer4SourcePort)
				.append(maximumPacketLength)
				.append(maximumTimeToLive)
				.append(minimumPacketLength)
				.append(minimumTimeToLive)
				.append(mplsLabel1)
				.append(mplsLabel2)
				.append(mplsLabel3)
				.append(mplsLabel4)
				.append(mplsLabel5)
				.append(mplsLabel6)
				.append(mplsLabel7)
				.append(mplsLabel8)
				.append(mplsLabel9)
				.append(mplsLabel10)
				.append(mplsTopLabelIpAddress)
				.append(mplsTopLabelType)
				.append(multicastDestinationBytes)
				.append(multicastDestinationPackets)
				.append(outputBytes)
				.append(outputPkts)
				.append(outputSnmpIndex)
				.append(protocol)
				.append(sourceAs)
				.append(sourceMac)
				.append(sourceVlan)
				.append(tcpFlags)
				.append(typeOfService)
				.toHashCode();
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof FlowData))
			return false;
		
		FlowData o = (FlowData)obj;
		
		return (new EqualsBuilder())
				.append(destinationAs, o.destinationAs)
				.append(destinationMac, o.destinationMac)
				.append(destinationTypeOfService, o.destinationTypeOfService)
				.append(destinationVlan, o.destinationVlan)
				.append(firstSwitched, o.firstSwitched)
				.append(flowClass, o.flowClass)
				.append(flowDirection, o.flowDirection)
				.append(flows, o.flows)
				.append(flowSamplerID, o.flowSamplerID)
				.append(icmpCode, o.icmpCode)
				.append(icmpType, o.icmpType)
				.append(igmpType, o.igmpType)
				.append(inputBytes, o.inputBytes)
				.append(inputPkts, o.inputPkts)
				.append(inputSnmpIndex, o.inputSnmpIndex)
				.append(ipProtocolVersion, o.ipProtocolVersion)
				.append(ipv4BgpNextHopAddress, o.ipv4BgpNextHopAddress)
				.append(ipv4DestinationAddress, o.ipv4DestinationAddress)
				.append(ipv4DestinationMask, o.ipv4DestinationMask)
				.append(ipv4Identity, o.ipv4Identity)
				.append(ipv4NextHopAddress, o.ipv4NextHopAddress)
				.append(ipv4SourceAddress, o.ipv4SourceAddress)
				.append(ipv4SourceMask, o.ipv4SourceMask)
				.append(ipv6BgpNextHopAddress, o.ipv6BgpNextHopAddress)
				.append(ipv6DestinationAddress, o.ipv6DestinationAddress)
				.append(ipv6DestinationMask, o.ipv6DestinationMask)
				.append(ipv6FlowLabel, o.ipv6FlowLabel)
				.append(ipv6NextHopAddress, o.ipv6NextHopAddress)
				.append(ipv6SourceAddress, o.ipv6SourceAddress)
				.append(ipv6SourceMask, o.ipv6SourceMask)
				.append(lastSwitched, o.lastSwitched)
				.append(layer4DestinationPort, o.layer4DestinationPort)
				.append(layer4SourcePort, o.layer4SourcePort)
				.append(maximumPacketLength, o.maximumPacketLength)
				.append(maximumTimeToLive, o.maximumTimeToLive)
				.append(minimumPacketLength, o.minimumPacketLength)
				.append(minimumTimeToLive, o.minimumTimeToLive)
				.append(mplsLabel1, o.mplsLabel1)
				.append(mplsLabel2, o.mplsLabel2)
				.append(mplsLabel3, o.mplsLabel3)
				.append(mplsLabel4, o.mplsLabel4)
				.append(mplsLabel5, o.mplsLabel5)
				.append(mplsLabel6, o.mplsLabel6)
				.append(mplsLabel7, o.mplsLabel7)
				.append(mplsLabel8, o.mplsLabel8)
				.append(mplsLabel9, o.mplsLabel9)
				.append(mplsLabel10, o.mplsLabel10)
				.append(mplsTopLabelIpAddress, o.mplsTopLabelIpAddress)
				.append(mplsTopLabelType, o.mplsTopLabelType)
				.append(multicastDestinationBytes, o.multicastDestinationBytes)
				.append(multicastDestinationPackets, o.multicastDestinationPackets)
				.append(outputBytes, o.outputBytes)
				.append(outputPkts, o.outputPkts)
				.append(outputSnmpIndex, o.outputSnmpIndex)
				.append(protocol, o.protocol)
				.append(sourceAs, o.sourceAs)
				.append(sourceMac, o.sourceMac)
				.append(sourceVlan, o.sourceVlan)
				.append(tcpFlags, o.tcpFlags)
				.append(typeOfService, o.typeOfService)
				.isEquals();
	}
}
