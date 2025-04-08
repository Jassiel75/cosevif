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
    public ResponseEntity<?> authenticateResident(String email, String password) {
        Optional<Resident> residentOptional = residentRepository.findByEmail(email);

        if (residentOptional.isPresent() && passwordEncoder.matches(password, residentOptional.get().getPassword())) {
            Resident resident = residentOptional.get();
            String token = jwtTokenProvider.generateToken(resident.getEmail(), "RESIDENT");

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("id", resident.getId());
            response.put("email", resident.getEmail());
            response.put("name", resident.getName());
            response.put("surnames", resident.getSurnames());
            response.put("phone", resident.getPhone());
            response.put("address", resident.getAddress());
            response.put("street", resident.getStreet());
            response.put("age", resident.getAge());
            response.put("birthDate", resident.getBirthDate());
            response.put("role", "RESIDENT");

            // Incluir ID y número de casa si existe
            if (resident.getHouse() != null) {
                response.put("houseId", resident.getHouse().getId());
                response.put("houseNumber", resident.getHouse().getHouseNumber());
            }

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("Credenciales incorrectas.");
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
        List<Resident> residents = residentRepository.findAll();

        // Enriquecer los residentes con la casa asociada, si existe
        for (Resident resident : residents) {
            if (resident.getHouse() != null) { // Verifica si la casa está asociada
                Optional<House> house = houseRepository.findById(resident.getHouse().getId());
                house.ifPresent(resident::setHouse);  // Setear la casa completa en el residente
            }
        }

        return residents;
    }

    // 🔹 4. Obtener un residente por ID (Solo Admin)
    public Optional<Resident> findById(String id) {
        Optional<Resident> resident = residentRepository.findById(id);
        if (resident.isPresent() && resident.get().getHouse() != null) {  // Verifica si la casa está asociada
            Optional<House> house = houseRepository.findById(resident.get().getHouse().getId());
            house.ifPresent(res -> resident.get().setHouse(res));
        }
        return resident;
    }

    // 🔹 5. Registrar un nuevo residente (Solo Admin)
    public ResponseEntity<?> registerResident(Resident resident) {
        // Verificar si la casa existe
        Optional<House> houseOptional = houseRepository.findById(resident.getHouse().getId());

        if (houseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La casa no existe.");
        }

        // Asignar la casa al residente
        House house = houseOptional.get();
        resident.setHouse(house);  // Establecer la casa en el residente

        // Verificar si ya existe un residente con el mismo teléfono
        if (!residentRepository.findByPhone(resident.getPhone()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El número de teléfono ya está registrado.");
        }

        // Verificar si la casa ya tiene un residente asignado
        boolean residentExists = residentRepository.existsByHouseId(resident.getHouse().getId());
        if (residentExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Esta casa ya tiene un residente asignado.");
        }

        // Verificar si el correo ya está registrado
        if (residentRepository.findByEmail(resident.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo ya está registrado.");
        }

        // Encriptar la contraseña antes de guardarla
        resident.setPassword(passwordEncoder.encode(resident.getPassword()));

        // Guardar el residente en la base de datos
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
        if (!existingResident.getHouse().getId().equals(updatedResident.getHouse().getId())) {
            Optional<House> newHouseOptional = houseRepository.findById(updatedResident.getHouse().getId());

            if (newHouseOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La nueva casa no existe.");
            }

            // Verificar si la nueva casa ya tiene un residente asignado
            if (residentRepository.existsByHouseId(updatedResident.getHouse().getId())) {
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
        existingResident.setHouse(updatedResident.getHouse()); // Actualizar la casa

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
            residentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    // 🔹 Modificación en ResidentService para permitir que un residente actualice su propio perfil
    public ResponseEntity<?> updateResidentProfile(String token, Resident updatedResident) {
        // Extraer el token eliminando el prefijo "Bearer "
        String jwtToken = token.replace("Bearer ", "");
        String email = jwtTokenProvider.getUsernameFromToken(jwtToken);

        Optional<Resident> residentOptional = residentRepository.findByEmail(email);

        if (residentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Residente no encontrado.");
        }

        Resident existingResident = residentOptional.get();

        // Solo permitir modificar su propio perfil
        existingResident.setName(updatedResident.getName());
        existingResident.setSurnames(updatedResident.getSurnames());
        existingResident.setAge(updatedResident.getAge());
        existingResident.setBirthDate(updatedResident.getBirthDate());
        existingResident.setEmail(updatedResident.getEmail());
        existingResident.setAddress(updatedResident.getAddress());
        existingResident.setStreet(updatedResident.getStreet());
        existingResident.setPhone(updatedResident.getPhone());

        // Si el residente proporcionó una nueva contraseña, la encriptamos y la guardamos
        if (updatedResident.getPassword() != null && !updatedResident.getPassword().isEmpty()) {
            existingResident.setPassword(passwordEncoder.encode(updatedResident.getPassword()));
        }

        // Guardar los cambios
        Resident savedResident = residentRepository.save(existingResident);

        // Crear una respuesta más amigable sin exponer datos sensibles
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Perfil actualizado correctamente");
        response.put("resident", savedResident);

        return ResponseEntity.ok(response);
    }




}
