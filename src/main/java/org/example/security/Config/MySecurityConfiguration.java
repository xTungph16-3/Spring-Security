package org.example.security.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class MySecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = passwordEncoder();
        List<UserDetails> users = new ArrayList<>();

        // Tạo người dùng tung123
        UserDetails user = User.builder()
                .username("tung123")
                .password(encoder.encode("tung123")) // Mã hóa mật khẩu
                .roles("USER") // Thêm vai trò USER
                .build();
        users.add(user);

        // Tạo người dùng admin
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("admin")) // Mã hóa mật khẩu
                .roles("ADMIN") // Thêm vai trò ADMIN
                .build();
        users.add(admin);

        return new InMemoryUserDetailsManager(users);
    }

    @Bean
    public SecurityFilterChain authorization(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Chỉ cho phép ADMIN truy cập
                        .anyRequest().permitAll() // Cho phép tất cả các yêu cầu khác
                )
                .formLogin(login -> login
                        .loginProcessingUrl("/login") // Đường dẫn xử lý đăng nhập
                        .permitAll() // Cho phép tất cả người dùng truy cập trang đăng nhập
                )
                .logout(logout -> logout
                        .permitAll() // Cho phép tất cả người dùng đăng xuất
                );

        return httpSecurity.build();
    }
}