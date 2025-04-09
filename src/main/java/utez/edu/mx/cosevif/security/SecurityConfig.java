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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenFilter jwtTokenFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/resident/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/admin/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/guard/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()

                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()

                        // Acceso solo para ADMIN
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/admin/houses/**").hasAuthority("ADMIN")
                        .requestMatchers("/admin/guard/**").hasAuthority("ADMIN")

                        // Acceso solo para GUARDIA
                        .requestMatchers("/guard/**").hasAuthority("GUARDIA")

                        // Acceso solo para RESIDENT
                        .requestMatchers("/resident/**").hasAuthority("RESIDENT")
                        .requestMatchers(HttpMethod.GET, "/auth/resident/profile").hasAuthority("RESIDENT")
                        .requestMatchers(HttpMethod.PUT, "/auth/resident/profile").hasAuthority("RESIDENT")
                        .requestMatchers(HttpMethod.GET, "/resident/workerVisits").hasAuthority("RESIDENT")
                        .requestMatchers(HttpMethod.POST, "/resident/workerVisits").hasAuthority("RESIDENT")

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // 🌐 Orígenes específicos permitidos
        corsConfig.setAllowedOrigins(List.of(
                "http://localhost:5173",
                "http://cosevif-frontend.s3-website-us-east-1.amazonaws.com"
        ));

        // 🟡 También aceptar todos los orígenes (solo para pruebas, se recomienda quitar en producción)
        corsConfig.addAllowedOriginPattern("*");

        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
