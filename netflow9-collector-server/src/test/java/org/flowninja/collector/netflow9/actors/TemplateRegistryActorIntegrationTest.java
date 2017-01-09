package org.flowninja.collector.netflow9.actors;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit4.SpringRunner;

import org.flowninja.collector.common.netflow9.types.DataTemplate;
import org.flowninja.collector.common.netflow9.types.OptionsTemplate;
import org.flowninja.collector.netflow9.actors.support.ActorsTestConfiguration;
import org.flowninja.collector.netflow9.actors.support.MessageSinkActor;
import org.flowninja.collector.netflow9.components.TemplateRegistry;
import org.flowninja.common.TestConfig;
import org.flowninja.common.akka.AkkaConfiguration;
import org.flowninja.common.akka.SpringActorProducer;

import static org.assertj.core.api.Assertions.assertThat;

import static java.util.concurrent.TimeUnit.SECONDS;

import akka.actor.ActorRef;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = TemplateRegistryActorIntegrationTest.TestConfiguration.class)
public class TemplateRegistryActorIntegrationTest {

    @Rule
    public Timeout globalTimeout = new Timeout(10, SECONDS);

    @Autowired
    private SpringActorProducer springActorProducer;

    private ActorRef templateRegistryActor;

    @Before
    public void createActors() {
        templateRegistryActor = springActorProducer.createActor(TemplateRegistryActor.class);
    }

    @Test
    public void shouldFireTemplateListenerOnDataTemplate() throws Exception {
        final CompletableFuture<List<TemplateRegistryActor.DataTemplateAvailableRequest>> notifiedCompletable = new CompletableFuture<>();
        final ActorRef notified = springActorProducer.createActor(
                MessageSinkActor.class,
                TemplateRegistryActor.DataTemplateAvailableRequest.class,
                1,
                notifiedCompletable);

        templateRegistryActor.tell(TemplateRegistryActor.AddNotifiedRequest.builder().notified(notified).build(), null);
        templateRegistryActor.tell(
                TemplateRegistryActor.StoreDataTemplateRequest.builder()
                        .dataTemplates(Arrays.asList(DataTemplate.builder().flowsetId(1).build()))
                        .build(),
                null);

        notifiedCompletable.whenComplete((l, t) -> {
            assertThat(l).hasSize(1);
            assertThat(l.get(0).getDataTemplate().getFlowsetId()).isEqualTo(1);
        }).get();
    }

    @Test
    public void shouldFireTemplateListenerOnTwoDataTemplate() throws Exception {
        final CompletableFuture<List<TemplateRegistryActor.DataTemplateAvailableRequest>> notifiedCompletable = new CompletableFuture<>();
        final ActorRef notified = springActorProducer.createActor(
                MessageSinkActor.class,
                TemplateRegistryActor.DataTemplateAvailableRequest.class,
                2,
                notifiedCompletable);

        templateRegistryActor.tell(TemplateRegistryActor.AddNotifiedRequest.builder().notified(notified).build(), null);
        templateRegistryActor.tell(
                TemplateRegistryActor.StoreDataTemplateRequest.builder()
                        .dataTemplates(
                                Arrays.asList(
                                        DataTemplate.builder().flowsetId(1).build(),
                                        DataTemplate.builder().flowsetId(2).build()))
                        .build(),
                null);

        notifiedCompletable.whenComplete((l, t) -> {
            assertThat(l).hasSize(2);
        }).get();
    }

    @Test
    public void shouldFireTemplateListenerOnOptionsTemplate() throws Exception {
        final CompletableFuture<List<TemplateRegistryActor.OptionsTemplateAvailableRequest>> notifiedCompletable = new CompletableFuture<>();
        final ActorRef notified = springActorProducer.createActor(
                MessageSinkActor.class,
                TemplateRegistryActor.OptionsTemplateAvailableRequest.class,
                1,
                notifiedCompletable);

        templateRegistryActor.tell(TemplateRegistryActor.AddNotifiedRequest.builder().notified(notified).build(), null);
        templateRegistryActor.tell(
                TemplateRegistryActor.StoreOptionTemplateRequest.builder()
                        .optionsTemplates(Arrays.asList(OptionsTemplate.builder().flowsetId(1).build()))
                        .build(),
                null);

        notifiedCompletable.whenComplete((l, t) -> {
            assertThat(l).hasSize(1);
            assertThat(l.get(0).getOptionsTemplate().getFlowsetId()).isEqualTo(1);
        }).get();
    }

    @Test
    public void shouldFireTemplateListenerOnTwoOptionsTemplate() throws Exception {
        final CompletableFuture<List<TemplateRegistryActor.OptionsTemplateAvailableRequest>> notifiedCompletable = new CompletableFuture<>();
        final ActorRef notified = springActorProducer.createActor(
                MessageSinkActor.class,
                TemplateRegistryActor.OptionsTemplateAvailableRequest.class,
                2,
                notifiedCompletable);

        templateRegistryActor.tell(TemplateRegistryActor.AddNotifiedRequest.builder().notified(notified).build(), null);
        templateRegistryActor.tell(
                TemplateRegistryActor.StoreOptionTemplateRequest.builder()
                        .optionsTemplates(
                                Arrays.asList(
                                        OptionsTemplate.builder().flowsetId(1).build(),
                                        OptionsTemplate.builder().flowsetId(2).build()))
                        .build(),
                null);

        notifiedCompletable.whenComplete((l, t) -> {
            assertThat(l).hasSize(2);
        }).get();
    }

