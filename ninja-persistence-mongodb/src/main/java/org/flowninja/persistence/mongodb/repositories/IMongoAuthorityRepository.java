/**
 * 
 */
package org.flowninja.persistence.mongodb.repositories;

import org.flowninja.persistence.mongodb.data.MongoAuthorityRecord;
import org.flowninja.types.generic.AuthorityKey;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author rainer
 *
 */
public interface IMongoAuthorityRepository extends CrudRepository<MongoAuthorityRecord, AuthorityKey>, QueryDslPredicateExecutor<MongoAuthorityRecord> {

}
