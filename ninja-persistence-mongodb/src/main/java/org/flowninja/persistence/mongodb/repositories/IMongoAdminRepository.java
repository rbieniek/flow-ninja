/**
 * 
 */
package org.flowninja.persistence.mongodb.repositories;

import org.flowninja.persistence.generic.types.AdminKey;
import org.flowninja.persistence.mongodb.data.MongoAdminRecord;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author rainer
 *
 */
public interface IMongoAdminRepository extends CrudRepository<MongoAdminRecord, AdminKey>, QueryDslPredicateExecutor<MongoAdminRecord> {

}
