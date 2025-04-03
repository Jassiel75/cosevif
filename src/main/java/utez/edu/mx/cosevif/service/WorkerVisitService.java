package utez.edu.mx.cosevif.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utez.edu.mx.cosevif.model.Resident;
import utez.edu.mx.cosevif.model.WorkerVisit;
import utez.edu.mx.cosevif.repository.ResidentRepository;
import utez.edu.mx.cosevif.repository.WorkerVisitRepository;
import utez.edu.mx.cosevif.security.JwtTokenProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class WorkerVisitService {

    private final WorkerVisitRepository workerVisitRepository;
    private final ResidentRepository residentRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public WorkerVisitService(WorkerVisitRepository workerVisitRepository,
                              ResidentRepository residentRepository,
                              JwtTokenProvider jwtTokenProvider) {
        this.workerVisitRepository = workerVisitRepository;
        this.residentRepository = residentRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public ResponseEntity<?> registerWorkerVisit(String token, WorkerVisit workerVisit) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        Optional<Resident> residentOptional = residentRepository.findByEmail(email);

        if (residentOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Residente no encontrado.");
        }

        Resident resident = residentOptional.get();

        workerVisit.setResidentId(resident.getId());
        if (resident.getHouse() != null) {
            workerVisit.setHouseId(resident.getHouse().getId());
        }

        WorkerVisit saved = workerVisitRepository.save(workerVisit);
        return ResponseEntity.ok(saved);
    }

    public List<WorkerVisit> getWorkerVisitsByResident(String token) {
        String email = jwtTokenProvider.getUsernameFromToken(token);
        Optional<Resident> residentOptional = residentRepository.findByEmail(email);

        if (residentOptional.isEmpty()) {
            return List.of();
        }

        return workerVisitRepository.findByResidentId(residentOptional.get().getId());
    }

    public ResponseEntity<?> getWorkerVisitById(String id) {
        Optional<WorkerVisit> optional = workerVisitRepository.findById(id);
        return optional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteWorkerVisit(String token, String id) {
        Optional<WorkerVisit> optional = workerVisitRepository.findById(id);
        if (optional.isPresent()) {
            workerVisitRepository.deleteById(id);
            return ResponseEntity.ok("Visita de trabajador eliminada correctamente.");
        }
        return ResponseEntity.status(404).body("Visita de trabajador no encontrada.");
    }

    public ResponseEntity<?> updateWorkerVisit(String token, String id, WorkerVisit updatedVisit) {
        Optional<WorkerVisit> optional = workerVisitRepository.findById(id);
        if (optional.isPresent()) {
            WorkerVisit visit = optional.get();
            visit.setWorkerName(updatedVisit.getWorkerName());
            visit.setAge(updatedVisit.getAge());
            visit.setAddress(updatedVisit.getAddress());
            visit.setInePhoto(updatedVisit.getInePhoto());

            return ResponseEntity.ok(workerVisitRepository.save(visit));
        }
        return ResponseEntity.status(404).body("Visita de trabajador no encontrada.");
    }
}