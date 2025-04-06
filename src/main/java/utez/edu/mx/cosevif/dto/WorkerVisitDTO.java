package utez.edu.mx.cosevif.dto;

import utez.edu.mx.cosevif.model.WorkerVisit;

public class WorkerVisitDTO {
    private WorkerVisit visit;
    private String residentName;
    private int houseNumber;

    public WorkerVisitDTO(WorkerVisit visit, String residentName, int houseNumber) {
        this.visit = visit;
        this.residentName = residentName;
        this.houseNumber = houseNumber;
    }

    // Getters y setters
    public WorkerVisit getVisit() {
        return visit;
    }

    public void setVisit(WorkerVisit visit) {
        this.visit = visit;
    }

    public String getResidentName() {
        return residentName;
    }

    public void setResidentName(String residentName) {
        this.residentName = residentName;
    }

    public int getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(int houseNumber) {
        this.houseNumber = houseNumber;
    }
}
