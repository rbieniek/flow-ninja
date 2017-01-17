package org.flowninja.collectord.actors;

import org.springframework.beans.factory.annotation.Autowired;

import org.flowninja.collector.common.netflow9.actors.DataFlowMessage;
import org.flowninja.collector.common.netflow9.actors.OptionsFlowMessage;
import org.flowninja.collectord.components.FlowFileWriter;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import akka.actor.UntypedActor;

@AkkaComponent
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
@Slf4j
public class FlowFileWriterActor extends UntypedActor {

    private final FlowFileWriter flowFileWriter;

    @Override
    @SuppressWarnings("checkstyle:IllegalCatch")
    public void onReceive(final Object message) throws Throwable {
        ActorUtils.withMessage(message).onType(DataFlowMessage.class, m -> {
            try {
                flowFileWriter.writeDataFlow(m.getDataFlow());
            } catch (RuntimeException e) {
                log.info("Failed to write data flow {} to disk", m.getDataFlow(), e);
            }
        }).onType(OptionsFlowMessage.class, m -> {
            try {
                flowFileWriter.writeOptionsFlow(m.getOptionsFlow());
            } catch (RuntimeException e) {
                log.info("Failed to write options flow {} to disk", m.getOptionsFlow(), e);
            }
        }).unhandled(m -> unhandled(m));

    }

}
