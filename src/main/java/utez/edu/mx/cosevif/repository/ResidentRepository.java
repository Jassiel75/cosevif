package utez.edu.mx.cosevif.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import utez.edu.mx.cosevif.model.Resident;

import java.util.Optional;

public interface ResidentRepository extends MongoRepository<Resident, String> {
    Optional<Resident> findByEmail(String email);

    Optional<Resident> findByPhone(String phone);

    boolean existsByHouseId(String houseId);
}
