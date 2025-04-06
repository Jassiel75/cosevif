package utez.edu.mx.cosevif.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import utez.edu.mx.cosevif.model.WorkerVisit;

import java.util.List;

@Repository
public interface WorkerVisitRepository extends MongoRepository<WorkerVisit, String> {
    List<WorkerVisit> findByResidentId(String residentId);

    List<WorkerVisit> findAllByResidentId(String residentId);

}

