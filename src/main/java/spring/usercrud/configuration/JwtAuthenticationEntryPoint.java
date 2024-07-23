/* (C)2024 */
package spring.usercrud.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import spring.usercrud.dto.request.ApiResponse;
import spring.usercrud.exception.ErrorCode;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException {

        ErrorCode errorCode = ErrorCode.UNAUTHENTICATED;

        response.setStatus(errorCode.getHttpStatusCode().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ApiResponse<?> apiResponse =
                ApiResponse.builder()
                        .code(errorCode.getErrorCode())
                        .message(errorCode.getErrorMessage())
                        .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getWriter(), apiResponse);
        response.flushBuffer();
    }
}
