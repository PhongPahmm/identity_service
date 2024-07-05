/* (C)2024 */
package spring.usercrud.controller;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import spring.usercrud.dto.request.ApiResponse;
import spring.usercrud.dto.request.RoleRequest;
import spring.usercrud.dto.response.RoleResponse;
import spring.usercrud.service.RoleService;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.<List<RoleResponse>>builder().data(roleService.getAllRole()).build();
    }

    @PostMapping
    public ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        var role = roleService.createRole(request);
        return ApiResponse.<RoleResponse>builder().data(role).build();
    }

    @DeleteMapping
    public ApiResponse<Void> deleteRole(String role) {
        roleService.deleteRole(role);
        return ApiResponse.<Void>builder().build();
    }
}
