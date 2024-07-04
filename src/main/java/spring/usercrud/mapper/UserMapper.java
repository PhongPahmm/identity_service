package spring.usercrud.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import spring.usercrud.dto.request.UserCreationRequest;
import spring.usercrud.dto.request.UserUpdateRequest;
import spring.usercrud.dto.response.UserResponse;
import spring.usercrud.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);
    UserResponse toUserResponse(User user);
    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
