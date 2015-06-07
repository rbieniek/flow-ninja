/**
 * 
 */
package org.flowninja.webapp.collector.generic.restcontrollers;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicLong;

import org.flowninja.persistence.generic.types.CollectorRecord;
import org.flowninja.security.oauth2.annotations.OAuth2LoggedIn;
import org.flowninja.types.flows.IPFlowCollection;
import org.flowninja.types.flows.IPv4Flow;
import org.kitesdk.data.Dataset;
import org.kitesdk.data.RefinableView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.store.DataStoreWriter;
import org.springframework.data.hadoop.store.dataset.DatasetOperations;
import org.springframework.data.hadoop.store.dataset.RecordCallback;
import org.springframework.data.hadoop.store.dataset.ViewCallback;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rainer
 *
 */
@RestController
@OAuth2LoggedIn
public class DataFlowRestContoller {
	private static final Logger logger = LoggerFactory.getLogger(DataFlowRestContoller.class);
	
	@Autowired
	private DataStoreWriter<IPv4Flow> ipFlowWriter;
	
	@Autowired
	private DatasetOperations datasetOperations;
	
	@RequestMapping(consumes="application/json", value="/rest/data-flows/ipv4", method=RequestMethod.PUT)
	public ResponseEntity<Object> storeIPFlow(HttpEntity<IPFlowCollection> requestEntity, 
			@ModelAttribute CollectorRecord collector) throws UnsupportedEncodingException {

		logger.info("Putting IP data flow collection for collector {}", collector);
		
		if(collector == null)
			return new ResponseEntity<Object>(HttpStatus.FORBIDDEN);
		
		try {
			for(IPv4Flow flow : requestEntity.getBody().getFlows()) {
				flow.setAccountingGroupUuid(collector.getAccountingGroup().getKey().toString());
				flow.setCollectorUuid(collector.getKey().toString());
				
				ipFlowWriter.write(flow);
			}
			
			ipFlowWriter.close();
		} catch(IOException e) {
			logger.error("failed to write IPv4 flows", e);
			
			return new ResponseEntity<Object>(HttpStatus.BAD_GATEWAY);
		}
		
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@RequestMapping(produces="application/json", value="/rest/data-flows/ipv4", method=RequestMethod.GET)
	public ResponseEntity<IPv4FlowInfoModel> getIpFlowInfo(@ModelAttribute CollectorRecord collector) {
		logger.info("Retrieving IP data flow collection summary for collector {}", collector);
		
		if(collector == null)
			return new ResponseEntity<IPv4FlowInfoModel>(HttpStatus.FORBIDDEN);
		
		final AtomicLong count = new AtomicLong();
		
		try {
			datasetOperations.read(IPv4Flow.class, new RecordCallback<IPv4Flow>() {
	
				@Override
				public void doInRecord(IPv4Flow record) {
					count.getAndIncrement();
				}
			}, new ViewCallback() {
	
				@Override
				public <T> RefinableView<T> doInView(Dataset<T> dataset, Class<T> targetClass) {
					return dataset.with("accountingGroup", collector.getAccountingGroup().getKey().toString())
							.with("collector", collector.getKey().toString());
				}	
			});
		} catch(Exception e) {
			logger.error("failed to read IPv4 flows", e);
			
			return new ResponseEntity<IPv4FlowInfoModel>(HttpStatus.BAD_GATEWAY);			
		}		
		
		return new ResponseEntity<IPv4FlowInfoModel>(new IPv4FlowInfoModel(count.get()), HttpStatus.OK);
	}
}
