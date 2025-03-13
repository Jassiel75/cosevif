package utez.edu.mx.cosevif.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.WorkerVisit;
import utez.edu.mx.cosevif.service.WorkerVisitService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/resident/workerVisits")
public class WorkerVisitController {

    private final WorkerVisitService workerVisitService;

    public WorkerVisitController(WorkerVisitService workerVisitService){
        this.workerVisitService = workerVisitService;
    }
    @PostMapping
    public ResponseEntity<?> registerWorkerVisit(@RequestBody WorkerVisit workerVisit) {
        return workerVisitService.registerWorkerVisit(workerVisit);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<WorkerVisit>> getWorkerVisit(@PathVariable String id) {
        return ResponseEntity.ok(workerVisitService.getWorkerVisitById(id));
    }

    @GetMapping
    public List<WorkerVisit> getWorkerVisitsByResident(@RequestParam String residentId) {
        return workerVisitService.getWorkerVisitsByResident(residentId);
    }

}
