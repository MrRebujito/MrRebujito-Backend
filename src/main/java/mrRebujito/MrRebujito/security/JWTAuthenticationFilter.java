package mrRebujito.MrRebujito.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mrRebujito.MrRebujito.service.ActorService;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private ActorService actorService;

	public JWTAuthenticationFilter(ActorService actorService) {
		this.actorService = actorService;
	}

	@Override
protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
    String token = JWTUtils.getToken(request);
    if (StringUtils.hasText(token) && JWTUtils.validateToken(token)) {
        String username = JWTUtils.getUsernameOfToken(token);
        String rol = JWTUtils.getRolFromToken(token);  
        UserDetails userDetails = actorService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
    filterChain.doFilter(request, response);
}
}
