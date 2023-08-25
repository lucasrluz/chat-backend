package com.backend.chatbackend.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.chatbackend.domains.user.UserDomain;
import com.backend.chatbackend.dtos.SignUpDTORequest;
import com.backend.chatbackend.dtos.SignUpDTOResponse;
import com.backend.chatbackend.models.UserModel;
import com.backend.chatbackend.repositories.UserRepository;
import com.backend.chatbackend.services.exceptions.EmailAlreadyRegisteredException;

import at.favre.lib.crypto.bcrypt.BCrypt;

@Service
public class AuthenticationService {
    private UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SignUpDTOResponse signup(SignUpDTORequest signUpDTORequest) throws Exception {
        UserDomain userDomain = UserDomain.validate(
            signUpDTORequest.getFirstName(),
            signUpDTORequest.getLastName(),
            signUpDTORequest.getEmail(),
            signUpDTORequest.getPassword()
        );

        Optional<UserModel> findUserModelResponse = this.userRepository.findByEmail(userDomain.getEmail());

        if (!findUserModelResponse.isEmpty()) {
            throw new EmailAlreadyRegisteredException();
        }

        String hashPassword = BCrypt.withDefaults().hashToString(12, userDomain.getPassword().toCharArray());

        UserModel userModel = new UserModel(
            signUpDTORequest.getFirstName(),
            signUpDTORequest.getLastName(),
            signUpDTORequest.getEmail(),
            hashPassword
        );

        UserModel saveUserModelResponse = this.userRepository.save(userModel);

        return new SignUpDTOResponse(saveUserModelResponse.getUserId().toString());
    }
}
