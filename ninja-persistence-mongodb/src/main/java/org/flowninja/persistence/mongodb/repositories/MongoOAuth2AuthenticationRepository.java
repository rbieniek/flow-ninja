/**
 * 
 */
package org.flowninja.persistence.mongodb.repositories;

import org.bson.types.ObjectId;
import org.flowninja.persistence.mongodb.data.MongoOAuth2Authentication;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author rainer
 *
 */
public interface MongoOAuth2AuthenticationRepository extends CrudRepository<MongoOAuth2Authentication, ObjectId>, QueryDslPredicateExecutor<MongoOAuth2Authentication>{

}
