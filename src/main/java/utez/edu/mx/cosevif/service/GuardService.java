package utez.edu.mx.cosevif.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utez.edu.mx.cosevif.model.Guard;
import utez.edu.mx.cosevif.repository.GuardRepository;
import utez.edu.mx.cosevif.security.JwtTokenProvider;

import java.util.List;
import java.util.Optional;

@Service
public class GuardService {

    private final GuardRepository guardRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public GuardService(GuardRepository guardRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.guardRepository = guardRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    public List<Guard> findAll() {
        return guardRepository.findAll();
    }

    public Optional<Guard> findById(String id) {
        return guardRepository.findById(id);
    }

    public Guard save(Guard guard) {
        // Codificar la contraseña antes de guardar
        guard.setPassword(passwordEncoder.encode(guard.getPassword()));
        return guardRepository.save(guard);
    }

    public void deleteGuard(String id) {
        guardRepository.deleteById(id);
    }


    // 🔹 Método de autenticación de guardia
    public ResponseEntity<?> authenticateGuard(String username, String password) {
        // Buscar al guardia por su username
        Optional<Guard> guardOptional = guardRepository.findByUsername(username);

        if (guardOptional.isPresent() && passwordEncoder.matches(password, guardOptional.get().getPassword())) {
            // Obtener el guardia de Optional
            Guard guard = guardOptional.get();
            // Generar el token JWT con el username y el role
            String token = jwtTokenProvider.generateToken(guard.getUsername(), "GUARDIA");
            return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
        }

        return ResponseEntity.status(401).body("Credenciales incorrectas.");
    }


}
