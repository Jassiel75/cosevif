package utez.edu.mx.cosevif.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.cosevif.model.WorkerVisit;
import utez.edu.mx.cosevif.security.JwtTokenProvider;
import utez.edu.mx.cosevif.service.WorkerVisitService;

@RestController
@RequestMapping("/resident")
@PreAuthorize("hasAuthority('RESIDENT')") // Solo residentes pueden acceder a estos endpoints
@CrossOrigin(origins = "*") // Permitir solicitudes de cualquier origen
public class WorkerVisitController {

    private final WorkerVisitService workerVisitService;
    private final JwtTokenProvider jwtTokenProvider;

    public WorkerVisitController(WorkerVisitService workerVisitService, JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.workerVisitService = workerVisitService;
    }

    // Registrar visita de trabajador
    @PostMapping("/workerVisits")
    public ResponseEntity<?> registerWorkerVisit(@RequestHeader("Authorization") String authHeader,
                                                 @RequestParam("workerName") String workerName,
                                                 @RequestParam("age") int age,
                                                 @RequestParam("address") String address,
                                                 @RequestParam("dateTime") String dateTime,
                                                 @RequestParam(value = "inePhoto", required = false) MultipartFile inePhoto) {
        String residentId = jwtTokenProvider.getUsernameFromToken(authHeader.substring(7));  // Extraer y decodificar el token

        // Llamar al servicio para registrar la visita del trabajador
        return workerVisitService.registerWorkerVisit(authHeader, workerName, age, address, dateTime, inePhoto);
    }

    // Obtener todas las visitas de trabajadores por residente
    @GetMapping("/workerVisits")
    public ResponseEntity<?> getWorkerVisitsByResident(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(workerVisitService.getWorkerVisitsByResident(authHeader));
    }

    // Obtener una visita de trabajador por su ID
    @GetMapping("/workerVisits/{id}")
    public ResponseEntity<?> getWorkerVisit(@PathVariable String id) {
        return workerVisitService.getWorkerVisitById(id);
    }

    // Eliminar una visita de trabajador
    @DeleteMapping("/workerVisits/{id}")
    public ResponseEntity<?> deleteWorkerVisit(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable String id) {
        return workerVisitService.deleteWorkerVisit(authHeader, id);
    }

    // Actualizar una visita de trabajador
    @PutMapping("/workerVisits/{id}")
    public ResponseEntity<?> updateWorkerVisit(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable String id,
                                               @RequestParam("workerName") String workerName,
                                               @RequestParam("age") int age,
                                               @RequestParam("address") String address,
                                               @RequestParam("dateTime") String dateTime,
                                               @RequestParam(value = "inePhoto", required = false) MultipartFile inePhoto) {
        return workerVisitService.updateWorkerVisit(authHeader, id, workerName, age, address, dateTime, inePhoto);
    }
}