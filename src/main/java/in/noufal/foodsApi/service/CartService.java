package in.noufal.foodsApi.service;

import org.springframework.stereotype.Service;

@Service
public interface CartService {
  void addToCart(String foodId);
}
