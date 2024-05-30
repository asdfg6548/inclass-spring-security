package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.constant.UserRole;
import ac.su.inclassspringsecurity.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    //인터페이스(구현체 Bean 알아서 주입)
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("User Create Test")
    public void create() {
        User newUser = new User();
        newUser.setUsername("test");
        newUser.setPassword(
                passwordEncoder.encode("test")
        );
        newUser.setEmail("test@email.com");
        newUser.setRole(UserRole.ADMIN);
        User savedUser=userRepository.save(newUser);
        System.out.println(savedUser);
    }

    @Test
    @DisplayName("email 필드 유저 검색")
    public void findByEmail() {
        create();
        Optional<User> foundUser= userRepository.findByEmail("test@email.com");
        assert foundUser.isPresent();
        System.out.println("유저 검색 성공");

        foundUser= userRepository.findByEmail("test44444@email.com");
        assert foundUser.isEmpty();
        System.out.println("없는 유저 검색 결과 반환");
    }

    @Test
    @DisplayName("유저네임 중복 검사 테스트")
    public void duplicateUsername() {
        create();
        boolean exists = userRepository.existsByUsername("test");
        System.out.println(exists);
        assert (exists);
        exists = userRepository.existsByUsername("test1");
        System.out.println(exists);
        assert (!exists);
    }

    @Test
    @DisplayName("유저 중복 테스트 (email)")
    public void existsByEmail() {
        create();
        boolean exists = userRepository.existsByEmail("test@mail.com");
        System.out.println(exists);

    }

    @Test
    public void duplicateUser() {
        create();
        User user = new User();
        user.setUsername("test");
        user.setPassword(
                passwordEncoder.encode("test")
        );
        user.setEmail("test@email.com");
        user.setRole(UserRole.ADMIN);
        assert userRepository.existsByUsername(user.getUsername());
        assert userRepository.existsByEmail(user.getEmail());
        }

    }
