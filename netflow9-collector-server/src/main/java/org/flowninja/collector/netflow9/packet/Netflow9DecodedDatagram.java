package org.flowninja.collector.netflow9.packet;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.flowninja.collector.common.netflow9.types.Header;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.common.netflow9.types.Template;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Netflow9DecodedDatagram {
	private InetAddress peerAddress;
	private Header header;
	private List<Template> templates = new LinkedList<>();
	private List<OptionsTemplate> optionsTemplates = new LinkedList<>();
	private List<FlowBuffer> flows = new LinkedList<>();

}
