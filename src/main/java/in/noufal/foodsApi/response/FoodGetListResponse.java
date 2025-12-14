package in.noufal.foodsApi.response;

import in.noufal.foodsApi.entity.FoodEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FoodGetListResponse {
  private int status;
  private List<FoodMiniResponse> foodItems;
  private int totalCount;
}
