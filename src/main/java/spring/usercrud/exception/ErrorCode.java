/* (C)2024 */
package spring.usercrud.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(1111, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1010, "User already existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1011, "User not found", HttpStatus.BAD_REQUEST),
    USER_NAME_INVALID(1012, "User name must be at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1013, "Password must be at least 6 characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1014, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1015, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DATE_OF_BIRTH(1016, "Your age must equal or greater than 18", HttpStatus.BAD_REQUEST),
    ;
    private final int errorCode;
    private final String errorMessage;
    private final HttpStatusCode httpStatusCode;

    ErrorCode(int errorCode, String errorMessage, HttpStatusCode httpStatusCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatusCode = httpStatusCode;
    }
}
