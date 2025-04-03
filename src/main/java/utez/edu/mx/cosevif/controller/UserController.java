package utez.edu.mx.cosevif.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.User;
import utez.edu.mx.cosevif.repository.UserRepository;


import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Verifica que el rol sea válido
        if (!user.getRole().equals("ROLE_ADMIN") && !user.getRole().equals("ROLE_RESIDENT") && !user.getRole().equals("ROLE_GUARDIA")) {
            return ResponseEntity.badRequest().body("Rol no válido");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
