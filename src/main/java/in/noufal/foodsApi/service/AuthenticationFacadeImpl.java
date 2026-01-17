package in.noufal.foodsApi.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

  // This class is essentially a helper / utility to access the current authenticated user anywhere
  // in your application.
  //  Returns the current Authentication object from Spring Security’s SecurityContextHolder.
  //
  //  Authentication contains:
  //
  //  principal → the authenticated user (UserEntity in your case)
  //
  //  authorities → user roles/permissions
  //
  //  isAuthenticated → true/false

  //  In some services or components, you may not have direct access to the HttpServletRequest or
  // @AuthenticationPrincipal, but you still need the current user.
  // You cannot directly use @AuthenticationPrincipal in service or repository layers because it is
  // a Spring MVC annotation, which only works in controller method parameters
  @Override
  public Authentication getAuthentication() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  // example:
  // @Service
  // @AllArgsConstructor
  // public class OrderService {
  //    private final AuthenticationFacade authenticationFacade;
  //
  //    public void placeOrder(OrderRequest request) {
  //        UserEntity user = (UserEntity) authenticationFacade.getAuthentication().getPrincipal();
  //        // now you have the current logged-in user
  //        orderRepository.save(new Order(user.getId(), request.getItems()));
  //    }
  // }
}
