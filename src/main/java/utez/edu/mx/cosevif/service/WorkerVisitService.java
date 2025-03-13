package utez.edu.mx.cosevif.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utez.edu.mx.cosevif.model.WorkerVisit;
import utez.edu.mx.cosevif.repository.WorkerVisitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WorkerVisitService {

    private final WorkerVisitRepository workerVisitRepository;

    public WorkerVisitService(WorkerVisitRepository workerVisitRepository){
        this.workerVisitRepository = workerVisitRepository;
    }

    public ResponseEntity<?> registerWorkerVisit(WorkerVisit workerVisit) {
        workerVisit.setDateTime(LocalDateTime.now());
        WorkerVisit savedVisit = workerVisitRepository.save(workerVisit);
        return ResponseEntity.ok(savedVisit);
    }

    public List<WorkerVisit> getWorkerVisitsByResident(String residentId) {
        return workerVisitRepository.findByResidentId(residentId);
    }

    public Optional<WorkerVisit> getWorkerVisitById(String id) {
        return workerVisitRepository.findById(id);
    }
}
