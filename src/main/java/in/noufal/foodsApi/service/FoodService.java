package in.noufal.foodsApi.service;

import in.noufal.foodsApi.constants.QueryParams;
import in.noufal.foodsApi.requests.FoodRequest;
import in.noufal.foodsApi.response.FoodGetListResponse;
import in.noufal.foodsApi.response.FoodResponse;
import in.noufal.foodsApi.response.SuccessResponse;
import org.springframework.web.multipart.MultipartFile;

public interface FoodService {
  String uploadFile(MultipartFile file);

  SuccessResponse addFood(FoodRequest request, MultipartFile file);

  FoodGetListResponse getFoodList(final QueryParams queryParams);

  FoodResponse getFoodItemDetails(final String foodItemId);

  SuccessResponse softDeleteFoodItem(final String foodItemId);

  boolean deleteFile(String fileName);

  SuccessResponse deleteFoodItem(final String foodItemId);
}
