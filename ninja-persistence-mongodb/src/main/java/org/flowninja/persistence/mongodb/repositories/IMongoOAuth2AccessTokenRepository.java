/**
 * 
 */
package org.flowninja.persistence.mongodb.repositories;

import org.bson.types.ObjectId;
import org.flowninja.persistence.mongodb.data.MongoOAuth2AccessToken;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author rainer
 *
 */
public interface IMongoOAuth2AccessTokenRepository extends CrudRepository<MongoOAuth2AccessToken, ObjectId>, QueryDslPredicateExecutor<MongoOAuth2AccessToken>{

}
