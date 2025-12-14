package in.noufal.foodsApi.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FoodMiniResponse {
  private String id;
  private String name;
  private String description;
  private double price;
  private String category;
  private String imageUrl;
  private Boolean isActive;
  private long createdOn;
  private long updatedOn;
}
