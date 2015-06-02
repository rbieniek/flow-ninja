/**
 * 
 */
package org.flowninja.persistence.mongodb.repositories;

import org.bson.types.ObjectId;
import org.flowninja.persistence.mongodb.data.MongoOAuth2RefreshToken;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author rainer
 *
 */
public interface MongoOAuth2RefreshTokenRepository extends CrudRepository<MongoOAuth2RefreshToken, ObjectId>, QueryDslPredicateExecutor<MongoOAuth2RefreshToken>{

}
