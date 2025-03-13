package utez.edu.mx.cosevif.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "workerVisits")
public class WorkerVisit {

    @Id
    private String id;
    private String residentId;
    private String houseId;
    private String workerName;
    private int age;
    private String address;
    private String inePhoto; // 🔹 Foto de la INE en formato Base64
    private LocalDateTime dateTime;

    public WorkerVisit() {
    }

    public WorkerVisit(String id, String residentId, String houseId, String workerName, int age, String address, String inePhoto, LocalDateTime dateTime) {
        this.id = id;
        this.residentId = residentId;
        this.houseId = houseId;
        this.workerName = workerName;
        this.age = age;
        this.address = address;
        this.inePhoto = inePhoto;
        this.dateTime = dateTime;
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

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInePhoto() {
        return inePhoto;
    }

    public void setInePhoto(String inePhoto) {
        this.inePhoto = inePhoto;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
