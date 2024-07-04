/* (C)2024 */
package spring.usercrud.service;

import java.util.HashSet;
import java.util.List;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import spring.usercrud.dto.request.RoleRequest;
import spring.usercrud.dto.response.RoleResponse;
import spring.usercrud.mapper.RoleMapper;
import spring.usercrud.repository.PermissionRepository;
import spring.usercrud.repository.RoleRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest request) {
        var role = roleMapper.toRole(request);

        var permissions = permissionRepository.findAllById(request.getPermissions());
        role.setPermissions(new HashSet<>(permissions));

        role = roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAllRole() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
