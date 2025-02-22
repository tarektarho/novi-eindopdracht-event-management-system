package nl.novi.event_management_system.dtos.userDtos;

import nl.novi.event_management_system.models.Role;
import nl.novi.event_management_system.models.UserPhoto;

import java.util.HashSet;
import java.util.Set;

public class UserProfileDTO {
        private String username;
        private String email;
        private Set<Role> roles = new HashSet<>();
        private UserPhoto userPhoto;

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public Set<Role> getRoles() {
                return roles;
        }

        public void setRoles(Set<Role> roles) {
                this.roles = roles;
        }

        public UserPhoto getUserPhoto() {
                return userPhoto;
        }

        public void setUserPhoto(UserPhoto userPhoto) {
                this.userPhoto = userPhoto;
        }

        @Override
        public String toString() {
                return "UserProfileDTO{" +
                        "username='" + username + '\'' +
                        ", email='" + email + '\'' +
                        ", roles=" + roles +
                        ", userPhoto=" + userPhoto +
                        '}';
        }
}
