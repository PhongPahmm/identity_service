/* (C)2024 */
package spring.usercrud.service;

import java.util.HashSet;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring.usercrud.constant.PredefinedRole;
import spring.usercrud.dto.request.UserCreationRequest;
import spring.usercrud.dto.request.UserUpdateRequest;
import spring.usercrud.dto.response.UserResponse;
import spring.usercrud.entity.Role;
import spring.usercrud.entity.User;
import spring.usercrud.exception.AppException;
import spring.usercrud.exception.ErrorCode;
import spring.usercrud.mapper.UserMapper;
import spring.usercrud.repository.RoleRepository;
import spring.usercrud.repository.UserRepository;

@RequiredArgsConstructor
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableMethodSecurity
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        var role = new HashSet<Role>();
        roleRepository.findById(PredefinedRole.ROLE_USER).ifPresent(role::add);
        user.setRoles(role);
        try{
            user = userRepository.save(user);
        }catch (DataIntegrityViolationException e){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext().getAuthentication().getName();
        User user =
                userRepository
                        .findByUsername(context)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getAllUsers() {
        log.info("in method getAllUsers");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse getUserById(int id) {
        log.info("in method getUserById");
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(int id, UserUpdateRequest request) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var role = roleRepository.findAllById(request.getRoles());
        user.setRoles(new HashSet<>(role));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(int id) {
        User user =
                userRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        userRepository.delete(user);
    }
}
