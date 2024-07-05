/* (C)2024 */
package spring.usercrud.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import spring.usercrud.dto.request.RoleRequest;
import spring.usercrud.dto.response.RoleResponse;
import spring.usercrud.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
