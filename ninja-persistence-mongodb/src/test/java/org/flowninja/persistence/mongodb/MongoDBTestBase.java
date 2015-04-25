/**
 * 
 */
package org.flowninja.persistence.mongodb;

import java.net.UnknownHostException;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;

/**
 * @author rainer
 *
 */
public class MongoDBTestBase {

	private static final MongodStarter starter = MongodStarter.getDefaultInstance();
	
	private MongodExecutable mongodExecutable;
	private MongodProcess mongodProcess;
	
	@Before
	public void setupMongod() throws Exception {
		this.mongodExecutable = starter.prepare((new MongodConfigBuilder())
				.version(Version.Main.PRODUCTION)
				.build());
		this.mongodProcess = this.mongodExecutable.start();
	}
	
	@After
	public void teardownMongod() {
		if(mongodExecutable != null) {
			if(this.mongodProcess != null) {
				this.mongodProcess.stop();
				this.mongodProcess = null;
			}

			this.mongodExecutable.stop();
			this.mongodExecutable = null;
		}
	}

	/**
	 * Creates a new Mongo connection.
	 * 
	 * @throws MongoException
	 * @throws UnknownHostException
	 */
	public MongoClient newMongo() throws UnknownHostException, MongoException {
		return new MongoClient(new ServerAddress(mongodProcess.getConfig().net().getServerAddress(),
				mongodProcess.getConfig().net().getPort()));
	}
	
	/**
	 * Creates a new DB with unique name for connection.
	 */
	public DB newDB(Mongo mongo) {
		return mongo.getDB(UUID.randomUUID().toString());
	}

}
