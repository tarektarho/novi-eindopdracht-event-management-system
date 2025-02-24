package nl.novi.event_management_system.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_photos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPhoto {
    @Id
    private String fileName;
}
