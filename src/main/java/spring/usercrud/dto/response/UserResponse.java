/* (C)2024 */
package spring.usercrud.dto.response;

import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
