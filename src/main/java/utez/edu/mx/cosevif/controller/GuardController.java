package utez.edu.mx.cosevif.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.Guard;
import utez.edu.mx.cosevif.service.GuardService;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/admin/guards")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
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
    public ResponseEntity<Guard> getGuard(@PathVariable String id) {
        Optional<Guard> guard = guardService.findById(id);
        return guard.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Guard> createGuard(@RequestBody Guard guard) {
        guard.setRole("ROLE_GUARDIA");
        return ResponseEntity.ok(guardService.save(guard));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGuard(@PathVariable String id, @RequestBody Guard updatedGuard) {
        Guard updated = guardService.updateGuard(id, updatedGuard);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuard(@PathVariable String id) {
        guardService.deleteGuard(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_GUARDIA')")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Bienvenido al Panel del Guardia");
    }
}