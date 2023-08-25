package com.backend.chatbackend.util.builders;

import com.backend.chatbackend.dtos.SignUpDTORequest;

public abstract class SignUpDTORequestBuilder {
    public static SignUpDTORequest createWithValidData() {
        return new SignUpDTORequest("foo", "bar", "foobar@gmail.com", "123456");
    }

    public static SignUpDTORequest createWithEmptyFirstName() {
        return new SignUpDTORequest("", "bar", "foobar@gmail.com", "123456");
    }

    public static SignUpDTORequest createWithEmptyLastName() {
        return new SignUpDTORequest("foo", "", "foobar@gmail.com", "123456");
    }
    
    public static SignUpDTORequest createWithInvalidEmailFormat() {
        return new SignUpDTORequest("foo", "bar", "foobar@", "123456");
    }

    public static SignUpDTORequest createWithEmptyEmail() {
        return new SignUpDTORequest("foo", "bar", "", "123456");
    }

    public static SignUpDTORequest createWithEmptyPassword() {
        return new SignUpDTORequest("foo", "bar", "foobar@gmail.com", "");
    }
}
