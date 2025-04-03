package utez.edu.mx.cosevif.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utez.edu.mx.cosevif.dto.LoginResponse;
import utez.edu.mx.cosevif.model.User;
import utez.edu.mx.cosevif.model.Resident;
import utez.edu.mx.cosevif.model.Guard;
import utez.edu.mx.cosevif.repository.UserRepository;
import utez.edu.mx.cosevif.repository.ResidentRepository;
import utez.edu.mx.cosevif.repository.GuardRepository;
import utez.edu.mx.cosevif.security.JwtTokenProvider;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final ResidentRepository residentRepository;
    private final GuardRepository guardRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            UserRepository userRepository,
            ResidentRepository residentRepository,
            GuardRepository guardRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.residentRepository = residentRepository;
        this.guardRepository = guardRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(String username, String password) {
        // 1. Buscar en ADMIN (User)
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole());
                return new LoginResponse(token, user.getId(), user.getUsername(), user.getRole());
            }
        }

        // 2. Buscar en RESIDENTE (por email)
        Optional<Resident> residentOptional = residentRepository.findByEmail(username);
        if (residentOptional.isPresent()) {
            Resident resident = residentOptional.get();
            if (passwordEncoder.matches(password, resident.getPassword())) {
                String token = jwtTokenProvider.generateToken(resident.getEmail(), "RESIDENT"); // Asegúrate de pasar "RESIDENT" si es un residente
                return new LoginResponse(token, resident.getId(), resident.getEmail(), "RESIDENT"); // Aquí también el rol
            }
        }

        // 3. Buscar en GUARDIA (por phone)
        Optional<Guard> guardOptional = guardRepository.findByPhone(username);
        if (guardOptional.isPresent()) {
            Guard guard = guardOptional.get();
            if (passwordEncoder.matches(password, guard.getPassword())) {
                String token = jwtTokenProvider.generateToken(guard.getPhone(), "GUARD"); // Asegúrate de pasar "GUARD" si es un guardia
                return new LoginResponse(token, guard.getId(), guard.getPhone(), "GUARD"); // Aquí también el rol
            }
        }

        // Si no se encuentra en ninguna colección
        throw new RuntimeException("Usuario o contraseña incorrectos");
    }
}
