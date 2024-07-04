package spring.usercrud.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import spring.usercrud.entity.Permission;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleRequest {
    String name;
    String description;
    Set<String> permissions;
}
