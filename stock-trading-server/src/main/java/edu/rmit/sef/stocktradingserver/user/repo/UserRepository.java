package edu.rmit.sef.stocktradingserver.user.repo;

import edu.rmit.sef.stocktradingserver.user.model.SystemUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<SystemUser, String> {
    public SystemUser findUserByUsername(String username);
}
