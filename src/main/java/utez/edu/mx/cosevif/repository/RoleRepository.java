package utez.edu.mx.cosevif.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import utez.edu.mx.cosevif.model.Role;

import java.util.Optional;


public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(String name);
}
