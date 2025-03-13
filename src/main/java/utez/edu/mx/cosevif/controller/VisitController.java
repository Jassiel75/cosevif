package utez.edu.mx.cosevif.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.Visit;
import utez.edu.mx.cosevif.model.WorkerVisit;
import utez.edu.mx.cosevif.service.VisitService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/resident")
public class VisitController {
    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    // 🔹 Registrar una nueva visita
    @PostMapping("/visit")
    public ResponseEntity<?> registerVisit(
            @RequestHeader("Authorization") String authHeader, // Recibe el Authorization completo
            @RequestBody Visit visit) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Token inválido o no proporcionado.");
        }

        String token = authHeader.replace("Bearer ", "").trim(); // ✅ Eliminar "Bearer " y espacios

        return visitService.registerVisit(token, visit);
    }

    // 🔹 Obtener todas las visitas del residente
    @GetMapping("/visits")
    public List<Visit> getVisitsByResident(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return List.of(); // Devuelve lista vacía si el token es inválido
        }

        String token = authHeader.replace("Bearer ", "").trim();
        return visitService.getVisitsByResident(token);
    }
}
