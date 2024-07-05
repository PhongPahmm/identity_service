package spring.usercrud.controller;

import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import spring.usercrud.dto.request.ApiResponse;
import spring.usercrud.dto.request.AuthenticationRequest;
import spring.usercrud.dto.request.IntrospectRequest;
import spring.usercrud.dto.request.LogoutRequest;
import spring.usercrud.dto.response.AuthenticationResponse;
import spring.usercrud.dto.response.IntrospectResponse;
import spring.usercrud.dto.response.LogoutResponse;
import spring.usercrud.service.AuthenticationService;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) throws JOSEException {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws JOSEException, ParseException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }
    @PostMapping("logout")
    ApiResponse<LogoutResponse> logout(@RequestBody LogoutRequest request) throws JOSEException, ParseException {
        var result = authenticationService.logout(request);
        return ApiResponse.<LogoutResponse>builder()
                .data(result)
                .build();
    }
}
