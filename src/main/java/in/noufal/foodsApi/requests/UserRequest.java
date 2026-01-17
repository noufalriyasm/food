package in.noufal.foodsApi.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
  @NotBlank(message = "Name cannot be null")
  @Size(min = 5, max = 20, message = "Name length should be in between 5 and 20")
  private String name;

  @NotBlank(message = "Email cannot be null")
  @Email(message = "Invalid Email format")
  private String email;

  @NotBlank(message = "Password cannot be null")
  @Size(min = 6, message = "Password must be at least 8 characters")
  private String password;
}
