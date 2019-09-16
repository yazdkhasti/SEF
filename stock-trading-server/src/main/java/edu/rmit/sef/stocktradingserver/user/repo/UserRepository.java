package edu.rmit.sef.stocktradingserver.user.repo;

import edu.rmit.sef.user.model.SystemUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<SystemUser, String> {
    SystemUser findUserByUsername(String username);
}
