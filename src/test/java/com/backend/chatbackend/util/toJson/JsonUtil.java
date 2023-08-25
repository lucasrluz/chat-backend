package com.backend.chatbackend.util.toJson;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JsonUtil {
    public static String asJsonString(final Object obj) {
        try {
          return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
    }
}