    @Test
    public void shouldFindKnownDataTemplate() {
        final CompletableFuture<List<TemplateRegistryActor.LookupDataTemplateResponse>> replyCompletable = new CompletableFuture<>();
        final ActorRef replyActor = springActorProducer.createActor(
                MessageSinkActor.class,
                TemplateRegistryActor.LookupDataTemplateResponse.class,
                1,
                replyCompletable);

        templateRegistryActor.tell(
                TemplateRegistryActor.StoreDataTemplateRequest.builder()
                        .dataTemplates(
                                Arrays.asList(
                                        DataTemplate.builder().flowsetId(1).build(),
                                        DataTemplate.builder().flowsetId(2).build(),
                                        DataTemplate.builder().flowsetId(3).build()))
                        .build(),
                null);

        templateRegistryActor.tell(TemplateRegistryActor.LookupTemplateRequest.builder().flowsetId(1).build(), replyActor);

        replyCompletable.whenComplete((l, t) -> {
            assertThat(l).hasSize(1);
        });
    }

    @Test
    public void shouldNotFindUnknownDataTemplate() {
        final CompletableFuture<List<TemplateRegistryActor.UnknownFlowsetResponse>> replyCompletable = new CompletableFuture<>();
        final ActorRef replyActor = springActorProducer
                .createActor(MessageSinkActor.class, TemplateRegistryActor.UnknownFlowsetResponse.class, 1, replyCompletable);

        templateRegistryActor.tell(
                TemplateRegistryActor.StoreDataTemplateRequest.builder()
                        .dataTemplates(
                                Arrays.asList(
                                        DataTemplate.builder().flowsetId(1).build(),
                                        DataTemplate.builder().flowsetId(2).build(),
                                        DataTemplate.builder().flowsetId(3).build()))
                        .build(),
                null);

        templateRegistryActor.tell(TemplateRegistryActor.LookupTemplateRequest.builder().flowsetId(4).build(), replyActor);

        replyCompletable.whenComplete((l, t) -> {
            assertThat(l).hasSize(1);
        });
    }

    @Test
    public void shouldFindKnownOptionsTemplate() {
        final CompletableFuture<List<TemplateRegistryActor.LookupOptionsTemplateResponse>> replyCompletable = new CompletableFuture<>();
        final ActorRef replyActor = springActorProducer.createActor(
                MessageSinkActor.class,
                TemplateRegistryActor.LookupOptionsTemplateResponse.class,
                1,
                replyCompletable);

        templateRegistryActor.tell(
                TemplateRegistryActor.StoreOptionTemplateRequest.builder()
                        .optionsTemplates(
                                Arrays.asList(
                                        OptionsTemplate.builder().flowsetId(1).build(),
                                        OptionsTemplate.builder().flowsetId(2).build(),
                                        OptionsTemplate.builder().flowsetId(3).build()))
                        .build(),
                null);

        templateRegistryActor.tell(TemplateRegistryActor.LookupTemplateRequest.builder().flowsetId(1).build(), replyActor);

        replyCompletable.whenComplete((l, t) -> {
            assertThat(l).hasSize(1);
        });
    }

    @Test
    public void shouldNotFindUnknownOptionsTemplate() {
        final CompletableFuture<List<TemplateRegistryActor.LookupOptionsTemplateResponse>> replyCompletable = new CompletableFuture<>();
        final ActorRef replyActor = springActorProducer.createActor(
                MessageSinkActor.class,
                TemplateRegistryActor.LookupOptionsTemplateResponse.class,
                1,
                replyCompletable);

        templateRegistryActor.tell(
                TemplateRegistryActor.StoreOptionTemplateRequest.builder()
                        .optionsTemplates(
                                Arrays.asList(
                                        OptionsTemplate.builder().flowsetId(1).build(),
                                        OptionsTemplate.builder().flowsetId(2).build(),
                                        OptionsTemplate.builder().flowsetId(3).build()))
                        .build(),
                null);

        templateRegistryActor.tell(TemplateRegistryActor.LookupTemplateRequest.builder().flowsetId(4).build(), replyActor);

        replyCompletable.whenComplete((l, t) -> {
            assertThat(l).hasSize(1);
        });
    }

    @TestConfig
    @Import({ AkkaConfiguration.class, ActorsTestConfiguration.class })
    @ComponentScan(basePackageClasses = { TemplateDecoderActor.class })
    public static class TestConfiguration {

        @Bean
        @Scope("prototype")
        public TemplateRegistry<DataTemplate> dataTemplateRegistry() {
            return new TemplateRegistry<>();
        }

        @Bean
        @Scope("prototype")
        public TemplateRegistry<OptionsTemplate> optionsTemplateRegistry() {
            return new TemplateRegistry<>();
        }
    }
}
