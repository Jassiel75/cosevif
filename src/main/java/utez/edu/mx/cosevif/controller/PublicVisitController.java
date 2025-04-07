package utez.edu.mx.cosevif.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.cosevif.model.Resident;
import utez.edu.mx.cosevif.model.Visit;
import utez.edu.mx.cosevif.repository.ResidentRepository;
import utez.edu.mx.cosevif.repository.VisitRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "*")
public class PublicVisitController {

    private final VisitRepository visitRepository;
    private final ResidentRepository residentRepository; // Asegúrate de tener esto inyectado

    public PublicVisitController(VisitRepository visitRepository, ResidentRepository residentRepository) {
        this.visitRepository = visitRepository;
        this.residentRepository = residentRepository;
    }

    @PostMapping("/visit")
    public ResponseEntity<?> createVisitByGuest(@RequestParam String residentId, @RequestBody Visit visit) {
        try {
            // 🔹 Buscar al residente por ID
            Optional<Resident> residentOpt = residentRepository.findById(residentId);

            if (residentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Residente no encontrado.");
            }

            Resident resident = residentOpt.get();

            // 🔹 Asociar datos del residente
            visit.setResidentId(residentId);
            visit.setHouseId(resident.getHouse().getId());
            visit.setStatus("PENDING");

            // 🔹 Generar QR
            String qrBase64 = generateQRCode(visit);
            visit.setQrCode(qrBase64);

            visitRepository.save(visit);

            return ResponseEntity.ok("✅ Visita registrada correctamente.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ Error al registrar la visita.");
        }
    }

    // 🔹 Función para generar QR con datos
    private String generateQRCode(Visit visit) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            String qrContent = "📌 VISITA REGISTRADA \n" +
                    "🏡 Casa: " + visit.getHouseId() + "\n" +
                    "👤 Visitante: " + visit.getVisitorName() + "\n" +
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
}
