package spring.usercrud.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import spring.usercrud.dto.request.ApiResponse;
import spring.usercrud.dto.request.PermissionRequest;
import spring.usercrud.dto.response.PermissionResponse;
import spring.usercrud.service.PermissionService;

import java.util.List;

@RestController
@RequestMapping("permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAllPermission())
                .build();
    }

    @PostMapping
    public ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.createPermission(request))
                .build();
    }

    @DeleteMapping("/{permission}")
    public ApiResponse<Void> deletePermission(@PathVariable("permission") String permission) {
       permissionService.deletePermission(permission);
       return ApiResponse.<Void>builder().build();
    }
}
