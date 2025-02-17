package utez.edu.mx.cosevif.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import utez.edu.mx.cosevif.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
}
