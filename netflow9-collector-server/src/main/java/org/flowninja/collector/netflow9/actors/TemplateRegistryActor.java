package org.flowninja.collector.netflow9.actors;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.flowninja.collector.common.netflow9.types.DataTemplate;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.netflow9.components.TemplateRegistry;
import org.flowninja.common.akka.ActorUtils;
import org.flowninja.common.akka.AkkaComponent;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

@AkkaComponent
@RequiredArgsConstructor(onConstructor = @__({ @Autowired }))
public class TemplateRegistryActor extends UntypedActor {

    private final TemplateRegistry<DataTemplate> dataTemplateRegistry;
    private final TemplateRegistry<OptionsTemplate> optionsTemplateRegistry;

    private List<ActorRef> notifiedActors = new LinkedList<>();

    @Override
    public void onReceive(final Object message) throws Throwable {
        final ActorRef sender = getSender();

        ActorUtils.withMessage(message).onType(StoreDataTemplateRequest.class, m -> {
            dataTemplateRegistry.addTemplates(m.getDataTemplates());

            m.getDataTemplates().forEach(dt -> {
                notifiedActors.forEach(a -> a.tell(DataTemplateAvailableRequest.builder().dataTemplate(dt).build(), getSelf()));
            });
        }).onType(StoreOptionTemplateRequest.class, m -> {
            optionsTemplateRegistry.addTemplates(m.getOptionsTemplates());

            m.getOptionsTemplates().forEach(ot -> {
                notifiedActors
                        .forEach(a -> a.tell(OptionsTemplateAvailableRequest.builder().optionsTemplate(ot).build(), getSelf()));
            });
        }).onType(AddNotifiedRequest.class, m -> {
            notifiedActors.add(m.getNotified());
        }).onType(LookupTemplateRequest.class, m -> {
            final DataTemplate dataTemplate = dataTemplateRegistry.templateForFlowsetID(m.getFlowsetId());
            final OptionsTemplate optionsTemplate = optionsTemplateRegistry.templateForFlowsetID(m.getFlowsetId());

            if (dataTemplate != null) {
                sender.tell(LookupDataTemplateResponse.builder().dataTemplate(dataTemplate).build(), getSelf());
            } else if (optionsTemplate != null) {
                sender.tell(LookupOptionsTemplateResponse.builder().optionsTemplate(optionsTemplate).build(), getSelf());
            } else {
                sender.tell(new UnknownFlowsetResponse(), getSelf());
            }
        }).unhandled(m -> unhandled(m));
    }

    @Builder
    @Getter
    public static class StoreDataTemplateRequest {

        private List<DataTemplate> dataTemplates;
    }

    @Builder
    @Getter
    public static class StoreOptionTemplateRequest {

        private List<OptionsTemplate> optionsTemplates;
    }

    @Builder
    @Getter
    public static class AddNotifiedRequest {

        private ActorRef notified;
    }

    @Builder
    @Getter
    public static class LookupTemplateRequest {

        private int flowsetId;
    }

    @Builder
    @Getter
    public static class LookupDataTemplateResponse {

        private DataTemplate dataTemplate;
    }

    @Builder
    @Getter
    public static class LookupOptionsTemplateResponse {

        private OptionsTemplate optionsTemplate;
    }

    public static class UnknownFlowsetResponse {

    }

    @Builder
    @Getter
    public static class DataTemplateAvailableRequest {

        private DataTemplate dataTemplate;
    }

    @Builder
    @Getter
    public static class OptionsTemplateAvailableRequest {

        private OptionsTemplate optionsTemplate;
    }

}
