package spring.usercrud.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    int id;
    String username;
    String firstName;
    String lastName;
    String dateOfBirth;
    Set<RoleResponse> roles;
}
