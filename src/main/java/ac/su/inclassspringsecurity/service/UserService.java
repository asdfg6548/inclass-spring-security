package ac.su.inclassspringsecurity.service;

import ac.su.inclassspringsecurity.constant.UserRole;
import ac.su.inclassspringsecurity.domain.SpringUser;
import ac.su.inclassspringsecurity.domain.User;
import ac.su.inclassspringsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    //로그인 전용 메서드 Override
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> registeredUSer= userRepository.findByEmail(email);
        if (registeredUSer.isEmpty()){
            throw new UsernameNotFoundException(email);
        }
//        User foundUser = registeredUSer.get();
//        return new SpringUser(
//                foundUser.getUsername(),
//                foundUser.getPassword(),
//                new ArrayList<>()
//        );
        return SpringUser.getSpringUserDetails(registeredUSer.get());
    }

    //CRUD 기능 구현
    public void create(
            String username,
            String password1,
            String email
    ) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(
                passwordEncoder.encode(password1)
        );
        newUser.setEmail(email);
        newUser.setRole(UserRole.USER);

        validateDuplicateUser(newUser);
        User savedUser=userRepository.save(newUser);
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
