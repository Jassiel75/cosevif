package utez.edu.mx.cosevif.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import utez.edu.mx.cosevif.model.House;
import utez.edu.mx.cosevif.service.HouseService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/houses")
public class HouseController {

    private final HouseService houseService;

    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }


    // 🔹 Obtener todas las casas
    @GetMapping
    public List<House> getAllHouses() {
        return houseService.findAll();
    }

    // 🔹 Obtener casa por ID
    @GetMapping("/{id}")
    public ResponseEntity<House> getHouseById(@PathVariable String id) {
        Optional<House> house = houseService.findById(id);
        return house.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    // 🔹 Registrar una casa con imagen en Base64
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> registerHouse(
            @RequestParam("address") String address,
            @RequestParam("street") String street,
            @RequestParam("houseNumber") int houseNumber,
            @RequestParam("description") String description,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {

        return houseService.registerHouse(address, street, houseNumber, description, photo);
    }


    // 🔹 Endpoint para obtener la imagen como archivo
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getHouseImage(@PathVariable String id) {
        return houseService.getHouseImage(id);
    }


    // 🔹 Actualizar una casa
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateHouse(
            @PathVariable String id,
            @RequestParam("address") String address,
            @RequestParam("street") String street,
            @RequestParam("houseNumber") int houseNumber,
            @RequestParam("description") String description,
            @RequestParam(value = "photo", required = false) MultipartFile photo) {

        return houseService.updateHouse(id, address, street, houseNumber, description, photo);
    }


    // 🔹 Eliminar una casa por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHouse(@PathVariable String id) {
        return houseService.deleteHouse(id);
    }

}
