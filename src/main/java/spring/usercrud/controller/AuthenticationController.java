/* (C)2024 */
package spring.usercrud.controller;

import com.nimbusds.jose.JOSEException;
import java.text.ParseException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import spring.usercrud.dto.request.*;
import spring.usercrud.dto.response.AuthenticationResponse;
import spring.usercrud.dto.response.IntrospectResponse;
import spring.usercrud.dto.response.LogoutResponse;
import spring.usercrud.dto.response.RefreshResponse;
import spring.usercrud.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
            throws JOSEException {
        var result = authenticationService.authenticate(request);
        return ApiResponse.<AuthenticationResponse>builder().data(result).build();
    }

    @PostMapping("introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws JOSEException, ParseException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().data(result).build();
    }

    @PostMapping("logout")
    ApiResponse<LogoutResponse> logout(@RequestBody LogoutRequest request)
            throws JOSEException, ParseException {
        var result = authenticationService.logout(request);
        return ApiResponse.<LogoutResponse>builder().data(result).build();
    }

    @PostMapping("refresh")
    ApiResponse<RefreshResponse> refresh(@RequestBody RefreshRequest request)
            throws JOSEException, ParseException {
        var result = authenticationService.refresh(request);
        return ApiResponse.<RefreshResponse>builder().data(result).build();
    }
}
