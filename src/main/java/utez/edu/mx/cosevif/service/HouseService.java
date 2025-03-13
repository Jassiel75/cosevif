package utez.edu.mx.cosevif.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.cosevif.model.House;
import utez.edu.mx.cosevif.repository.HouseRepository;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class HouseService {

    private final HouseRepository houseRepository;

    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    public List<House> findAll() {
        return houseRepository.findAll();
    }

    public Optional<House> findById(String id) {
        return houseRepository.findById(id);
    }


    // 🔹 Registrar una casa con imagen en Base64
    public ResponseEntity<?> registerHouse(String address, String street, int houseNumber, String description, MultipartFile photo) {
        try {
            String encodedPhoto = null;

            if (photo != null && !photo.isEmpty()) {
                encodedPhoto = Base64.getEncoder().encodeToString(photo.getBytes());
            }

            House newHouse = new House(address, street, houseNumber, description, encodedPhoto);
            House savedHouse = houseRepository.save(newHouse);

            return ResponseEntity.ok(savedHouse);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la imagen.");
        }
    }

    // 🔹 Obtener la imagen de una casa en Base64
    public ResponseEntity<byte[]> getHouseImage(String id) {
        Optional<House> houseOptional = houseRepository.findById(id);

        if (houseOptional.isEmpty() || houseOptional.get().getPhoto() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        House house = houseOptional.get();
        byte[] imageBytes = Base64.getDecoder().decode(house.getPhoto());

        return ResponseEntity.ok().contentType(org.springframework.http.MediaType.IMAGE_PNG).body(imageBytes);
    }



    // 🔹 Actualizar una casa
    public ResponseEntity<?> updateHouse(String id, String address, String street, int houseNumber,
                                         String description, MultipartFile photo) {
        Optional<House> houseOptional = houseRepository.findById(id);

        if (houseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La casa no existe.");
        }

        House house = houseOptional.get();
        house.setAddress(address);
        house.setStreet(street);
        house.setHouseNumber(houseNumber);
        house.setDescription(description);

        // Si se envió una nueva imagen, actualizarla en la base de datos
        if (photo != null && !photo.isEmpty()) {
            try {
                String base64Image = Base64.getEncoder().encodeToString(photo.getBytes());
                house.setPhoto(base64Image);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al procesar la imagen.");
            }
        }

        House updatedHouse = houseRepository.save(house);
        return ResponseEntity.ok(updatedHouse);
    }


    // 🔹 Eliminar una casa por ID
    public ResponseEntity<?> deleteHouse(String id) {
        Optional<House> houseOptional = houseRepository.findById(id);

        if (houseOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La casa no existe.");
        }

        houseRepository.deleteById(id);
        return ResponseEntity.ok("Casa eliminada correctamente.");
    }







}

