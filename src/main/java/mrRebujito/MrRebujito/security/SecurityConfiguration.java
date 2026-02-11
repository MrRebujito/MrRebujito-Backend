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

/**
 * Configuracion central de seguridad de la aplicacion MrRebujito.
 * Define que endpoints son publicos y cuales requieren autenticacion o un rol concreto.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    // Gestor de autenticacion necesario para el login con JWT
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Encoder de contraseñas con BCrypt
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cadena de filtros de seguridad.
     * Aqui se definen todas las reglas de acceso a los endpoints de la API.
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                // ==========================================
                // RUTAS PUBLICAS (sin autenticacion)
                // ==========================================

                // Login accesible para todos
                .requestMatchers(HttpMethod.POST, "/login").permitAll()

                // Registro publico SOLO de casetas (socios NO se registran solos)
                .requestMatchers(HttpMethod.POST, "/caseta").permitAll()

                // Listar ayuntamientos es publico (cualquiera puede verlos)
                .requestMatchers(HttpMethod.GET, "/ayuntamiento", "/ayuntamiento/{id}").permitAll()

                // Listar casetas y cartas de productos es publico
                .requestMatchers(HttpMethod.GET,
                    "/caseta",
                    "/caseta/{id}",
                    "/caseta/carta/{id}",
                    "/producto",
                    "/producto/{id}").permitAll()

                // Swagger para documentacion
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // ==========================================
                // RUTAS AUTENTICADAS (cualquier usuario logueado)
                // ==========================================

                // Obtener datos del actor logueado
                .requestMatchers(HttpMethod.GET, "/actorLogin").authenticated()

                // ==========================================
                // ADMINISTRADOR - control total
                // ==========================================

                // CRUD completo de administradores
                .requestMatchers("/administrador/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/administrador").hasAuthority("ADMIN")

                // ADMIN crea, edita y borra ayuntamientos por ID
                .requestMatchers(HttpMethod.POST, "/ayuntamiento").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/ayuntamiento/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/ayuntamiento/{id}").hasAuthority("ADMIN")

                // ADMIN crea socios (los socios NO se registran solos)
                .requestMatchers(HttpMethod.POST, "/socio").hasAuthority("ADMIN")

                // ADMIN puede ver la lista de socios
                .requestMatchers(HttpMethod.GET, "/socio", "/socio/{id}").hasAuthority("ADMIN")

                // ADMIN puede banear y desbanear actores
                .requestMatchers(HttpMethod.PUT, "/banear/{actorId}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/desbanear/{actorId}").hasAuthority("ADMIN")

                // ADMIN tiene control total sobre solicitudes de licencia
                .requestMatchers(HttpMethod.POST, "/solicitud").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.POST, "/solicitud/crear-con-ayuntamiento/{ayuntamientoId}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/solicitud/{id}").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/solicitud/{id}").hasAnyAuthority("ADMIN", "CASETA")

                // ==========================================
                // AYUNTAMIENTO
                // ==========================================

                // Editar y borrar su propio perfil (sin ID en la ruta)
                .requestMatchers(HttpMethod.PUT, "/ayuntamiento").hasAuthority("AYUNTAMIENTO")
                .requestMatchers(HttpMethod.DELETE, "/ayuntamiento").hasAuthority("AYUNTAMIENTO")

                // Ver solicitudes dirigidas a su ayuntamiento
                .requestMatchers(HttpMethod.GET, "/solicitud/ayuntamiento").hasAuthority("AYUNTAMIENTO")

                // Cambiar estado de solicitudes (aceptar/rechazar)
                .requestMatchers(HttpMethod.PUT, "/solicitud/aceptar/{id}").hasAuthority("AYUNTAMIENTO")
                .requestMatchers(HttpMethod.PUT, "/solicitud/rechazar/{id}").hasAuthority("AYUNTAMIENTO")

                // ==========================================
                // CASETA
                // ==========================================

                // Editar y borrar su propio perfil
                .requestMatchers(HttpMethod.PUT, "/caseta").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.DELETE, "/caseta").hasAuthority("CASETA")

                // Crear solicitud de licencia a un ayuntamiento
                .requestMatchers(HttpMethod.POST, "/caseta/solicitud/{ayuntamientoId}").hasAuthority("CASETA")

                // Ver sus propios socios y gestionarlos
                .requestMatchers(HttpMethod.GET, "/caseta/socios").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.GET, "/caseta/anadirSocio/{idSocio}").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.GET, "/caseta/eliminarSocio/{idSocio}").hasAuthority("CASETA")

                // Gestionar carta de productos
                .requestMatchers(HttpMethod.GET, "/caseta/carta").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.POST, "/producto").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.PUT, "/producto/{id}").hasAuthority("CASETA")
                .requestMatchers(HttpMethod.DELETE, "/producto/{id}").hasAuthority("CASETA")

                // Ver solicitudes (todas las publicas para filtrar las suyas en frontend)
                .requestMatchers(HttpMethod.GET, "/solicitud", "/solicitud/{id}").hasAnyAuthority("CASETA", "ADMIN", "AYUNTAMIENTO")

                // ==========================================
                // SOCIO
                // ==========================================

                // Ver las casetas a las que pertenece
                .requestMatchers(HttpMethod.GET, "/socio/misCasetas").hasAuthority("SOCIO")

                // Editar y borrar su propio perfil
                .requestMatchers(HttpMethod.PUT, "/socio").hasAuthority("SOCIO")
                .requestMatchers(HttpMethod.DELETE, "/socio").hasAuthority("SOCIO")

                // ==========================================
                // TODAS LAS DEMAS RUTAS requieren autenticacion
                // ==========================================
                .anyRequest().authenticated())

            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuracion de CORS para permitir peticiones desde Angular (localhost:4200)
     */
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