package utez.edu.mx.cosevif.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/resident/login").permitAll() // Permitir login sin autenticación
                        .requestMatchers(HttpMethod.POST, "/auth/admin/login").permitAll() // Login admin permitido
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll() // Permitir registro

                        // 🔥 Rutas accesibles solo para ADMIN
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/admin/houses/**").hasAuthority("ADMIN")
                        .requestMatchers("/admin/guards/**").hasAuthority("ADMIN")

                        // 🔥 Rutas accesibles solo para RESIDENT
                        .requestMatchers(HttpMethod.GET, "/auth/resident/profile").hasAuthority("RESIDENT")
                        .requestMatchers("/resident/**").hasAuthority("RESIDENT")

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll() // Permitir login
                        .requestMatchers(HttpMethod.POST, "/admin/houses").hasAuthority("ADMIN")  // ✅ Permitir solo a ADMIN

                        // 🔥 Permitir acceso público a imágenes
                        .requestMatchers("/uploads/**").permitAll()  // 🔥 Permitir acceso público a imágenes

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class); // 🔥 Agregar filtro JWT

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}