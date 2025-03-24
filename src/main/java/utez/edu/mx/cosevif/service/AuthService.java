package utez.edu.mx.cosevif.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utez.edu.mx.cosevif.dto.LoginResponse;
import utez.edu.mx.cosevif.model.User;
import utez.edu.mx.cosevif.repository.UserRepository;
import utez.edu.mx.cosevif.security.JwtTokenProvider;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public LoginResponse login(String username, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            throw new RuntimeException("Usuario o contraseña incorrectos");
        }

        User user = userOptional.get();

        // Token solo incluye el username y rol (como en el back original)
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().toString());

        // Retorna un LoginResponse con los campos esperados
        return new LoginResponse(token, user.getId(), user.getUsername(), user.getRole().toString());
    }
}