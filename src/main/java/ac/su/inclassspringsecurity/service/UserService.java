package ac.su.inclassspringsecurity.service;

import ac.su.inclassspringsecurity.constant.UserRole;
import ac.su.inclassspringsecurity.domain.User;
import ac.su.inclassspringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void create() {
        User newUser = new User();
        newUser.setUsername("test");
        newUser.setPassword(
                passwordEncoder.encode("test")
        );
        newUser.setEmail("test@email.com");
        newUser.setRole(UserRole.ADMIN);
        validateDuplicateUser(newUser);
        User savedUser=userRepository.save(newUser);
        System.out.println(savedUser);
    }

    // 유저 중복 Validation 체크 (username, email) 후 중복 시 throw Exception
    public void validateDuplicateUser(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new IllegalStateException("이미 존재하는 유저입니다.");
        }
        if (existsByEmail(user.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }
    }
    //유저 네임 중복 체크
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    // 이메일 중복 체크
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
