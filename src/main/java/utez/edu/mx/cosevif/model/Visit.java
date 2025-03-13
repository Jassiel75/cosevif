package utez.edu.mx.cosevif.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "visits")
public class Visit {
    @Id
    private String id;
    private String residentId; // 🔹 ID del residente que creó la visita
    private String houseId; // 🔹 ID de la casa asociada
    private LocalDateTime dateTime; // 🔹 Fecha y hora de la visita
    private int numPeople; // 🔹 Número de personas
    private String description;
    private String vehiclePlate; // 🔹 Placas del vehículo
    private String password; // 🔹 Clave para acceder (residente la proporciona)
    private String visitorName; // 🔹 Nombre del visitante (Opcional)
    private String status; // 🔹 Estado de la visita
    private String qrCode; // 🔹 Código QR generado para la visita

    public Visit() {
    }

    public Visit(String id, String residentId, String houseId, LocalDateTime dateTime, int numPeople, String description, String vehiclePlate, String password, String visitorName, String status, String qrCode, LocalDateTime qrGeneratedTime) {
        this.id = id;
        this.residentId = residentId;
        this.houseId = houseId;
        this.dateTime = dateTime;
        this.numPeople = numPeople;
        this.description = description;
        this.vehiclePlate = vehiclePlate;
        this.password = password;
        this.visitorName = visitorName;
        this.status = status;
        this.qrCode = qrCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResidentId() {
        return residentId;
    }

    public void setResidentId(String residentId) {
        this.residentId = residentId;
    }

    public String getHouseId() {
        return houseId;
    }

    public void setHouseId(String houseId) {
        this.houseId = houseId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }


}
