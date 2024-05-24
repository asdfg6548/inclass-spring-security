package ac.su.inclassspringsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity  // URL 요청에 대한 Spring Security 동작 활성화
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(
                (authorizeHttpRequests) ->
                    authorizeHttpRequests.requestMatchers(
                        // Apache Ant 스타일 패턴을 사용해 URL 매칭 정의
                        new AntPathRequestMatcher(
                            "/**"   // 모든 URL 패턴에 대해 허용
                        )
                    // ).denyAll()
                    ).permitAll()
            )
        ;
        return http.build();
    }

    // passwordEncoder 빈 등록
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}