package in.noufal.foodsApi.service;

import in.noufal.foodsApi.requests.FoodRequest;
import in.noufal.foodsApi.response.FoodResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FoodService {
    String uploadFile(MultipartFile file);
    FoodResponse addFood(FoodRequest request, MultipartFile file);
}
