package com.backend.chatbackend.util.builders;

import java.util.UUID;

import com.backend.chatbackend.models.UserModel;

public class UserModelBuilder {
    public static UserModel createWithUserId() {
        return new UserModel(UUID.randomUUID(), "foo", "bar", "foobar@gmail.com", "123456");
    }

    public static UserModel createWithEmptyUserId() {
        return new UserModel("foo", "bar", "foobar@gmail.com", "123456");
    }
}