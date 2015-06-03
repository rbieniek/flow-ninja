/**
 * 
 */
package org.flowninja.persistence.mongodb.repositories;

import org.flowninja.persistence.mongodb.data.MongoCollectorRecord;
import org.flowninja.types.generic.CollectorKey;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author rainer
 *
 */
public interface IMongoCollectorRepository extends CrudRepository<MongoCollectorRecord, CollectorKey>, QueryDslPredicateExecutor<MongoCollectorRecord> {

}
