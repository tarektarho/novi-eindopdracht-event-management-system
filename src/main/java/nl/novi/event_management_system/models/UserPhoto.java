package nl.novi.event_management_system.models;

import jakarta.persistence.*;

@Entity
@Table(name = "user_photos")
public class UserPhoto {
    @Id
    private String fileName;

    public UserPhoto(String fileName) {
        this.fileName = fileName;
    }

    public UserPhoto() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
