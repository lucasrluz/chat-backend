package com.backend.chatbackend.unit.services;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.backend.chatbackend.dtos.SignUpDTORequest;
import com.backend.chatbackend.dtos.SignUpDTOResponse;
import com.backend.chatbackend.models.UserModel;
import com.backend.chatbackend.repositories.UserRepository;
import com.backend.chatbackend.services.AuthenticationService;
import com.backend.chatbackend.services.exceptions.EmailAlreadyRegisteredException;
import com.backend.chatbackend.util.builders.SignUpDTORequestBuilder;
import com.backend.chatbackend.util.builders.UserModelBuilder;

@ExtendWith(SpringExtension.class)
public class AuthenticationServiceTests {
    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void retornaUserId() throws Exception {
        Optional<UserModel> userModelMockFindByEmail = Optional.empty();
        BDDMockito.when(this.userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(userModelMockFindByEmail);

        UserModel userModelMockSave = UserModelBuilder.createWithUserId();
        BDDMockito.when(this.userRepository.save(ArgumentMatchers.any())).thenReturn(userModelMockSave);
    
        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithValidData();
        SignUpDTOResponse signUpDTOResponse = this.authenticationService.signup(signUpDTORequest);

        assertThat(signUpDTOResponse.getUserId()).isEqualTo(userModelMockSave.getUserId().toString());
    }

    @Test
    public void retornaException_EmailJaCadastrado() {
        Optional<UserModel> userModelMockFindByEmail = Optional.of(UserModelBuilder.createWithUserId());
        BDDMockito.when(this.userRepository.findByEmail(ArgumentMatchers.any())).thenReturn(userModelMockFindByEmail);

        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithValidData();

        assertThatExceptionOfType(EmailAlreadyRegisteredException.class)
            .isThrownBy(() -> this.authenticationService.signup(signUpDTORequest))
            .withMessage("Email already registered");
    }
}
