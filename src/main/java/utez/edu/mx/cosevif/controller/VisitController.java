package utez.edu.mx.cosevif.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.Visit;
import utez.edu.mx.cosevif.service.VisitService;

import java.util.List;

@RestController
@RequestMapping("/resident")
@PreAuthorize("hasRole('RESIDENT')")
@CrossOrigin(origins = "*") // Permitir solicitudes de cualquier origen
public class VisitController {

    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    // 🔹 Registrar una nueva visita
    @PostMapping("/visit")
    public ResponseEntity<?> registerVisit(@RequestHeader("Authorization") String authHeader, @RequestBody Visit visit) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token inválido o no proporcionado.");
        }

        String token = authHeader.replace("Bearer ", "").trim();
        return visitService.registerVisit(token, visit);
    }

    // 🔹 Obtener todas las visitas del residente
    @GetMapping("/visits")
    public List<Visit> getVisitsByResident(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return List.of();  // Devuelve lista vacía si el token es inválido
        }

        String token = authHeader.replace("Bearer ", "").trim();
        return visitService.getVisitsByResident(token);
    }

    // 🔹 Actualizar una visita (por ejemplo, marcar como completada)
    @PutMapping("/visit/{id}")
    public ResponseEntity<?> updateVisit(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable String id,
                                         @RequestBody Visit updatedVisit) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token inválido o no proporcionado.");
        }

        String token = authHeader.replace("Bearer ", "").trim();
        return visitService.updateVisit(token, id, updatedVisit);
    }

    // 🔹 Eliminar una visita
    @DeleteMapping("/visit/{id}")
    public ResponseEntity<?> deleteVisit(@RequestHeader("Authorization") String authHeader, @PathVariable String id) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token inválido o no proporcionado.");
        }

        String token = authHeader.replace("Bearer ", "").trim();
        return visitService.deleteVisit(token, id);
    }

    // 🔹 Cancelar una visita
    @PutMapping("/visit/{id}/cancel")
    public ResponseEntity<?> cancelVisit(@RequestHeader("Authorization") String authHeader, @PathVariable String id) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token inválido o no proporcionado.");
        }

        String token = authHeader.replace("Bearer ", "").trim();
        return visitService.cancelVisit(token, id);
    }
}

