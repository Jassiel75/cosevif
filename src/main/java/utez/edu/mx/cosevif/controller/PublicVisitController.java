package utez.edu.mx.cosevif.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.Visit;
import utez.edu.mx.cosevif.repository.VisitRepository;
@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "*")
public class PublicVisitController {

    private final VisitRepository visitRepository;

    public PublicVisitController(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
    }

    @PostMapping("/visit")
    public ResponseEntity<?> createVisitByGuest(
            @RequestBody Visit visit,
            @RequestParam String residentId
    ) {
        try {
            if (residentId == null || residentId.isEmpty()) {
                return ResponseEntity.badRequest().body("❌ ID del residente es obligatorio.");
            }

            visit.setResidentId(residentId); // Asignar el ID del residente
            visit.setStatus("PENDING"); // Estado inicial
            visitRepository.save(visit);

            return ResponseEntity.ok("✅ Visita registrada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Error al registrar la visita.");
        }
    }

}