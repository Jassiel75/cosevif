package utez.edu.mx.cosevif.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.Guard;
import utez.edu.mx.cosevif.service.GuardService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/guards")
public class GuardController {

    private final GuardService guardService;

    public GuardController(GuardService guardService) {
        this.guardService = guardService;
    }
    @GetMapping
    public ResponseEntity<List<Guard>> getAllGuards() {
        return ResponseEntity.ok(guardService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guard>getGuard(@PathVariable String id) {
        Optional<Guard> guard = guardService.findById(id);
        return guard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Guard> createGuard(@RequestBody Guard guard) {
        return ResponseEntity.ok(guardService.save(guard));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuard(@PathVariable String id) {
        guardService.deleteGuard(id);
        return ResponseEntity.ok().build();
    }

}
