package utez.edu.mx.cosevif.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import utez.edu.mx.cosevif.model.Guard;
import utez.edu.mx.cosevif.model.Resident;
import utez.edu.mx.cosevif.model.User;
import utez.edu.mx.cosevif.repository.GuardRepository;
import utez.edu.mx.cosevif.repository.ResidentRepository;
import utez.edu.mx.cosevif.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final ResidentRepository residentRepository;
    private final GuardRepository guardRepository; // Repositorio para los guardias


    public UserDetailsServiceImpl(UserRepository userRepository, ResidentRepository residentRepository, GuardRepository guardRepository) {
        this.userRepository = userRepository;
        this.residentRepository = residentRepository;
        this.guardRepository = guardRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 🔹 Buscar primero en la tabla de ADMINISTRADORES
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString())))
                    .build();
        }

        // 🔹 Si no se encontró en ADMIN, buscar en RESIDENTES
        Optional<Resident> residentOptional = residentRepository.findByEmail(username);

        if (residentOptional.isEmpty()) {
            residentOptional = residentRepository.findByPhone(username);
        }

        if (residentOptional.isPresent()) {
            Resident resident = residentOptional.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(resident.getEmail())  // Se usa el email como username
                    .password(resident.getPassword())
                    .authorities(Collections.singleton(new SimpleGrantedAuthority("RESIDENT"))) // Asigna el rol RESIDENT
                    .build();
        }
        // 🔹 Si no se encontró en RESIDENTES, buscar en GUARDIAS
        Optional<Guard> guardOptional = guardRepository.findByUsername(username);

        if (guardOptional.isPresent()) {
            Guard guard = guardOptional.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(guard.getUsername())
                    .password(guard.getPassword())
                    .authorities(Collections.singleton(new SimpleGrantedAuthority("GUARDIA"))) // Asigna el rol GUARD
                    .build();
        }


        // 🔥 Si no se encontró en ninguno, lanzar excepción
        throw new UsernameNotFoundException("Usuario no encontrado");
    }

}