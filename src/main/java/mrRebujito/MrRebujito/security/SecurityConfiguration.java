package mrRebujito.MrRebujito.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
	private JWTAuthenticationFilter JWTAuthenticationFilter;

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.cors().and()
		.csrf().disable()
			.authorizeHttpRequests()
				// LOGIN
				.requestMatchers("/login").permitAll()
				.requestMatchers("/userLogin").permitAll()
				.requestMatchers("/login/actorExiste/{username}").permitAll()
				// PRODUCTO
				.requestMatchers(HttpMethod.GET, "/producto").permitAll()
				.requestMatchers(HttpMethod.GET, "/producto/{id}").permitAll()
				.requestMatchers(HttpMethod.POST, "/producto").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.PUT, "/producto/{id}").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.DELETE, "/producto/{id}").hasAuthority("CASETA")

				// SOLICITUD
				.requestMatchers(HttpMethod.GET, "/solicitud/aceptar/{id}").hasAuthority("AYUNTAMIENTO")
				.requestMatchers(HttpMethod.GET, "/solicitud/rechazar/{id}").hasAuthority("AYUNTAMIENTO")
				.requestMatchers(HttpMethod.GET, "/solicitud/{id}").hasAnyAuthority("CASETA", "AYUNTAMIENTO")
				.requestMatchers(HttpMethod.GET, "/solicitud/deCaseta").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.GET, "/solicitud/deAyuntamiento").hasAuthority("AYUNTAMIENTO")
				.requestMatchers(HttpMethod.POST, "/solicitud/{id}").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.DELETE, "/solicitud").hasAuthority("CASETA")
				
				// CASETA
				.requestMatchers(HttpMethod.GET, "/caseta").permitAll()
				.requestMatchers(HttpMethod.GET, "/caseta/{id}").permitAll()
				.requestMatchers(HttpMethod.GET, "/caseta/carta/{id}").permitAll()
				.requestMatchers(HttpMethod.POST, "/caseta").permitAll()
				.requestMatchers(HttpMethod.PUT, "/caseta").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.DELETE, "/caseta").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.GET, "/caseta/socios").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.GET, "/caseta/anadirSocio/{id}").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.GET, "/caseta/eliminarSocio/{id}").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.POST, "/caseta/carta").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.PUT, "/caseta/carta/{id}").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.DELETE, "/caseta").hasAuthority("CASETA")
				.requestMatchers(HttpMethod.GET, "/caseta/carta").hasAuthority("CASETA")
				
				// SOCIO
				.requestMatchers(HttpMethod.POST, "/socios").permitAll()
				.requestMatchers(HttpMethod.PUT, "/socios").hasAuthority("SOCIO")
				.requestMatchers(HttpMethod.DELETE, "/socios").hasAuthority("SOCIO")
				.requestMatchers(HttpMethod.GET, "/socios/misCasetas").hasAuthority("SOCIO")
				
				// ADMIN
				.requestMatchers("/admin").hasAuthority("ADMIN")
				
				// AYUNTAMIENTO
				.requestMatchers(HttpMethod.GET, "/ayuntamiento").permitAll()
				.requestMatchers(HttpMethod.GET, "/ayuntamiento/{id}").permitAll()
				.requestMatchers(HttpMethod.POST, "/ayuntamiento").hasAnyAuthority("ADMIN")
				.requestMatchers(HttpMethod.PUT, "/ayuntamiento").hasAnyAuthority("AYUNTAMIENTO")
				.requestMatchers(HttpMethod.DELETE, "/ayuntamiento").hasAuthority("AYUNTAMIENTO")
				
				// SWAGGER
				.requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                
				// OTRAS RUTAS
				.anyRequest().authenticated();

		http.addFilterBefore(JWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
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
