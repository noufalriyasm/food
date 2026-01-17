package in.noufal.foodsApi.service;

import in.noufal.foodsApi.repository.CartRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class CartServiceImpl implements CartService {
  private final CartRepository cartRepository;
  private final UserService userService;

  @Override
  public void addToCart(String foodId) {
    String loggedInUserId = userService.findByUserId();
    //        cartRepository

  }
}
