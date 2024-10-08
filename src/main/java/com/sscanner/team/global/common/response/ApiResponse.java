package com.sscanner.team.global.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ApiResponse<T> {

    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> ok(int status, T data, String message) {

        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> ok(T data, String message) {

        return new ApiResponse<>(200, message, data);
    }

    public static ApiResponse<?> ok(String message) {

        return new ApiResponse<>(200, message, null);
    }

    public static ApiResponse<?> error(int code, String message) {
        return new ApiResponse<>(code, message, null);
    }



    public ApiResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}