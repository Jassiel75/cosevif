package utez.edu.mx.cosevif.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import utez.edu.mx.cosevif.model.Guard;
import utez.edu.mx.cosevif.repository.GuardRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GuardService {

    private final GuardRepository guardRepository;
    private final PasswordEncoder passwordEncoder;

    public GuardService(GuardRepository guardRepository, PasswordEncoder passwordEncoder) {
        this.guardRepository = guardRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Guard> findAll() {
        return guardRepository.findAll();
    }
    public Optional<Guard> findById(String id) {
        return guardRepository.findById(id);
    }
    public Guard save(Guard guard) {
        guard.setPassword(passwordEncoder.encode(guard.getPassword()));
        return guardRepository.save(guard);
    }

    public void deleteGuard(String id) {
        guardRepository.deleteById(id);
    }

}
