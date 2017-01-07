package org.flowninja.collector.common.netflow9.actors;

import java.net.InetAddress;

import org.flowninja.collector.common.netflow9.types.Header;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.common.netflow9.types.DataTemplate;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TemplateDecodingFailureMessage {
	private DataTemplate dataTemplate;
	private OptionsTemplate optionsTemplate;
	private Header header;
	private InetAddress peerAddress;
	private int flowSetId;
	private byte[] payload;
	private final Throwable reason;
}
