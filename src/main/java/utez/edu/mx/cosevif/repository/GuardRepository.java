package utez.edu.mx.cosevif.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.cosevif.model.Guard;

import java.util.Optional;

@Repository
public interface GuardRepository  extends MongoRepository<Guard, String> {
    Optional<Guard> findByUsername(String username);

}
