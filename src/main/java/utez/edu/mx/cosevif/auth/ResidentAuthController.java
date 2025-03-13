package utez.edu.mx.cosevif.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.dto.LoginRequest;
import utez.edu.mx.cosevif.service.ResidentService;

@RestController
@RequestMapping("/auth/resident")
public class ResidentAuthController {

    private final ResidentService residentService;

    public ResidentAuthController(ResidentService residentService) {
        this.residentService = residentService;
    }

    // 🔹 Login para residentes
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return residentService.authenticateResident(loginRequest.getUsername(), loginRequest.getPassword());
    }

    // 🔹 Obtener el perfil del residente autenticado
    @GetMapping("/profile")
    public ResponseEntity<?> getResidentProfile(@RequestHeader("Authorization") String token) {
        return residentService.getResidentProfile(token);
    }
}
