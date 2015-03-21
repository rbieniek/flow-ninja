/**
 * 
 */
package org.flowninja.collector.netflow9.packet;

import java.net.InetAddress;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerListener;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.listeners.TriggerListenerSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a mapping between peer network addresses 
 * and per-peer flow registries.
 * 
 * @author rainer
 *
 */
public class PeerRegistry {
	private static final Logger logger = LoggerFactory.getLogger(PeerRegistry.class);
	
	/**
	 * Container modeling a single peer
	 * 
	 * @author rainer
	 *
	 */
	private static class PeerEntry {
		private PeerEntry() {
			this.flowRegistry = new FlowRegistry();
			this.lastUsedStamp = LocalDateTime.now();
		}
		
		private FlowRegistry flowRegistry;
		private LocalDateTime lastUsedStamp;
		
		/**
		 * @return the flowRegistry
		 */
		public FlowRegistry getFlowRegistry() {
			this.lastUsedStamp = LocalDateTime.now();
			
			return flowRegistry;
		}
		
		/**
		 * @return the lastUsedStamp
		 */
		public LocalDateTime getLastUsedStamp() {
			return lastUsedStamp;
		}
	}

	public static class PeerExpiryJob implements Job {

		@Override
		public void execute(JobExecutionContext context) throws JobExecutionException {
		}
		
	}
	
	private class PeerExpiryListener extends TriggerListenerSupport {

		@Override
		public String getName() {
			return "peer-expiry-listener";
		}

		/* (non-Javadoc)
		 * @see org.quartz.listeners.TriggerListenerSupport#triggerFired(org.quartz.Trigger, org.quartz.JobExecutionContext)
		 */
		@Override
		public void triggerFired(Trigger trigger, JobExecutionContext context) {
			LocalDateTime expiryDate = LocalDateTime.now().minus(peerTtl, ChronoUnit.SECONDS);

			logger.info("peer expiry trigger fired, checking for peer expired before {}", expiryDate);
			
			synchronized (PeerRegistry.this) {
				List<InetAddress> removeable = new LinkedList<InetAddress>();

				for(Entry<InetAddress, PeerEntry> entry : peers.entrySet()) {
					if(entry.getValue().getLastUsedStamp().isBefore(expiryDate))
						removeable.add(entry.getKey());
				}
				
				for(InetAddress key : removeable) {
					logger.info("Remove peer {}", key);
					
					peers.remove(key);
				}
			}
			
		}

		
	}
	
	private int peerTtl;
	private Map<InetAddress, PeerEntry> peers = new HashMap<InetAddress, PeerRegistry.PeerEntry>();
	private Scheduler scheduler;

	/**
	 * @param peerTtl the peerTtl to set
	 */
	public void setPeerTtl(int peerTtl) {
		this.peerTtl = peerTtl;
	}

	/**
	 * 
	 */
	public void init() throws Exception {
		logger.info("Starting peer registry");
		
		Trigger jobTrigger = TriggerBuilder.newTrigger()
				.withIdentity("expiryTrigger")
				.startNow()
				.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever())
				.build();
		
		JobDetail jobDetail = JobBuilder.newJob(PeerExpiryJob.class).withIdentity("peerExpiryJob").build();
		
		try {
			scheduler = (new StdSchedulerFactory()).getScheduler();
			
			scheduler.start();
			scheduler.scheduleJob(jobDetail, jobTrigger);
			scheduler.getListenerManager().addTriggerListener(new PeerExpiryListener());
			
		} catch(SchedulerException e) {
			logger.error("failed to start scheduler", e);
			
			throw e;
		}
	}
	
	/**
	 * 
	 */
	public void destroy() throws Exception{
		logger.info("Stopping peer registry");		

		try {
			scheduler.shutdown();
		} catch(SchedulerException e) {
			logger.error("failed to start scheduler", e);
			
			throw e;
		}
	}
	
	public synchronized FlowRegistry registryForPeerAddress(InetAddress peerAddress) {
		logger.info("obtaing flow registry for peer address {}", peerAddress);
		
		PeerEntry peer = peers.get(peerAddress);
		
		if(peer == null) {
			peer = new PeerEntry();
			
			peers.put(peerAddress, peer);
		}
 		
		return peer.getFlowRegistry();
	}
	
}
