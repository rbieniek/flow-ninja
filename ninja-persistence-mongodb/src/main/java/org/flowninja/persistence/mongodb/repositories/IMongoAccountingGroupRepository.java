/**
 * 
 */
package org.flowninja.persistence.mongodb.repositories;

import org.flowninja.persistence.mongodb.data.MongoAccountingGroupRecord;
import org.flowninja.types.generic.AccountingGroupKey;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author rainer
 *
 */
public interface IMongoAccountingGroupRepository extends CrudRepository<MongoAccountingGroupRecord, AccountingGroupKey>, QueryDslPredicateExecutor<MongoAccountingGroupRecord> {

}
