package utez.edu.mx.cosevif.auth;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.dto.JwtResponse;
import utez.edu.mx.cosevif.dto.LoginRequest;

import utez.edu.mx.cosevif.dto.LoginResponse;
import utez.edu.mx.cosevif.service.AuthService;



@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request.getUsername(), request.getPassword());
        System.out.println("Respuesta Login: " + response.getToken() + " - " + response.getId() + " - " + response.getUsername() + " - " + response.getRole());
        return ResponseEntity.ok(response);
    }
}