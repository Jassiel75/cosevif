package utez.edu.mx.cosevif.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.cosevif.dto.WorkerVisitDTO;
import utez.edu.mx.cosevif.model.WorkerVisit;
import utez.edu.mx.cosevif.model.Resident;
import utez.edu.mx.cosevif.repository.WorkerVisitRepository;
import utez.edu.mx.cosevif.repository.ResidentRepository;
import utez.edu.mx.cosevif.security.JwtTokenProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class WorkerVisitService {

    private final WorkerVisitRepository workerVisitRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResidentRepository residentRepository;

    public WorkerVisitService(WorkerVisitRepository workerVisitRepository,
                              JwtTokenProvider jwtTokenProvider,
                              ResidentRepository residentRepository) {
        this.workerVisitRepository = workerVisitRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.residentRepository = residentRepository;
    }

    // 🔹 Registrar una visita de trabajador
    public ResponseEntity<?> registerWorkerVisit(String authHeader, String workerName, int age,
                                                 String address, String dateTime, MultipartFile inePhoto) {
        try {
            String token = authHeader.substring(7);
            String email = jwtTokenProvider.getUsernameFromToken(token);

            Optional<Resident> optionalResident = residentRepository.findByEmail(email);
            if (optionalResident.isEmpty()) {
                return ResponseEntity.status(404).body("Residente no encontrado.");
            }

            Resident resident = optionalResident.get();

            String encodedPhoto = null;
            if (inePhoto != null && !inePhoto.isEmpty()) {
                encodedPhoto = Base64.getEncoder().encodeToString(inePhoto.getBytes());
            }

            WorkerVisit workerVisit = new WorkerVisit();
            workerVisit.setWorkerName(workerName);
            workerVisit.setAge(age);
            workerVisit.setAddress(address);
            workerVisit.setDateTime(LocalDateTime.parse(dateTime));
            workerVisit.setInePhoto(encodedPhoto);
            workerVisit.setResidentId(resident.getId()); // ID del residente
            workerVisit.setHouseId(resident.getHouse().getId()); // ID de la casa

            WorkerVisit savedVisit = workerVisitRepository.save(workerVisit);

            // 🔸 Extra: nombre del residente y número de casa
            Map<String, Object> response = new HashMap<>();
            response.put("visit", savedVisit);
            response.put("residentName", resident.getName() + " " + resident.getSurnames());
            response.put("houseNumber", resident.getHouse().getHouseNumber());

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Error al procesar la imagen del INE.");
        }
    }



    // 🔹 Obtener todas las visitas (a futuro puedes filtrarlas por ID)
    public List<WorkerVisitDTO> getWorkerVisitsByResident(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtTokenProvider.getUsernameFromToken(token);

        Optional<Resident> optionalResident = residentRepository.findByEmail(email);
        if (optionalResident.isEmpty()) return List.of();

        Resident resident = optionalResident.get();
        String residentName = resident.getName() + " " + resident.getSurnames();
        int houseNumber = resident.getHouse().getHouseNumber();

        List<WorkerVisit> visits = workerVisitRepository.findAllByResidentId(resident.getId());

        return visits.stream()
                .map(visit -> new WorkerVisitDTO(visit, residentName, houseNumber))
                .toList();
    }

    public ResponseEntity<?> getWorkerVisitById(String id) {
        Optional<WorkerVisit> workerVisit = workerVisitRepository.findById(id);
        return workerVisit.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<?> deleteWorkerVisit(String authHeader, String id) {
        Optional<WorkerVisit> workerVisitOptional = workerVisitRepository.findById(id);
        if (workerVisitOptional.isPresent()) {
            workerVisitRepository.deleteById(id);
            return ResponseEntity.ok("Visita de trabajador eliminada correctamente.");
        }
        return ResponseEntity.status(404).body("Visita de trabajador no encontrada.");
    }

    public ResponseEntity<?> updateWorkerVisit(String authHeader, String id, WorkerVisit updatedVisit) {
        Optional<WorkerVisit> workerVisitOptional = workerVisitRepository.findById(id);
        if (workerVisitOptional.isPresent()) {
            WorkerVisit workerVisit = workerVisitOptional.get();
            workerVisit.setWorkerName(updatedVisit.getWorkerName());
            workerVisit.setAge(updatedVisit.getAge());
            workerVisit.setAddress(updatedVisit.getAddress());
            workerVisit.setInePhoto(updatedVisit.getInePhoto());
            workerVisit.setDateTime(updatedVisit.getDateTime());

            workerVisitRepository.save(workerVisit);
            return ResponseEntity.ok(workerVisit);
        }
        return ResponseEntity.status(404).body("Visita de trabajador no encontrada.");
    }
}
