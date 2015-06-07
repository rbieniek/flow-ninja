/**
 * 
 */
package org.flowninja.webapp.collector.generic.components;

import org.apache.commons.lang3.StringUtils;
import org.flowninja.types.flows.IPv4Flow;
import org.kitesdk.data.Formats;
import org.kitesdk.data.PartitionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.hadoop.store.DataStoreWriter;
import org.springframework.data.hadoop.store.dataset.AvroPojoDatasetStoreWriter;
import org.springframework.data.hadoop.store.dataset.DatasetDefinition;
import org.springframework.data.hadoop.store.dataset.DatasetRepositoryFactory;

/**
 * @author rainer
 *
 */
@Configuration
public class DatasetConfiguration {

	@Autowired
	private Environment environment;
	
	@Autowired
	private org.apache.hadoop.conf.Configuration hadoopConfiguration;
	
	@Bean
	public DatasetRepositoryFactory datasetRepositoryFactory() {
		DatasetRepositoryFactory datasetRepositoryFactory = new DatasetRepositoryFactory();
	    String fsPath = environment.getProperty("flowninja.fs.path");

	    if(StringUtils.isBlank(fsPath))
	    	throw new IllegalArgumentException("Property flowninja.fs.path must be set");
	    
	    datasetRepositoryFactory.setConf(hadoopConfiguration);
	    datasetRepositoryFactory.setBasePath(fsPath);
	    datasetRepositoryFactory.setNamespace("flowninja");
	    
	    return datasetRepositoryFactory;
	}	

	@Bean
	public DatasetDefinition ipv4FlowDatasetDefinition() {
	    DatasetDefinition definition = new DatasetDefinition();
	    
	    definition.setFormat(Formats.AVRO.getName());
	    definition.setTargetClass(IPv4Flow.class);
	    definition.setAllowNullValues(false);
	    definition.setPartitionStrategy((new PartitionStrategy.Builder())
	    		.identity("accountingGroupUuid", "accountingGroup")
	    		.identity("collectorUuid", "collector")
	    		.provided("ipv4")
	    		.year("header.stamp")
	    		.month("header.stamp")
	    		.day("header.stamp")
	    		.hour("header.stamp")
	    		.build());
	    
	    return definition;
	  }
	
	@Bean
	public DataStoreWriter<IPv4Flow> ipv4FlowDataStoreWriter() {
	    return new AvroPojoDatasetStoreWriter<IPv4Flow>(IPv4Flow.class, datasetRepositoryFactory(), ipv4FlowDatasetDefinition());
	  }
}
