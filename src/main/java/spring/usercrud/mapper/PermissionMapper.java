package spring.usercrud.mapper;

import org.mapstruct.Mapper;
import spring.usercrud.dto.request.PermissionRequest;
import spring.usercrud.dto.response.PermissionResponse;
import spring.usercrud.entity.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);
    PermissionResponse toPermissionResponse(Permission permission);
}
