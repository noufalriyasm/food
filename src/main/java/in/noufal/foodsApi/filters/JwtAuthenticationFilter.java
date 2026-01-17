package in.noufal.foodsApi.filters;

import in.noufal.foodsApi.exceptions.BusinessException;
import in.noufal.foodsApi.exceptions.JwtTokenException;
import in.noufal.foodsApi.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired private JwtUtil jwtUtil;
  @Autowired private UserDetailsService userDetailsService;
  @Autowired private final JwtTokenException jwtTokenException;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      final String authHeader = request.getHeader("Authorization");
      if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
          UserDetails userDetails = userDetailsService.loadUserByUsername(email);

          if (jwtUtil.validateToken(token, userDetails)) {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()); // here the credentials null means password
            // already checked,JWT-based auth does not recheck password

            authenticationToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
          }
        }
      }
      filterChain.doFilter(request, response);

    } catch (ExpiredJwtException e) {
      jwtTokenException.handle(response, 401, "Jwt token expired");
      return;
    } catch (JwtException | IllegalArgumentException e) {
      jwtTokenException.handle(response, 401, "Invalid JWT token");
      return;
    }
  }
}
