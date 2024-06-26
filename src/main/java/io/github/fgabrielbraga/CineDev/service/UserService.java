package io.github.fgabrielbraga.CineDev.service;

import io.github.fgabrielbraga.CineDev.dto.input.PasswordInputDTO;
import io.github.fgabrielbraga.CineDev.dto.input.UserInputDTO;
import io.github.fgabrielbraga.CineDev.dto.output.UserOutputDTO;
import io.github.fgabrielbraga.CineDev.exceptions.InvalidParameterException;
import io.github.fgabrielbraga.CineDev.exceptions.ResourceNotFoundException;
import io.github.fgabrielbraga.CineDev.model.User;
import io.github.fgabrielbraga.CineDev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public UserOutputDTO findById(UUID uuid) {
        Optional<User> userOpt = userRepository.findById(uuid);
        return userOpt.map(UserOutputDTO::ofUser).orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, usuário não encontrado. Tente novamente."));
    }

    public UserOutputDTO findByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt.map(UserOutputDTO::ofUser).orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, usuário não encontrado. Tente novamente."));
    }

    public List<UserOutputDTO> findAll() {
        List<User> users = userRepository.findAll();
        return users.stream().map(UserOutputDTO::ofUser).toList();
    }

    public List<UserOutputDTO> findTop1000ByNameAndEmailAndCpf(
            String name, String email, String cpf) {
        List<User> users = userRepository
                .findTop1000ByNameAndEmailAndCpf(name, email, cpf);
        return users.stream().map(UserOutputDTO::ofUser).toList();
    }

    public UserOutputDTO save(UserInputDTO userDTO) {
        User user = UserInputDTO.parseUser(userDTO);
        String passwordEncoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);
        resetIdentifier(user);
        User userSaved = userRepository.save(user);
        return UserOutputDTO.ofUser(userSaved);
    }

    public UserOutputDTO update(UserInputDTO userDTO) {
        Optional<User> userOpt = userRepository.findById(userDTO.getUuid());
        return userOpt.map(userFound -> {
            userFound.setName(userDTO.getName());
            userFound.setCpf(userDTO.getCpf());
            userFound.setRole(userDTO.getRole());
            userFound.setEmail(userDTO.getEmail());
            userFound.setPhoneNumber(userDTO.getPhoneNumber());
            User userSaved = userRepository.save(userFound);
            return UserOutputDTO.ofUser(userSaved);
        }).orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, usuário não encontrado. Tente novamente."));
    }

    public UserOutputDTO updateProfile(UserInputDTO userDTO) {
        Optional<User> userOpt = userRepository.findById(userDTO.getUuid());
        return userOpt.map(userFound -> {
            userFound.setName(userDTO.getName());
            userFound.setCpf(userDTO.getCpf());
            userFound.setEmail(userDTO.getEmail());
            userFound.setPhoneNumber(userDTO.getPhoneNumber());
            User userSaved = userRepository.save(userFound);
            return UserOutputDTO.ofUser(userSaved);
        }).orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, usuário não encontrado. Tente novamente."));
    }

    public UserOutputDTO updateProfilePicture(UserInputDTO userDTO) {
        Optional<User> userOpt = userRepository.findById(userDTO.getUuid());
        return userOpt.map(userFound -> {
            userFound.setProfilePicture(userDTO.getProfilePicture());
            User userSaved = userRepository.save(userFound);
            return UserOutputDTO.ofUser(userSaved);
        }).orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, usuário não encontrado. Tente novamente."));
    }

    public UserOutputDTO updatePassword(UUID uuid, PasswordInputDTO passwordDTO) {
        Optional<User> userOpt = userRepository.findById(uuid);
        return userOpt.map(userFound -> {
            if(passwordDTO.getNewPassword().equals(passwordDTO.getConfirmPassword())) {
                if(passwordEncoder.matches(
                        passwordDTO.getCurrentPassword(),userFound.getPassword())) {
                    String passwordEncoded = passwordEncoder.encode(passwordDTO.getNewPassword());
                    userFound.setPassword(passwordEncoded);
                    User userSaved = userRepository.save(userFound);
                    return UserOutputDTO.ofUser(userSaved);
                }
                throw new InvalidParameterException("Desculpe, a senha inserida está incorreta. Por favor, tente novamente.");
            }
            throw new InvalidParameterException("As senhas fornecidas não conferem. Certifique-se de digitá-las corretamente e tente novamente.");
        }).orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, usuário não encontrado. Tente novamente."));
    }

    public void deleteById(UUID uuid) {
        Optional<User> userOpt = userRepository.findById(uuid);
        userOpt.orElseThrow(() ->
                new ResourceNotFoundException("Desculpe, usuário não encontrado. Tente novamente."));
        userRepository.deleteById(uuid);
    }

    @Transactional
    public void disable(UUID uuid) {
        userRepository.disableUserById(uuid);
    }

    @Transactional
    public void enable(UUID uuid) {
        userRepository.enableUserById(uuid);
    }

    private void resetIdentifier(User user) {
        user.setUuid(null);
    }
}
