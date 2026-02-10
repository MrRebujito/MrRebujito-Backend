package mrRebujito.MrRebujito.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ==========================================
                // RUTAS PÚBLICAS
                // ==========================================
                // Login
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                
                // Registro público según requisitos 1a
                .requestMatchers(HttpMethod.POST, 
                    "/socio", 
                    "/caseta").permitAll()
                
                // Listar ayuntamientos (requisito 1b)
                .requestMatchers(HttpMethod.GET, 
                    "/ayuntamiento", 
                    "/ayuntamiento/{id}").permitAll()
                
                // Listar casetas y cartas (requisito 1c)
                .requestMatchers(HttpMethod.GET, 
                    "/caseta", 
                    "/caseta/{id}",
                    "/solicitud",
                    "/solicitud/{id}",
                    "/caseta/carta/{id}",
                    "/producto",
                    "/producto/{id}").permitAll()
                
                // Listar socios (público según tu configuración)
                .requestMatchers(HttpMethod.GET, 
                    "/socio",
                    "/socio/{id}").permitAll()
                
                // Swagger
                .requestMatchers(
                    "/swagger-ui/**", 
                    "/v3/api-docs/**").permitAll()
                
                // ==========================================
                // RUTAS QUE REQUIEREN AUTENTICACIÓN
                // ==========================================
                // ¡IMPORTANTE! actorLogin debe ser AUTENTICADO, no público
                .requestMatchers(HttpMethod.GET, "/actorLogin").authenticated()
                
                // ==========================================
                // ADMINISTRADOR
                // ==========================================
                .requestMatchers("/administrador/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/administrador").hasAuthority("ADMIN")
                
                // Solo ADMIN puede crear/editar/borrar ayuntamientos por ID
                .requestMatchers(HttpMethod.POST, "/ayuntamiento").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/ayuntamiento/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/ayuntamiento/{id}").hasAuthority("ADMIN")
                
                // ==========================================
                // AYUNTAMIENTO
                // ==========================================
                // Editar propio perfil (sin ID)
                .requestMatchers(HttpMethod.PUT, "/ayuntamiento").hasAuthority("AYUNTAMIENTO")
                .requestMatchers(HttpMethod.DELETE, "/ayuntamiento").hasAuthority("AYUNTAMIENTO")
                
                // Listar solicitudes del ayuntamiento
                .requestMatchers(HttpMethod.GET, "/solicitud/ayuntamiento").hasAuthority("AYUNTAMIENTO")
                
                // Aceptar/rechazar solicitudes
                .requestMatchers(HttpMethod.PUT, 
                    "/solicitud/aceptar/{id}",
                    "/solicitud/{id}/rechazar").hasAuthority("AYUNTAMIENTO")
                
                // ==========================================
                // CASETA
                // ==========================================
                // Gestionar solicitudes
                .requestMatchers(HttpMethod.POST, "/solicitud").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.DELETE, "/solicitud/{id}").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.GET, "/solicitud/Caseta").hasAuthority("CASETA")
                
                // Gestionar carta/productos
                .requestMatchers(HttpMethod.POST, "/producto").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.PUT, "/producto/{id}").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.DELETE, "/producto/{id}").hasAuthority("CASETA")
                
                // Gestionar socios
                .requestMatchers(HttpMethod.POST, "/caseta/socios/{socioId}").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.DELETE, "/caseta/socios/{socioId}").hasAuthority("CASETA")
                
                // Editar datos propios
                .requestMatchers(HttpMethod.PUT, "/caseta").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.DELETE, "/caseta").hasAuthority("CASETA")
                
                // ==========================================
                // SOCIO
                // ==========================================
                // Listar casetas propias
                .requestMatchers(HttpMethod.GET, "/socio/misCasetas").hasAuthority("SOCIO")
                
                // Editar datos propios
                .requestMatchers(HttpMethod.PUT, "/socio").hasAuthority("SOCIO")
                .requestMatchers(HttpMethod.DELETE, "/socio").hasAuthority("SOCIO")
                
                // ==========================================
                // TODAS LAS DEMÁS RUTAS
                // ==========================================
                .anyRequest().authenticated())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}