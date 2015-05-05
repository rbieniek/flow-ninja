/**
 * 
 */
package org.flowninja.persistence.mongodb.repositories;

import org.flowninja.persistence.mongodb.data.MongoAdminRecord;
import org.flowninja.types.generic.AdminKey;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;

/**
 * @author rainer
 *
 */
public interface IMongoAdminRepository extends CrudRepository<MongoAdminRecord, AdminKey>, QueryDslPredicateExecutor<MongoAdminRecord> {

}
