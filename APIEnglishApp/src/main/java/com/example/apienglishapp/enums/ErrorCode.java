package com.example.apienglishapp.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1000, "User already existed", HttpStatus.BAD_REQUEST),
    USER_OK(1001, "OK", HttpStatus.OK),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    INVALID_USERNAME(1003, "Username's length must be at least 3 characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password's length must be at least 8 characters", HttpStatus.BAD_REQUEST),
    NULL_NAME(1005, "Name cannot be empty", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1006, "INVALID email address", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_NEW_WORD(1009, "front side and back side must be different", HttpStatus.BAD_REQUEST),
    NEW_WORD_NOT_FOUND(1010, "new word not found", HttpStatus.NOT_FOUND),
    INVALID_NAME_NEW_WORD(1011, "invalid new word's name", HttpStatus.BAD_REQUEST)
    ;
    private int code;
    private String message;
    private HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
