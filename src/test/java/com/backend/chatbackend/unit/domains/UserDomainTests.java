package com.backend.chatbackend.unit.domains;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;

import com.backend.chatbackend.domains.user.UserDomain;
import com.backend.chatbackend.domains.user.exception.InvalidUserDomainException;

public class UserDomainTests {
    @Test
    public void retornaUserDomain() throws InvalidUserDomainException {
        UserDomain userDomain = UserDomain.validate("foo", "bar", "foobar@gmail.com", "123456");

        assertThat(userDomain.getFirstName()).isEqualTo("foo");
        assertThat(userDomain.getLastName()).isEqualTo("bar");
        assertThat(userDomain.getEmail()).isEqualTo("foobar@gmail.com");
        assertThat(userDomain.getPassword()).isEqualTo("123456");
    }

    @Test
    public void retornaException_FirstNameInvalido() throws InvalidUserDomainException {
        assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> UserDomain.validate("", "bar", "foobar@gmail.com", "123456"))
            .withMessage("firstName: must not be blank");
    }

    @Test
    public void retornaException_LastNameInvalido() throws InvalidUserDomainException {
        assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> UserDomain.validate("foo", "", "foobar@gmail.com", "123456"))
            .withMessage("lastName: must not be blank");
    }

    @Test
    public void retornaException_EmailInvalido_ValorVazio() throws InvalidUserDomainException {
        assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> UserDomain.validate("foo", "bar", "", "123456"))
            .withMessage("email: must not be blank");
    }

    @Test
    public void retornaException_EmailInvalido_FormatoInvalido() throws InvalidUserDomainException {
        assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> UserDomain.validate("foo", "bar", "foobar@", "123456"))
            .withMessage("email: must be a well-formed email address");
    }

    @Test
    public void retornaException_PasswordInvalida() throws InvalidUserDomainException {
        assertThatExceptionOfType(InvalidUserDomainException.class)
            .isThrownBy(() -> UserDomain.validate("foo", "bar", "foobar@gmail.com", ""))
            .withMessage("password: must not be blank");
    } 
}
