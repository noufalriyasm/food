package in.noufal.foodsApi.service;

import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
  Authentication getAuthentication();
}
