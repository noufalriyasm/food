package in.noufal.foodsApi.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class BusinessException extends RuntimeException {
  public BusinessException(String message) {
    super(message);
  }
}
