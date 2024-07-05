/* (C)2024 */
package spring.usercrud.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "username")
    String username;

    @Column(name = "password")
    String password;

    @Column(name = "firstname")
    String firstName;

    @Column(name = "lastname")
    String lastName;

    @Column(name = "dob")
    LocalDate dateOfBirth;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;
}
