/**
 *
 */
package org.flowninja.collector.netflow9.components;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.flowninja.collector.common.netflow9.types.DataTemplate;
import org.flowninja.collector.common.netflow9.types.DataTemplateField;
import org.flowninja.collector.common.netflow9.types.FieldType;
import org.flowninja.collector.common.netflow9.types.Header;
import org.flowninja.collector.common.netflow9.types.OptionField;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.common.netflow9.types.ScopeField;
import org.flowninja.collector.common.netflow9.types.ScopeType;
import org.flowninja.collector.netflow9.packet.FlowBuffer;
import org.flowninja.collector.netflow9.packet.Netflow9DecodedDatagram;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Netwflow9 datagram packet decoder implementation.
 *
 * This decoder receives a byte-encoded netflow 9 packet and sents a decoded packet object upstream.
 *
 * The packet decoder temporarily stores received flow packet if a matching template record has not been received yet
 *
 * @author rainer
 *
 */
@Component
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
@Sharable
@Slf4j
public class Netflow9DatagramDecoder extends ChannelInboundHandlerAdapter {

    private final PeerAddressMapper peerAddressMapper;

    private static final int PACKET_HEADER_LENGTH = 20;
    private static final int VERSION_NUMBER = 9;
    private static final int TEMPLATE_FLOWSET_ID = 0;
    private static final int OPTIONS_TEMPLATE_FLOWSET_ID = 1;

    /*
     * (non-Javadoc)
     *
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty. channel.ChannelHandlerContext)
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        log.info("netflow 9 collector server channel is active");

        ctx.fireChannelActive();
    }

    /*
     * (non-Javadoc)
     *
     * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty. channel.ChannelHandlerContext)
     */
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        log.info("netflow 9 collector server channel is inactive");

        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object o) throws Exception {
        log.info("recieved message with payload type {}", o.getClass().getName());

        if (o instanceof DatagramPacket) {
            final DatagramPacket msg = (DatagramPacket) o;
            final InetAddress peerAddress = peerAddressMapper.mapRemoteAddress(msg.sender());
            final ByteBuf in = msg.content();

            log.info("received flow packet from peer {}", peerAddress);

            if (in.readableBytes() > PACKET_HEADER_LENGTH) {
                final int versionNumber = in.readUnsignedShort();

                if (versionNumber == VERSION_NUMBER) {
                    final Header header = Header.builder()
                            .recordCount(in.readUnsignedShort())
                            .sysUpTime(in.readUnsignedInt())
                            .unixSeconds(in.readUnsignedInt())
                            .sequenceNumber(in.readUnsignedInt())
                            .sourceId(in.readUnsignedInt())
                            .build();

                    final Netflow9DecodedDatagram decodedDatagram = new Netflow9DecodedDatagram();

                    decodedDatagram.setPeerAddress(peerAddress);
                    decodedDatagram.setHeader(header);

                    processDatagramPacket(decodedDatagram, header, peerAddress, in);

                    ctx.fireChannelRead(decodedDatagram);
                    ctx.fireChannelReadComplete();
                }
            } else {
                log.error(
                        "dropping received packet with {} bytes size but expected at least {} bytes",
                        in.readableBytes(),
                        PACKET_HEADER_LENGTH);
            }
        }
    }

    private void processDatagramPacket(
            final Netflow9DecodedDatagram decodedDatagram,
            final Header header,
            final InetAddress peerAddress,
            final ByteBuf in) {
        int recordNumber = 0;

        while (in.readableBytes() > 4) {
            final int flowSetId = in.readUnsignedShort();
            final int length = in.readUnsignedShort();
            final int remainingOctets = length - 4; // subtract length of
            // flowset ID and flowset
            // length

            if (in.readableBytes() < remainingOctets) {
                log.error(
                        "packet short to {} bytes when {} bytes required in record {}",
                        in.readableBytes(),
                        remainingOctets,
                        recordNumber);

                return;
            }

            final ByteBuf workBuf = in.readSlice(remainingOctets);

            switch (flowSetId) {
            case TEMPLATE_FLOWSET_ID:
                processTemplateFlowset(decodedDatagram, peerAddress, recordNumber, workBuf);
                recordNumber++;
                break;
            case OPTIONS_TEMPLATE_FLOWSET_ID:
                recordNumber++;
                processOptionsTemplateFlowset(decodedDatagram, peerAddress, workBuf);
                break;
            default:
                log.info("received flowset with ID {} from peer", flowSetId, peerAddress);

                decodedDatagram.getFlows()
                        .add(FlowBuffer.builder().header(header).flowSetId(flowSetId).buffer(workBuf).build());
                break;
            }
        }
    }

    private void processTemplateFlowset(
            final Netflow9DecodedDatagram decodedDatagram,
            final InetAddress peerAddress,
            final int recordNumber,
            final ByteBuf workBuf) {
        log.info("received data template flowset from peer", peerAddress);

        while (workBuf.readableBytes() > 4) {
            final int flowSetID = workBuf.readUnsignedShort();
            final int fieldCount = workBuf.readUnsignedShort();
            final List<DataTemplateField> fields = new LinkedList<>();

            if (workBuf.readableBytes() < fieldCount * 4) {
                log.error(
                        "packet short to {} bytes when {} bytes required in record {}",
                        workBuf.readableBytes(),
                        fieldCount * 4,
                        recordNumber);

                return;
            }

            for (int fieldNumber = 0; fieldNumber < fieldCount; fieldNumber++) {
                fields.add(
                        DataTemplateField.builder()
                                .type(FieldType.fromCode(workBuf.readUnsignedShort()))
                                .length(workBuf.readUnsignedShort())
                                .build());
            }

            decodedDatagram.getTemplates().add(DataTemplate.builder().flowsetId(flowSetID).fields(fields).build());
        }
    }

    private void processOptionsTemplateFlowset(
            final Netflow9DecodedDatagram decodedDatagram,
            final InetAddress peerAddress,
            final ByteBuf workBuf) {
        log.info("received options template flowset from peer", peerAddress);

        {
            final int flowSetID = workBuf.readUnsignedShort();
            int scopeLength = workBuf.readUnsignedShort();
            int optionsLength = workBuf.readUnsignedShort();
            final List<ScopeField> scopeFields = new LinkedList<>();
            final List<OptionField> optionFields = new LinkedList<>();

            while (scopeLength >= 4) {
                scopeFields.add(
                        ScopeField.builder()
                                .type(ScopeType.fromCode(workBuf.readUnsignedShort()))
                                .length(workBuf.readUnsignedShort())
                                .build());
                scopeLength -= 4;
            }

            while (optionsLength >= 4) {
                optionFields.add(
                        OptionField.builder()
                                .type(FieldType.fromCode(workBuf.readUnsignedShort()))
                                .length(workBuf.readUnsignedShort())
                                .build());
                optionsLength -= 4;
            }

            decodedDatagram.getOptionsTemplates().add(
                    OptionsTemplate.builder().flowsetId(flowSetID).optionFields(optionFields).scopeFields(scopeFields).build());

        }
    }
}
