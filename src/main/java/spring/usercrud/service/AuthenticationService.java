/* (C)2024 */
package spring.usercrud.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import spring.usercrud.dto.request.AuthenticationRequest;
import spring.usercrud.dto.request.IntrospectRequest;
import spring.usercrud.dto.request.LogoutRequest;
import spring.usercrud.dto.request.RefreshRequest;
import spring.usercrud.dto.response.AuthenticationResponse;
import spring.usercrud.dto.response.IntrospectResponse;
import spring.usercrud.dto.response.LogoutResponse;
import spring.usercrud.dto.response.RefreshResponse;
import spring.usercrud.entity.InvalidatedToken;
import spring.usercrud.entity.User;
import spring.usercrud.exception.AppException;
import spring.usercrud.exception.ErrorCode;
import spring.usercrud.repository.LogoutRepository;
import spring.usercrud.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    LogoutRepository logoutRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws JOSEException {
        var user =
                userRepository
                        .findByUsername(request.getUsername())
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(user);
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return IntrospectResponse.builder().valid(isValid).build();
    }

    public LogoutResponse logout(LogoutRequest request) throws ParseException, JOSEException {
        var signedToken = verifyToken(request.getToken(), true);
        String jti = signedToken.getJWTClaimsSet().getJWTID();
        Date expirationTime = signedToken.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = new InvalidatedToken(jti, expirationTime);
        logoutRepository.save(invalidatedToken);
        return LogoutResponse.builder().logout(true).build();
    }

    public RefreshResponse refresh(RefreshRequest request) throws ParseException, JOSEException {
        var signJWT = verifyToken(request.getToken(), true);
        String jti = signJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();
        InvalidatedToken invalidatedToken = new InvalidatedToken(jti, expiryTime);
        logoutRepository.save(invalidatedToken);
        var username = signJWT.getJWTClaimsSet().getSubject();
        var user =
                userRepository
                        .findByUsername(username)
                        .orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
        var token = generateToken(user);
        return RefreshResponse.builder().newToken(token).authenticated(true).build();
    }

    private SignedJWT verifyToken(String token, boolean isRefresh)
            throws ParseException, JOSEException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);
        SignedJWT signedJWT = SignedJWT.parse(token);
        Date expirationTime =
                isRefresh
                        ? new Date(
                                signedJWT
                                        .getJWTClaimsSet()
                                        .getIssueTime()
                                        .toInstant()
                                        .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                                        .toEpochMilli())
                        : signedJWT.getJWTClaimsSet().getExpirationTime();
        var verified = signedJWT.verify(verifier);
        if (!(verified && expirationTime.after(new Date()))) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if (logoutRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    private String generateToken(User user) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet =
                new JWTClaimsSet.Builder()
                        .subject(user.getUsername())
                        .issuer("myDomain.com")
                        .issueTime(new Date())
                        .expirationTime(
                                new Date(
                                        Instant.now()
                                                .plus(VALID_DURATION, ChronoUnit.SECONDS)
                                                .toEpochMilli()))
                        .jwtID(String.valueOf(UUID.randomUUID()))
                        .claim("scope", buildScope(user))
                        .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));

        return jwsObject.serialize();
    }

    private String buildScope(User user) {
        StringJoiner scopeJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles()
                    .forEach(
                            role -> {
                                scopeJoiner.add("ROLE_" + role.getName());
                                if (!CollectionUtils.isEmpty(role.getPermissions()))
                                    role.getPermissions()
                                            .forEach(
                                                    permission ->
                                                            scopeJoiner.add(permission.getName()));
                            });
        }
        return scopeJoiner.toString();
    }
}
