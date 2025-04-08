package utez.edu.mx.cosevif.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.dto.LoginRequest;
import utez.edu.mx.cosevif.model.Resident;
import utez.edu.mx.cosevif.service.ResidentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/residents")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class ResidentController {
    private final ResidentService residentService;

    public ResidentController(ResidentService residentService) {
        this.residentService = residentService;
    }

    @GetMapping
    public List<Resident> getAllResidents() {
        return residentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resident> getResidentById(@PathVariable String id) {
        Optional<Resident> resident = residentService.findById(id);
        return resident.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> registerResident(@RequestBody Resident resident) {
        return residentService.registerResident(resident);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResident(@PathVariable String id, @RequestBody Resident updatedResident) {
        return residentService.updateResident(id, updatedResident);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResident(@PathVariable String id) {
        return residentService.deleteResident(id);
    }
}


