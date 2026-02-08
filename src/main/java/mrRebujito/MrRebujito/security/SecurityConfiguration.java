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
		http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(auth -> auth

				// ==========================================
				// 1. AUTENTICACIÓN (Público)
				.requestMatchers("/login", "/userLogin").permitAll()
				.requestMatchers(HttpMethod.POST, "/socio", "/caseta").permitAll()
				.requestMatchers(HttpMethod.GET, "/socio", "/socio/{id}").permitAll()

				// 1b. Listar ayuntamientos
				.requestMatchers(HttpMethod.GET, "/ayuntamiento", "/ayuntamiento/{id}").permitAll()

				// 1c. Listar casetas y sus cartas (productos)
				.requestMatchers(HttpMethod.GET, "/caseta", "/caseta/{id}", "/solicitud", "/solicitud/{id}", "/socio").permitAll()
				.requestMatchers(HttpMethod.GET, "/caseta/carta/{id}", "/producto", "/producto/{id}").permitAll()

				// ==========================================
				// 3. ADMINISTRADOR (Req. Funcional 6)
				// ==========================================
				.requestMatchers("/admin/**").hasAuthority("ADMIN")
				// 6b. Registrar ayuntamientos (Solo admin puede crear ayuntamientos)
				.requestMatchers(HttpMethod.POST, "/ayuntamiento").hasAuthority("ADMIN")

				// ==========================================
				// 4. CASETA (Req. Funcional 3)
				// ==========================================
				// 3a. Gestionar solicitudes (Listar propias, crear, borrar si es pendiente)
				.requestMatchers(HttpMethod.POST, "/solicitud").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.DELETE, "/solicitud/{id}").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.GET, "/solicitud/Caseta").hasAuthority("CASETA")

				// 3b. Gestionar carta (Añadir/Borrar/Modificar productos)
				.requestMatchers(HttpMethod.POST, "/producto").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.PUT, "/producto/{id}").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.DELETE, "/producto/{id}").hasAuthority("CASETA")

				// 3c. Añadir/Eliminar socios
				.requestMatchers(HttpMethod.POST, "/caseta/socios/{socioId}").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.DELETE, "/caseta/socios/{socioId}").hasAuthority("CASETA")

				// Editar sus propios datos
				.requestMatchers(HttpMethod.PUT, "/caseta/").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.DELETE, "/caseta/").hasAuthority("CASETA")

				// ==========================================
				// 5. AYUNTAMIENTO (Req. Funcional 4)
				// ==========================================
				// 4a. Listar sus casetas aceptadas (Ruta privada del ayuntamiento)
				.requestMatchers(HttpMethod.GET, "/solicitud/ayuntamiento").hasAuthority("AYUNTAMIENTO")

				// IMPORTANTE: PUT porque cambia el estado de la BD
				.requestMatchers(HttpMethod.PUT, "/solicitud/{id}/aceptar").hasAuthority("AYUNTAMIENTO")
				.requestMatchers(HttpMethod.PUT, "/solicitud/{id}/rechazar").hasAuthority("AYUNTAMIENTO")

				// Editar sus propios datos
				.requestMatchers(HttpMethod.PUT, "/ayuntamiento/").hasAuthority("AYUNTAMIENTO")

				// ==========================================
				// 6. SOCIO (Req. Funcional 5)
				// ==========================================
				// 5b. Listar casetas a las que pertenece
				.requestMatchers(HttpMethod.GET, "/socio/misCasetas").hasAuthority("SOCIO")

				// Editar sus datos (Req 2b)
				.requestMatchers(HttpMethod.PUT, "/socio").hasAuthority("SOCIO")
				.requestMatchers(HttpMethod.DELETE, "/socio").hasAuthority("SOCIO")
				.requestMatchers(HttpMethod.GET, "/socio", "/socio/{id}").permitAll()

				// SWAGGER (Documentación)
				.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()

				// CUALQUIER OTRA RUTA
				.anyRequest().authenticated())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	// Configuración de CORS para permitir peticiones desde Angular (localhost:4200)
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