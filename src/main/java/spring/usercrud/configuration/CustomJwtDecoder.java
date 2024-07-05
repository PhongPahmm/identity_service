package spring.usercrud.configuration;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;
import spring.usercrud.dto.request.IntrospectRequest;
import spring.usercrud.exception.AppException;
import spring.usercrud.exception.ErrorCode;
import spring.usercrud.service.AuthenticationService;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class CustomJwtDecoder implements JwtDecoder {
    private final AuthenticationService authenticationService;
    private NimbusJwtDecoder jwtDecoder;

    @Value("${jwt.signerKey}")
    String SIGNER_KEY;
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            var response = authenticationService.introspect(IntrospectRequest.builder()
                            .token(token)
                    .build());
            if (!response.isValid()){
                throw new AppException(ErrorCode.UNAUTHENTICATED);
            }
        }catch (ParseException | JOSEException exception){
            throw new JwtException(exception.getMessage());
        }

        if (Objects.isNull(jwtDecoder)) {
            SecretKey secretKey = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
            jwtDecoder = NimbusJwtDecoder.withSecretKey(secretKey)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return jwtDecoder.decode(token);
    }
}
