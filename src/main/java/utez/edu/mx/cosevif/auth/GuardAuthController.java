package utez.edu.mx.cosevif.auth;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.cosevif.model.Guard;
import utez.edu.mx.cosevif.service.GuardService;

@RestController
@RequestMapping("/auth/guard")
public class GuardAuthController {

    private final GuardService guardService;

    public GuardAuthController(GuardService guardService) {
        this.guardService = guardService;
    }

    // 🔹 Login para Guardias
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Guard guardRequest) {
        return guardService.authenticateGuard(guardRequest.getPhone(), guardRequest.getPassword());
    }
}
