/* (C)2024 */
package spring.usercrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.usercrud.entity.Permission;

public interface PermissionRepository extends JpaRepository<Permission, String> {}
