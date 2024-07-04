/* (C)2024 */
package spring.usercrud.entity;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = @JoinColumn(name = "role_name"),
            inverseJoinColumns = @JoinColumn(name = "permission_name"))
    Set<Permission> permissions;
}
