package nl.novi.event_management_system.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_photos")
@Data
@NoArgsConstructor
public class UserPhoto {

    @Id
    private String fileName;


    public UserPhoto(String fileName) {
        this.fileName = fileName;
    }
}
