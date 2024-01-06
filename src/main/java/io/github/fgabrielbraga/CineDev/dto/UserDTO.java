package io.github.fgabrielbraga.CineDev.dto;

import io.github.fgabrielbraga.CineDev.model.User;
import io.github.fgabrielbraga.CineDev.model.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserDTO {

    private UUID uuid;
    private String name;
    private String email;
    private String password;
    private String cpf;
    private String phoneNumber;
    private String profilePicture;
    private Boolean disabled;
    private Boolean confirmed;
    private Role role = Role.CLIENT;
    private LocalDateTime createdAt;

    public UserDTO() {
    }

    public UserDTO(User user) {
        this.uuid = user.getUuid();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.cpf = user.getCpf();
        this.phoneNumber = user.getPhoneNumber();
        this.profilePicture = user.getProfilePicture();
        this.disabled = user.getDisabled();
        this.confirmed = user.getConfirmed();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }

    public static User convert(UserDTO userDTO) {
        User user = new User();
        user.setUuid(userDTO.getUuid());
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setCpf(userDTO.getCpf());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setProfilePicture(userDTO.getProfilePicture());
        user.setDisabled(userDTO.getDisabled());
        user.setConfirmed(userDTO.getConfirmed());
        user.setRole(userDTO.getRole());
        user.setCreatedAt(userDTO.getCreatedAt());
        return user;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
