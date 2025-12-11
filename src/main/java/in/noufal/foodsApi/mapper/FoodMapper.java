package in.noufal.foodsApi.mapper;

import in.noufal.foodsApi.entity.FoodEntity;
import in.noufal.foodsApi.requests.FoodRequest;
import in.noufal.foodsApi.response.FoodResponse;
import org.springframework.stereotype.Component;

@Component
public class FoodMapper {
    public FoodEntity convertToEntity(FoodRequest request){
       return FoodEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(request.getCategory())
                .price(request.getPrice())
                .build();
    }


}
