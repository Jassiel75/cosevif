package utez.edu.mx.cosevif.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import utez.edu.mx.cosevif.model.WorkerVisit;

import java.util.List;

public interface WorkerVisitRepository extends MongoRepository<WorkerVisit, String> {
    List<WorkerVisit> findByResidentId(String residentId);
}
