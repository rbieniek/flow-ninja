/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.Header;
import org.flowninja.collector.common.netflow9.types.Template;
import org.flowninja.collector.common.netflow9.types.TemplateField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Netwflow9 datagram packet decoder implementation. 
 * 
 * This decoder receives a byte-encoded netflow 9 packet
 * and sents a decoded packet object upstream.
 * 
 * The packet decoder temporarily stores received flow packet if a matching template record has not been received yet
 * 
 * @author rainer
 *
 */
public class Netflow9PacketDecoder extends ByteToMessageDecoder {
	private static final Logger logger = LoggerFactory.getLogger(Netflow9PacketDecoder.class);
	
	private static final int PACKET_HEADER_LENGTH = 20;
	private static final int VERSION_NUMBER = 9;
	private static final int TEMPLATE_FLOWSET_ID = 0;
	private static final int OPTIONS_TEMPLATE_FLOWSET_ID = 1;
	
	private PeerRegistry peerRegistry;
	private PeerAddressMapper peerAddressMapper = new InetSocketAddressPeerAddressMapper();
	
	/**
	 * @param peerFlowRegistry the peerFlowRegistry to set
	 */
	public void setPeerRegistry(PeerRegistry peerFlowRegistry) {
		this.peerRegistry = peerFlowRegistry;
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("netflow 9 collector server channel is active");
	}

	/* (non-Javadoc)
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty.channel.ChannelHandlerContext)
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("netflow 9 collector server channel is inactive");
	}

	/**
	 * main packet decoder routine. 
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		InetAddress peerAddress = peerAddressMapper.mapRemoteAddress(ctx.channel().remoteAddress());
		
		logger.info("received flow packet from peer {}", peerAddress);
		
		if(in.readableBytes() > PACKET_HEADER_LENGTH) {
			int versionNumber = in.readUnsignedShort();
			
			if(versionNumber == VERSION_NUMBER) {
				Header header = new Header(in.readUnsignedShort(), 
						in.readUnsignedInt(), 
						in.readUnsignedInt(), 
						in.readUnsignedInt(), 
						in.readUnsignedInt());

				List<Template> templates = new LinkedList<Template>();
				List<FlowBuffer> flows = new LinkedList<FlowBuffer>();
				
				int recordNumber = 0;
				while(in.readableBytes() > 4) {					
					int flowSetId = in.readUnsignedShort();
					int length = in.readUnsignedShort();
					int remainingOctets = length - 4; // subtract length of flowset ID and flowset length 
					
					if(in.readableBytes() < remainingOctets) {
						logger.error("packet short to {} bytes when {} bytes required in record {}", 
								in.readableBytes(), remainingOctets, recordNumber);

						return;
					}

					ByteBuf workBuf = in.readSlice(remainingOctets);
					
					switch(flowSetId) {
					case TEMPLATE_FLOWSET_ID:
						while(workBuf.readableBytes() > 4) {
							int flowSetID = workBuf.readUnsignedShort();
							int fieldCount = workBuf.readUnsignedShort();
							List<TemplateField> fields = new LinkedList<TemplateField>();
							
							if(workBuf.readableBytes() < fieldCount * 4) {
								logger.error("packet short to {} bytes when {} bytes required in record {}",
										workBuf.readableBytes(), fieldCount * 4, recordNumber);
								
								return;
							}
							
							for(int fieldNumber=0; fieldNumber < fieldCount; fieldNumber++) {
								fields.add(new TemplateField(FieldType.fromCode(workBuf.readUnsignedShort()), 
										workBuf.readUnsignedShort()));
							}
							
							templates.add(new Template(flowSetID, fields));								
							recordNumber++;
						}
						break;
					case OPTIONS_TEMPLATE_FLOWSET_ID: {
							int flowSetID = workBuf.readUnsignedShort();
							
							recordNumber++;
						}
						break;
					default:
						flows.add(new FlowBuffer(flowSetId, workBuf));
					}
				}
				
				FlowRegistry flowRegistry = peerRegistry.registryForPeerAddress(peerAddress, header.getSourceId());
				

				flowRegistry.addFlowTemplates(templates);
			} 
		} else {
			logger.error("dropping received packet with {} bytes size but expected at least {} bytes", 
					in.readableBytes(), PACKET_HEADER_LENGTH);
		}
	}

	/**
	 * @param peerAddressMapper the peerAddressMapper to set
	 */
	public void setPeerAddressMapper(PeerAddressMapper peerAddressMapper) {
		this.peerAddressMapper = peerAddressMapper;
	}
}
