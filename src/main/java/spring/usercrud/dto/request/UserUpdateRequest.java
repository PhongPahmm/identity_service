/* (C)2024 */
package spring.usercrud.dto.request;

import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String username;
    String password;
    String firstName;
    String lastName;
    String dateOfBirth;
    List<String> roles;
}
