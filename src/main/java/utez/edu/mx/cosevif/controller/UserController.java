package utez.edu.mx.cosevif.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.Role;
import utez.edu.mx.cosevif.model.User;
import utez.edu.mx.cosevif.repository.RoleRepository;
import utez.edu.mx.cosevif.repository.UserRepository;
import utez.edu.mx.cosevif.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        Optional<Role> roleOpt = roleRepository.findByName(user.getRole().getName());
        if (!roleOpt.isPresent()) {
            return ResponseEntity.badRequest().body("Rol no encontrado");
        }

        user.setRole(roleOpt.get());
        userRepository.save(user);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}