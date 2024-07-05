package spring.usercrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.usercrud.entity.InvalidatedToken;

@Repository
public interface LogoutRepository extends JpaRepository<InvalidatedToken, String> {
}
