/**
 * 
 */
package org.flowninja.persistence.mongodb.repositories;

import org.flowninja.persistence.mongodb.data.MongoBlockedNetworkRecord;
import org.flowninja.types.generic.BlockedNetworkKey;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author rainer
 *
 */
public interface IMongoBlockedNetworkRepository extends CrudRepository<MongoBlockedNetworkRecord, BlockedNetworkKey>, QueryDslPredicateExecutor<MongoBlockedNetworkRecord> {

}
