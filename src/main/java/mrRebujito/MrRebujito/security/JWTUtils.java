package mrRebujito.MrRebujito.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import mrRebujito.MrRebujito.entity.Actor;
import mrRebujito.MrRebujito.entity.Administrador;
import mrRebujito.MrRebujito.entity.Ayuntamiento;
import mrRebujito.MrRebujito.entity.Caseta;
import mrRebujito.MrRebujito.entity.Socio;
import mrRebujito.MrRebujito.service.ActorService;
import mrRebujito.MrRebujito.service.AdministradorService;
import mrRebujito.MrRebujito.service.AyuntamientoService;
import mrRebujito.MrRebujito.service.CasetaService;
import mrRebujito.MrRebujito.service.SocioService;


@Component
public class JWTUtils {
	private static final String JWT_FIRMA = "Antonio";
	private static final long EXTENCION_TOKEN = 3600000; //Cambiado esto para que el token dure una hora 
	
	@Autowired
	@Lazy
	private ActorService actorService;

	@Autowired
	@Lazy
	private CasetaService casetaService;

	@Autowired
	@Lazy
	private AdministradorService adminService;

	@Autowired
	@Lazy
	private SocioService socioService;

	@Autowired
	@Lazy
	private AyuntamientoService ayuntamientoService;

	public static String getToken(HttpServletRequest request) {
		String tokenBearer = request.getHeader("Authorization");
		if (StringUtils.hasText(tokenBearer) && tokenBearer.startsWith("Bearer ")) {
			return tokenBearer.substring(7);
		}
		return null;
	}

	public static boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(JWT_FIRMA).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			throw new AuthenticationCredentialsNotFoundException("JWT ha experido o no es valido");
		}
	}

	public static String getUsernameOfToken(String token) {
		return Jwts.parser().setSigningKey(JWT_FIRMA).parseClaimsJws(token).getBody().getSubject();
	}

	public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date fechaActual = new Date();
        Date fechaExpiracion = new Date(fechaActual.getTime() + EXTENCION_TOKEN);
        String rol = authentication.getAuthorities().iterator().next().getAuthority();
        String token = Jwts.builder().setSubject(username).setIssuedAt(fechaActual).setExpiration(fechaExpiracion)
                .claim("rol", rol).signWith(SignatureAlgorithm.HS512, JWT_FIRMA).compact();
        return token;
    }
	
	
	
	// Obtener el rol a partir del token
	public static String getRolFromToken(String token) {
		return Jwts.parser()
			.setSigningKey(JWT_FIRMA)
			.parseClaimsJws(token)
			.getBody()
			.get("rol", String.class);
	}

	public <T> T userLogin() {
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		T res = null;

		if (StringUtils.hasText(username)) {
			Optional<Actor> actorO = actorService.findByUsername(username);
			if (actorO.isPresent()) {
				Actor actor = actorO.get();
				switch (actor.getRol()) {
				case CASETA:
					Optional<Caseta> casetaOptional = casetaService.findByUsername(username);
					if (casetaOptional.isPresent()) {
						res = (T) casetaOptional.get();
					}
					break;
				case ADMIN:
					Optional<Administrador> adminOptional = adminService.findByUsername(username);
					if (adminOptional.isPresent()) {
						res = (T) adminOptional.get();
					}
					break;
				case AYUNTAMIENTO:
					Optional<Ayuntamiento> ayuntamientoOptional = ayuntamientoService.findByUsername(username);
					if (ayuntamientoOptional.isPresent()) {
						res = (T) ayuntamientoOptional.get();
					}
					break;

				case SOCIO:
					Optional<Socio> socioOptional = socioService.findByUsername(username);
					if (socioOptional.isPresent()) {
						res = (T) socioOptional.get();
					}
					break;
				}
			}
		}
		return res;
	}
}
