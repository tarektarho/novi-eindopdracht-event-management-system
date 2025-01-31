package nl.novi.event_management_system.mappers;

import nl.novi.event_management_system.dtos.userDtos.UserCreateDTO;
import nl.novi.event_management_system.dtos.userDtos.UserResponseDTO;
import nl.novi.event_management_system.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class UserMapper {

    public static UserResponseDTO toUserResponseDTO(User user) {

        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setUsername(user.getUsername());
        userResponseDTO.setEmail(user.getEmail());
        userResponseDTO.setPassword(user.getPassword());
        userResponseDTO.setEnabled(user.getEnabled());
        userResponseDTO.setRoles(user.getRoles());

        if(user.getUserPhoto() != null) {
            userResponseDTO.setUserPhoto(user.getUserPhoto());
        }

        return userResponseDTO;
    }

    public static List<UserResponseDTO> toUserResponseDTOList(List<User> users) {

        return users.stream()
                .map(UserMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }

    public static User toUserEntity(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(userCreateDTO.getPassword());
        user.setEnabled(userCreateDTO.getEnabled());
        return user;
    }

    public static User toUserEntity(UserResponseDTO userResponseDTO) {
        User user = new User();
        user.setUsername(userResponseDTO.getUsername());
        user.setEmail(userResponseDTO.getEmail());
        user.setPassword(userResponseDTO.getPassword());
        user.setEnabled(userResponseDTO.getEnabled());
        return user;
    }
}