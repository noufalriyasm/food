package in.noufal.foodsApi.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartRequest {
  private String userId;
  private Map<String, Integer> items = new HashMap<>();
}
