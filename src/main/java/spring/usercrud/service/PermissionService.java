/* (C)2024 */
package spring.usercrud.service;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import spring.usercrud.dto.request.PermissionRequest;
import spring.usercrud.dto.response.PermissionResponse;
import spring.usercrud.entity.Permission;
import spring.usercrud.mapper.PermissionMapper;
import spring.usercrud.repository.PermissionRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAllPermission() {
        return permissionRepository.findAll().stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public void deletePermission(String permission) {
        permissionRepository.deleteById(permission);
    }
}
