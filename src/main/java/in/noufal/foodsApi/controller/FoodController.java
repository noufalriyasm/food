package in.noufal.foodsApi.controller;

import in.noufal.foodsApi.constants.Messages;
import in.noufal.foodsApi.constants.QueryParams;
import in.noufal.foodsApi.exceptions.BusinessException;
import in.noufal.foodsApi.requests.FoodRequest;
import in.noufal.foodsApi.response.FoodGetListResponse;
import in.noufal.foodsApi.response.FoodMiniResponse;
import in.noufal.foodsApi.response.FoodResponse;
import in.noufal.foodsApi.response.SuccessResponse;
import in.noufal.foodsApi.service.FoodService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/foods/")
@AllArgsConstructor
public class FoodController {

  @Autowired private final FoodService foodService;

  /**
   * @param request
   * @param file
   * @param httpServletRequest
   * @return
   */
  @PostMapping("create-food")
  public ResponseEntity<SuccessResponse> addFood(
      @Valid @ModelAttribute FoodRequest request,
      @RequestPart(value = "file", required = false) MultipartFile file,
      HttpServletRequest httpServletRequest) {
    Set<String> allowedBodyParams =
        Set.of("name", "description", "category", "price", "isActive", "file");
    /**
     * httpServlet request gives you the complete request params if i pass same name key multiple
     * times spring store it in getParameterMap like {"name":["apple","banana"]}
     */
    httpServletRequest
        .getParameterMap()
        .forEach(
            (key, value) -> {
              if (value != null && value.length > 1) {
                throw new BusinessException(String.format(Messages.DUPLICATE_PARAM, key));
              }
              if (!allowedBodyParams.contains(key)) {
                throw new BusinessException(Messages.INVALID_PARAMS);
              }
            });
    SuccessResponse foodResponse = foodService.addFood(request, file);
    if (foodResponse != null) {
      return ResponseEntity.status(HttpStatus.OK).body(foodResponse);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @GetMapping("/get-food-list")
  public ResponseEntity<FoodGetListResponse> getFoodList(
      @RequestParam(required = false) final Boolean isActive,
      @RequestParam(required = false) final Boolean isRemoved,
      @RequestParam Map<String, String> allParams) {

    Set<String> allowedParams = Set.of("isActive", "isRemoved");
    List<String> unknownParams =
        allParams.keySet().stream().filter(param -> !allowedParams.contains(param)).toList();
    if (!unknownParams.isEmpty()) {
      throw new BusinessException(String.format("Invalid params: %s", unknownParams));
    }
    QueryParams queryParams = new QueryParams();
    queryParams.setIsActive(isActive);
    queryParams.setIsRemoved(isRemoved);

    FoodGetListResponse foodGetListResponse = foodService.getFoodList(queryParams);
    if (foodGetListResponse != null) {
      return ResponseEntity.status(HttpStatus.OK).body(foodGetListResponse);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }

  @GetMapping("/get-food-details/{foodItemId}")
  public ResponseEntity<FoodResponse> getFoodDetails(
      @PathVariable(required = true) final String foodItemId) {
    FoodResponse foodResponse = foodService.getFoodItemDetails(foodItemId);
    if (foodResponse != null) {
      return ResponseEntity.status(HttpStatus.OK).body(foodResponse);
    } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @DeleteMapping("/soft-delete-food-item/{foodItemId}")
  public ResponseEntity<SuccessResponse> softDeleteFoodItem(@PathVariable final String foodItemId) {
    SuccessResponse response = foodService.softDeleteFoodItem(foodItemId);
    if (response != null) {
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } else return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }

  @DeleteMapping("/delete-food-item/{foodItemId}")
  public ResponseEntity<SuccessResponse> deleteFoodItem(@PathVariable final String foodItemId) {
    SuccessResponse response = foodService.deleteFoodItem(foodItemId);

    if (response != null) {
      return ResponseEntity.status(HttpStatus.OK).body(response);
    } else {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
  }
}
