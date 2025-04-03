package utez.edu.mx.cosevif.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.WorkerVisit;
import utez.edu.mx.cosevif.service.WorkerVisitService;

import java.util.List;
import java.util.Optional;


@RequestMapping("/resident/workerVisits")
@PreAuthorize("hasAuthority('RESIDENT')")
public class WorkerVisitController {

    private final WorkerVisitService workerVisitService;

    public WorkerVisitController(WorkerVisitService workerVisitService){
        this.workerVisitService = workerVisitService;
    }

    @PostMapping
    public ResponseEntity<?> registerWorkerVisit(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody WorkerVisit workerVisit) {
        return workerVisitService.registerWorkerVisit(extractToken(authHeader), workerVisit);
    }

    @GetMapping
    public ResponseEntity<?> getWorkerVisitsByResident(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(workerVisitService.getWorkerVisitsByResident(extractToken(authHeader)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkerVisit(@PathVariable String id) {
        return workerVisitService.getWorkerVisitById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkerVisit(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable String id) {
        return workerVisitService.deleteWorkerVisit(extractToken(authHeader), id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkerVisit(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable String id,
                                               @RequestBody WorkerVisit updatedVisit) {
        return workerVisitService.updateWorkerVisit(extractToken(authHeader), id, updatedVisit);
    }

    private String extractToken(String authHeader) {
        return authHeader.replace("Bearer ", "").trim();
    }
}