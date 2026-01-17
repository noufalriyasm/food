package in.noufal.foodsApi.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtTokenException {
  public void handle(HttpServletResponse httpServletResponse, int status, String message)
      throws IOException {
    httpServletResponse.setStatus(status);
    httpServletResponse.setContentType("application/json");
    httpServletResponse.setCharacterEncoding("UTF-8");

    httpServletResponse.getWriter().write("{ \"status\": 0, \"message\": \"" + message + "\" }");
  }
}
