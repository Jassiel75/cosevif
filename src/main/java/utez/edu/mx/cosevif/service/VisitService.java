package utez.edu.mx.cosevif.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import utez.edu.mx.cosevif.model.House;
import utez.edu.mx.cosevif.model.Resident;
import utez.edu.mx.cosevif.model.Visit;
import utez.edu.mx.cosevif.repository.HouseRepository;
import utez.edu.mx.cosevif.repository.ResidentRepository;
import utez.edu.mx.cosevif.repository.VisitRepository;
import utez.edu.mx.cosevif.security.JwtTokenProvider;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    private final VisitRepository visitRepository;
    private final ResidentRepository residentRepository;
    private final HouseRepository houseRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public VisitService(VisitRepository visitRepository, ResidentRepository residentRepository, JwtTokenProvider jwtTokenProvider, HouseRepository houseRepository) {
        this.visitRepository = visitRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.houseRepository = houseRepository;
        this.residentRepository = residentRepository;
    }

    // 🔹 Registrar una nueva visita
    public ResponseEntity<?> registerVisit(String token, Visit visit) {
        // ✅ Obtener el ID del residente desde el token JWT
        String residentEmail = jwtTokenProvider.getUsernameFromToken(token);
        Optional<Resident> residentOptional = residentRepository.findByEmail(residentEmail);

        if (residentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Residente no encontrado.");
        }

        Resident resident = residentOptional.get();

        // ✅ Obtener la casa desde la relación (ya no usamos houseId, accedemos directamente al objeto House)
        House house = resident.getHouse(); // Obtener el objeto House completo desde la relación
        int houseNumber = house != null ? house.getHouseNumber() : -1;

        // ✅ Asignar datos de la visita
        visit.setResidentId(resident.getId());
        visit.setHouseId(resident.getHouse().getId());  // Sigue siendo necesario tener el ID de la casa para la relación en la base de datos
        visit.setStatus("PENDIENTE");

        // ✅ Generar el código QR si falta 1 hora o menos para la visita
        LocalDateTime qrGenerationTime = visit.getDateTime().minusHours(1);
        if (LocalDateTime.now().isAfter(qrGenerationTime)) {
            visit.setQrCode(generateQRCode(visit, String.valueOf(houseNumber))); // ✅ Pasar houseNumber al QR
        } else {
            visit.setQrCode(null);
        }

        // ✅ Guardar la visita en la base de datos
        Visit savedVisit = visitRepository.save(visit);
        return ResponseEntity.ok(savedVisit);
    }

    // 🔹 Generar QR en Base64 con todos los datos necesarios
    private String generateQRCode(Visit visit, String houseNumber) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String qrContent = "📌 VISITA REGISTRADA \n" +
                    "🏡 Casa: " + houseNumber + "\n" +
                    "👤 Visitante: " + (visit.getVisitorName() != null ? visit.getVisitorName() : "No registrado") + "\n" +
                    "📅 Fecha y Hora: " + visit.getDateTime() + "\n" +
                    "🚗 Vehículo: " + (visit.getVehiclePlate() != null ? visit.getVehiclePlate() : "No registrado") + "\n" +
                    "🔑 Clave de acceso: " + visit.getPassword() + "\n" +
                    "👥 Personas: " + visit.getNumPeople();

            BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 250, 250);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] qrBytes = pngOutputStream.toByteArray();

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(qrBytes);
        } catch (WriterException | IOException e) {
            return null;
        }
    }

    // 🔹 Obtener visitas de un residente autenticado
    public List<Visit> getVisitsByResident(String token) {
        String residentEmail = jwtTokenProvider.getUsernameFromToken(token);
        Optional<Resident> residentOptional = residentRepository.findByEmail(residentEmail);

        if (residentOptional.isEmpty()) {
            return List.of();
        }

        return visitRepository.findByResidentId(residentOptional.get().getId());
    }
}
