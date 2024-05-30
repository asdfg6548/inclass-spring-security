package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
//    Optional<User> findByUsername(String username);

    //중복 유저 체크를 위한 기본 쿼리( username, email )
    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);
    //로그인 인증시 유저 데이터 조회 가능(pass, role)

    boolean existsByEmail(String email); //로그인 인증 시 유저 유무 확인 가능
}
