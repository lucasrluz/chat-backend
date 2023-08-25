package com.backend.chatbackend.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import com.backend.chatbackend.dtos.SignUpDTORequest;
import com.backend.chatbackend.models.UserModel;
import com.backend.chatbackend.repositories.UserRepository;
import com.backend.chatbackend.util.builders.SignUpDTORequestBuilder;
import com.backend.chatbackend.util.builders.UserModelBuilder;
import com.backend.chatbackend.util.toJson.JsonUtil;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import org.json.JSONObject;

@SpringBootTest
@AutoConfigureMockMvc
public class SignUpApiTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void retornaCreatedEUserId() throws Exception {
        this.userRepository.deleteAll();

        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithValidData();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/auth/signup")
            .contentType("application/json")
            .content(JsonUtil.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(201);

        String userId = new JSONObject(response.getContentAsString()).getString("userId");
        Optional<UserModel> findUserModel = this.userRepository.findById(UUID.fromString(userId));
        
        assertThat(findUserModel.isEmpty()).isEqualTo(false);
        assertThat(findUserModel.get().getFirstName()).isEqualTo("foo");
        assertThat(findUserModel.get().getLastName()).isEqualTo("bar");
        assertThat(findUserModel.get().getEmail()).isEqualTo("foobar@gmail.com");

        Result verifyPassword = BCrypt.verifyer().verify(
            signUpDTORequest.getPassword().toCharArray(),
            findUserModel.get().getPassword()
        );

        assertThat(verifyPassword.verified).isEqualTo(true);

        this.userRepository.deleteAll();
    }

    @Test
    public void retornaBadRequest_EmailJaCadastrado() throws Exception {
        this.userRepository.deleteAll();

        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithValidData();

        this.userRepository.save(UserModelBuilder.createWithEmptyUserId());

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/auth/signup")
            .contentType("application/json")
            .content(JsonUtil.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        assertThat(response.getContentAsString()).isEqualTo("Email already registered");

        this.userRepository.deleteAll();
    }

    @Test
    public void retornaBadRequest_FirstNameComValorVazio() throws Exception {
        this.userRepository.deleteAll();

        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithEmptyFirstName();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/auth/signup")
            .contentType("application/json")
            .content(JsonUtil.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        
        assertThat(response.getContentAsString()).isEqualTo("firstName: must not be blank");

        this.userRepository.deleteAll();
    }

    @Test
    public void retornaBadRequest_LastNameComValorVazio() throws Exception {
        this.userRepository.deleteAll();

        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithEmptyLastName();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/auth/signup")
            .contentType("application/json")
            .content(JsonUtil.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        
        assertThat(response.getContentAsString()).isEqualTo("lastName: must not be blank");

        this.userRepository.deleteAll();
    }

    @Test
    public void retornaBadRequest_EmailComFormatoInvalido() throws Exception {
        this.userRepository.deleteAll();

        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithInvalidEmailFormat();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/auth/signup")
            .contentType("application/json")
            .content(JsonUtil.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        
        assertThat(response.getContentAsString()).isEqualTo("email: must be a well-formed email address");

        this.userRepository.deleteAll();
    }

    @Test
    public void retornaBadRequest_EmailComValorVazio() throws Exception {
        this.userRepository.deleteAll();

        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithEmptyEmail();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/auth/signup")
            .contentType("application/json")
            .content(JsonUtil.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        
        assertThat(response.getContentAsString()).isEqualTo("email: must not be blank");

        this.userRepository.deleteAll();
    }

    @Test
    public void retornaBadRequest_PasswordComValorVazio() throws Exception {
        this.userRepository.deleteAll();

        SignUpDTORequest signUpDTORequest = SignUpDTORequestBuilder.createWithEmptyPassword();

        MockHttpServletResponse response = this.mockMvc.perform(
            post("/api/auth/signup")
            .contentType("application/json")
            .content(JsonUtil.asJsonString(signUpDTORequest))
        ).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(400);
        
        assertThat(response.getContentAsString()).isEqualTo("password: must not be blank");

        this.userRepository.deleteAll();
    }
}
