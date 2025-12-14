package in.noufal.foodsApi.constants;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class QueryParams {
  private Boolean isActive;
  private Boolean isRemoved;
}
