/* (C)2024 */
package spring.usercrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.usercrud.entity.Role;

public interface RoleRepository extends JpaRepository<Role, String> {}
