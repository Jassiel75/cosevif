package utez.edu.mx.cosevif.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.cosevif.model.Resident;
import utez.edu.mx.cosevif.repository.ResidentRepository;
import utez.edu.mx.cosevif.security.JwtTokenProvider;

import java.util.Optional;
@RestController
@RequestMapping("/resident")
public class ResidentDashController {

    private final ResidentRepository residentRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public ResidentDashController(ResidentRepository residentRepository, JwtTokenProvider jwtTokenProvider) {
        this.residentRepository = residentRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('RESIDENT')")
    public ResponseEntity<?> dashboard(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtTokenProvider.getUsernameFromToken(token);

        Optional<Resident> residentOptional = residentRepository.findByEmail(email);
        if (residentOptional.isPresent()) {
            Resident resident = residentOptional.get();
            return ResponseEntity.ok("Bienvenido residente: " + resident.getName());
        } else {
            return ResponseEntity.status(404).body("Residente no encontrado");
        }
    }
}