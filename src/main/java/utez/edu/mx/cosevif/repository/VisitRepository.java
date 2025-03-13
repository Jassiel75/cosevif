package utez.edu.mx.cosevif.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import utez.edu.mx.cosevif.model.Visit;

import java.util.List;

public interface VisitRepository extends MongoRepository<Visit, String> {
    List<Visit> findByResidentId(String residentId);
}
