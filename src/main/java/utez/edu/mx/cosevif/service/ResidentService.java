package utez.edu.mx.cosevif.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import utez.edu.mx.cosevif.model.House;
import utez.edu.mx.cosevif.model.Resident;
import utez.edu.mx.cosevif.repository.HouseRepository;
import utez.edu.mx.cosevif.repository.ResidentRepository;
import utez.edu.mx.cosevif.security.JwtTokenProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ResidentService {
    private final ResidentRepository residentRepository;
    private final HouseRepository houseRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;



    public ResidentService(ResidentRepository residentRepository, HouseRepository houseRepository, JwtTokenProvider jwtTokenProvider) {
        this.residentRepository = residentRepository;
        this.houseRepository = houseRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtTokenProvider = jwtTokenProvider;


    }

    // 🔹 Método para autenticar residentes y devolver un Token JWT
    public ResponseEntity<?> authenticateResident(String username, String password) {
        Optional<Resident> residentOptional = residentRepository.findByEmail(username);

        if (residentOptional.isEmpty()) {
            residentOptional = residentRepository.findByPhone(username);
        }

        if (residentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }

        Resident resident = residentOptional.get();

        if (!passwordEncoder.matches(password, resident.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas.");
        }

        String token = jwtTokenProvider.generateToken(resident.getEmail(), "RESIDENT");

        // 🔥 NUEVO: Enviar también el id, username y role
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("id", resident.getId());
        response.put("username", resident.getEmail());
        response.put("role", "RESIDENT");

        return ResponseEntity.ok(response);
    }

    // 🔹 Obtener el perfil del residente autenticado
    public ResponseEntity<?> getResidentProfile(String token) {
        String email = jwtTokenProvider.getUsernameFromToken(token.replace("Bearer ", ""));

        Optional<Resident> residentOptional = residentRepository.findByEmail(email);
        if (residentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Residente no encontrado.");
        }

        return ResponseEntity.ok(residentOptional.get());
    }


    // 🔹 3. Obtener todos los residentes (Solo Admin)
    public List<Resident> findAll() {
        return residentRepository.findAll();
    }

    // 🔹 4. Obtener un residente por ID (Solo Admin)
    public Optional<Resident> findById(String id) {
        return residentRepository.findById(id);
    }
    // 🔹 5. Registrar un nuevo residente (Solo Admin)
    public ResponseEntity<?> registerResident(Resident resident) {

        // 🔹 Verificar si la casa existe
        Optional<House> houseOptional = houseRepository.findById(resident.getHouseId());

        if (houseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La casa no existe.");
        }
        // Verificar si ya existe un residente con el mismo teléfono
        if (!residentRepository.findByPhone(resident.getPhone()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El número de teléfono ya está registrado.");
        }

        // 🔹 Verificar si la casa ya tiene un residente asignado
        boolean residentExists = residentRepository.existsByHouseId(resident.getHouseId());
        if (residentExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta casa ya tiene un residente asignado.");
        }

        // 🔹 Verificar si el correo ya está registrado
        if (residentRepository.findByEmail(resident.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado.");
        }

        // 🔹 Encriptar la contraseña antes de guardarla
        resident.setPassword(passwordEncoder.encode(resident.getPassword()));

        // 🔹 Guardar el residente en la base de datos
        Resident savedResident = residentRepository.save(resident);

        return ResponseEntity.ok(savedResident);
    }

    // 🔹 6. Actualizar datos del residente (Solo Admin)
    public ResponseEntity<?> updateResident(String id, Resident updatedResident) {
        Optional<Resident> residentOptional = residentRepository.findById(id);

        if (residentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El residente no existe.");
        }

        Resident existingResident = residentOptional.get();

        // Verificar si la casa asignada es diferente a la actual
        if (!existingResident.getHouseId().equals(updatedResident.getHouseId())) {
            Optional<House> newHouseOptional = houseRepository.findById(updatedResident.getHouseId());

            if (newHouseOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La nueva casa no existe.");
            }


            // Verificar si la nueva casa ya tiene un residente asignado
            if (residentRepository.existsByHouseId(updatedResident.getHouseId())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("La nueva casa ya tiene un residente asignado.");
            }
        }


        // Actualizar los datos
        existingResident.setName(updatedResident.getName());
        existingResident.setSurnames(updatedResident.getSurnames());
        existingResident.setAge(updatedResident.getAge());
        existingResident.setBirthDate(updatedResident.getBirthDate());
        existingResident.setEmail(updatedResident.getEmail());
        existingResident.setAddress(updatedResident.getAddress());
        existingResident.setStreet(updatedResident.getStreet());
        existingResident.setPhone(updatedResident.getPhone());
        existingResident.setHouseId(updatedResident.getHouseId());

        // Verificar si se cambió la contraseña y encriptarla
        if (updatedResident.getPassword() != null && !updatedResident.getPassword().isEmpty()) {
            existingResident.setPassword(passwordEncoder.encode(updatedResident.getPassword()));
        }

        Resident savedResident = residentRepository.save(existingResident);
        return ResponseEntity.ok(savedResident);
    }



    // 🔹 7. Eliminar un residente (Solo Admin)
    public ResponseEntity<Void> deleteResident(String id) {
        Optional<Resident> residentOptional = residentRepository.findById(id);

        if (residentOptional.isPresent()) {
            Resident resident = residentOptional.get();

            // Liberar la casa
            Optional<House> houseOptional = houseRepository.findById(resident.getHouseId());
            houseOptional.ifPresent(house -> {
                house.setId(null);
                houseRepository.save(house);
            });

            residentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }



}
