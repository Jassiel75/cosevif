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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Permitir CORS
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/auth/resident/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/admin/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()

                        .requestMatchers(HttpMethod.POST, "/auth/guard/login").permitAll()  // 🔥 Agregado para el guardia
                        .requestMatchers("/guard/**").hasAuthority("GUARDIA")

                        // 🔥 Rutas accesibles solo para ADMIN
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .requestMatchers("/admin/houses/**").hasAuthority("ADMIN")
                        .requestMatchers("/admin/guard/**").hasAuthority("ADMIN")

                        // 🔥 Rutas accesibles solo para RESIDENT
                        .requestMatchers(HttpMethod.GET, "/auth/resident/profile").hasAuthority("RESIDENT")
                        .requestMatchers("/resident/**").hasAuthority("RESIDENT")
                        .requestMatchers(HttpMethod.POST, "/resident/visit").hasAuthority("RESIDENT")

                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/admin/houses").hasAuthority("ADMIN")

                        // 🔥 Permitir acceso público a imágenes
                        .requestMatchers("/uploads/**").permitAll()

                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 🔥 Agregar configuración de CORS
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOriginPatterns(List.of("*")); // ✅ Permite todas las IPs (incluido móvil, web, etc.)
        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("Authorization", "Content-Type", "Access-Control-Allow-Origin"));
        corsConfig.setExposedHeaders(List.of("Authorization")); // ✅ Para permitir ver el token en frontend si es necesario
        corsConfig.setAllowCredentials(true); // ⚠️ Importante si usas cookies o sesiones (aunque en tu caso no es obligatorio)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
