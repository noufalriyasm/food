package in.noufal.foodsApi.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodRequest {
  @NotBlank(message = "Name cannot be null or empty")
  //  @Pattern(regexp = "^[A-Za-z ]+$", message = "Name must contain only letters")
  private String name;

  @NotBlank(message = "description cannot be null or empty")
  @Size(max = 50, message = "description should not be more than 25 characters")
  private String description;

  @NotBlank(message = "category cannot be null or empty")
  @Pattern(
      regexp = "^(veg|non_veg|drinks|others)$",
      message = "Category must be one of: veg, non_veg, drinks")
  private String category;

  @NotNull(message = "isActive cannot be null")
  private Boolean isActive;

  @NotNull(message = "price cannot be null")
  private Double price;
}
