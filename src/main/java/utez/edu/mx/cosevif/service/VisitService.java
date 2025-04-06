    package utez.edu.mx.cosevif.service;

    import com.google.zxing.BarcodeFormat;
    import com.google.zxing.WriterException;
    import com.google.zxing.common.BitMatrix;
    import com.google.zxing.qrcode.QRCodeWriter;
    import com.google.zxing.client.j2se.MatrixToImageWriter;

    import org.springframework.stereotype.Service;
    import org.springframework.http.ResponseEntity;
    import utez.edu.mx.cosevif.model.Visit;
    import utez.edu.mx.cosevif.repository.VisitRepository;
    import utez.edu.mx.cosevif.repository.ResidentRepository;
    import utez.edu.mx.cosevif.security.JwtTokenProvider;
    import utez.edu.mx.cosevif.model.Resident;

    import java.io.ByteArrayOutputStream;
    import java.io.IOException;
    import java.time.LocalDateTime;
    import java.util.List;
    import java.util.Base64;
    import java.util.Optional;

    @Service
    public class VisitService {

        private final VisitRepository visitRepository;
        private final JwtTokenProvider jwtTokenProvider;
        private final ResidentRepository residentRepository;

        public VisitService(VisitRepository visitRepository, JwtTokenProvider jwtTokenProvider, ResidentRepository residentRepository) {
            this.visitRepository = visitRepository;
            this.jwtTokenProvider = jwtTokenProvider;
            this.residentRepository = residentRepository;
        }

        // 🔹 Registrar una nueva visita
        public ResponseEntity<?> registerVisit(String token, Visit visit) {
            // Obtener el ID del residente desde el token JWT
            String residentEmail = jwtTokenProvider.getUsernameFromToken(token);

            Optional<Resident> residentOptional = residentRepository.findByEmail(residentEmail);
            if (residentOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Residente no encontrado.");
            }

            Resident resident = residentOptional.get();
            visit.setResidentId(resident.getId()); // Guardar el ID del residente
            visit.setHouseId(resident.getHouse().getId()); // Guardar el ID de la casa

            // Asignar el estado inicial
            visit.setStatus("PENDIENTE");

            // IMPORTANTE: No modificar la fecha y hora, solo generar QR y determinar estado
            generateQRAndDetermineStatus(visit);

            // Guardar la visita en la base de datos
            Visit savedVisit = visitRepository.save(visit);
            return ResponseEntity.ok(savedVisit);
        }

        // 🔹 Generar QR y determinar estado sin modificar la fecha
        private void generateQRAndDetermineStatus(Visit visit) {
            // NO modificar la fecha y hora de la visita
            LocalDateTime visitDate = visit.getDateTime();
            LocalDateTime now = LocalDateTime.now();

            // Siempre generar el QR independientemente del estado
            visit.setQrCode(generateQRCode(visit));

            // Cambiar estado basado en la diferencia de tiempo
            long hoursDifference = java.time.Duration.between(now, visitDate).toHours();

            if (hoursDifference > 1) {
                visit.setStatus("PENDIENTE");
            } else if (hoursDifference >= -2) {
                visit.setStatus("EN_PROGRESO");
            } else {
                visit.setStatus("CADUCADO");
            }
        }

        // 🔹 Función para generar QR en Base64 con todos los datos necesarios
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

        // 🔹 Obtener todas las visitas del residente
        public List<Visit> getVisitsByResident(String token) {
            String residentEmail = jwtTokenProvider.getUsernameFromToken(token);
            Optional<Resident> residentOptional = residentRepository.findByEmail(residentEmail);

            if (residentOptional.isEmpty()) {
                return List.of();  // Devuelve lista vacía si no se encuentra el residente
            }

            return visitRepository.findByResidentId(residentOptional.get().getId()); // Buscamos las visitas del residente por su ID
        }

        // 🔹 Actualizar visita
        public ResponseEntity<?> updateVisit(String token, String visitId, Visit updatedVisit) {
            Optional<Visit> visitOptional = visitRepository.findById(visitId);
            if (!visitOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Visit visit = visitOptional.get();

            // Actualizar campos sin modificar la fecha y hora
            visit.setVisitorName(updatedVisit.getVisitorName());
            visit.setDescription(updatedVisit.getDescription());
            visit.setNumPeople(updatedVisit.getNumPeople());
            visit.setVehiclePlate(updatedVisit.getVehiclePlate());
            visit.setPassword(updatedVisit.getPassword());

            // IMPORTANTE: Mantener la fecha y hora exactamente como se recibió
            if (updatedVisit.getDateTime() != null) {
                visit.setDateTime(updatedVisit.getDateTime());
            }

            // Generar QR y determinar estado
            generateQRAndDetermineStatus(visit);

            visitRepository.save(visit);
            return ResponseEntity.ok(visit);
        }

        // 🔹 Eliminar una visita
        public ResponseEntity<?> deleteVisit(String token, String visitId) {
            Optional<Visit> visitOptional = visitRepository.findById(visitId);
            if (!visitOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            visitRepository.delete(visitOptional.get());
            return ResponseEntity.noContent().build();
        }

        // 🔹 Cancelar una visita
        public ResponseEntity<?> cancelVisit(String token, String visitId) {
            Optional<Visit> visitOptional = visitRepository.findById(visitId);
            if (!visitOptional.isPresent()) {
                return ResponseEntity.notFound().build();
            }

            Visit visit = visitOptional.get();
            visit.setStatus("CANCELADO");

            // Siempre generar el QR incluso cuando se cancela
            visit.setQrCode(generateQRCode(visit));

            visitRepository.save(visit);
            return ResponseEntity.ok(visit);
        }
    }

