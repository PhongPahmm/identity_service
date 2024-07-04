/* (C)2024 */
package spring.usercrud.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "permission")
public class Permission {
    @Id
    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;
}
