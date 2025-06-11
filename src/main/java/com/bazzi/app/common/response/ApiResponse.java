package com.bazzi.app.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private int statusCode;
    private String success;
    private String message;
    private T data;

    private static final int SUCCESS = 200;

    public static <T> ApiResponse<T> success(String message, T data){
        return new ApiResponse<T>(SUCCESS, "true", message, data);
    }
}
