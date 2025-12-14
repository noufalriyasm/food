package in.noufal.foodsApi.service;

import com.mongodb.MongoException;
import in.noufal.foodsApi.constants.Messages;
import in.noufal.foodsApi.constants.QueryParams;
import in.noufal.foodsApi.entity.FoodEntity;
import in.noufal.foodsApi.exceptions.BusinessException;
import in.noufal.foodsApi.mapper.FoodMapper;
import in.noufal.foodsApi.repository.FoodRepository;
import in.noufal.foodsApi.requests.FoodRequest;
import in.noufal.foodsApi.response.FoodGetListResponse;
import in.noufal.foodsApi.response.FoodMiniResponse;
import in.noufal.foodsApi.response.FoodResponse;
import in.noufal.foodsApi.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class FoodServiceImpl implements FoodService {

  private final S3Client s3Client;
  private final FoodMapper foodMapper;
  private final FoodRepository foodRepository;

  @Value("${aws.s3.bucketname}")
  private String bucketName;

  public FoodServiceImpl(S3Client s3Client, FoodMapper foodMapper, FoodRepository foodRepository) {
    this.s3Client = s3Client;
    this.foodMapper = foodMapper;
    this.foodRepository = foodRepository;
  }

  @Override
  public String uploadFile(MultipartFile file) {

    if (file == null || file.isEmpty()) {
      return null;
    }
    /*
    extracting the file extension first
    */
    String fileNameExtension =
        Objects.requireNonNull(file.getOriginalFilename())
            .substring(file.getOriginalFilename().lastIndexOf(".") + 1);
    /*
     * creating a unique id for name the image in s3
     */
    String key = UUID.randomUUID().toString() + "." + fileNameExtension;

    try {
      /*
      this portion gives instructions for how to upload
      */
      PutObjectRequest putObjectRequest =
          PutObjectRequest.builder()
              .bucket(bucketName)
              .key(key)
              .acl("public-read")
              .contentType(file.getContentType())
              .build();

      PutObjectResponse response =
          s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

      if (response.sdkHttpResponse().isSuccessful()) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
      } else {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "file upload failed");
      }
    } catch (IOException ex) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while uploading the file");
    }
  }

  @Override
  public SuccessResponse addFood(FoodRequest request, MultipartFile file) {
    try {
      //            FoodEntity newFoodEntity = foodMapper.convertToEntity(request);
      String imageUrl = uploadFile(file);
      FoodEntity food =
          FoodEntity.builder()
              .name(request.getName())
              .description(request.getDescription())
              .category(request.getCategory())
              .price(request.getPrice())
              .imageUrl(imageUrl)
              .isActive(request.getIsActive())
              .isRemoved(false)
              .createdOn(System.currentTimeMillis())
              .updatedOn(System.currentTimeMillis())
              .build();
      FoodEntity savedFood = foodRepository.addFood(food);
      return SuccessResponse.builder()
          .status(1)
          .message(String.format(Messages.CREATED_SUCCESSFULLY, savedFood.getName()))
          .id(savedFood.getId())
          .build();
    } catch (MongoException e) {
      throw e;
    } catch (Exception e) {
      throw new BusinessException(e.getMessage());
    }
  }

  @Override
  public FoodGetListResponse getFoodList(final QueryParams queryParams) {
    try {
      Pair<Integer, List<FoodEntity>> allFoodList = foodRepository.getFoodsList(queryParams);
      List<FoodMiniResponse> foodMiniResponses =
          allFoodList.getSecond().isEmpty()
              ? List.of()
              : allFoodList.getSecond().stream()
                  .map(
                      foodEntity ->
                          FoodMiniResponse.builder()
                              .id(String.valueOf(foodEntity.getId()))
                              .name(foodEntity.getName())
                              .description(foodEntity.getDescription())
                              .category(foodEntity.getCategory())
                              .price(foodEntity.getPrice())
                              .isActive(foodEntity.getIsActive())
                              .createdOn(foodEntity.getCreatedOn())
                              .updatedOn(foodEntity.getUpdatedOn())
                              .imageUrl(foodEntity.getImageUrl())
                              .build())
                  .toList();

      return FoodGetListResponse.builder()
          .status(1)
          .foodItems(foodMiniResponses)
          .totalCount(allFoodList.getFirst())
          .build();

    } catch (MongoException e) {
      throw e;
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public FoodResponse getFoodItemDetails(String foodItemId) {
    try {
      FoodEntity foodEntity = foodRepository.getFoodItemDetails(foodItemId);
      FoodMiniResponse foodResponse =
          FoodMiniResponse.builder()
              .name(foodEntity.getName())
              .id(foodEntity.getId())
              .description(foodEntity.getDescription())
              .category(foodEntity.getCategory())
              .imageUrl(foodEntity.getImageUrl())
              .isActive(foodEntity.getIsActive())
              .createdOn(foodEntity.getCreatedOn())
              .updatedOn(foodEntity.getUpdatedOn())
              .build();
      return FoodResponse.builder().status(1).item(foodResponse).build();
    } catch (MongoException e) {
      throw e;
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public SuccessResponse softDeleteFoodItem(String foodItemId) {
    try {
      FoodEntity foodEntity = foodRepository.softDeleteFoodItem(foodItemId);
      return SuccessResponse.builder()
          .status(1)
          .id(foodEntity.getId())
          .message(String.format(Messages.DELETED_SUCCESFULLY, foodEntity.getName()))
          .build();

    } catch (MongoException e) {
      throw e;
    } catch (Exception e) {
      throw e;
    }
  }

  @Override
  public boolean deleteFile(String fileName) {
    DeleteObjectRequest deleteObjectRequest =
        DeleteObjectRequest.builder().bucket(bucketName).key(fileName).build();
    s3Client.deleteObject(deleteObjectRequest);
    return true;
  }

  @Override
  public SuccessResponse deleteFoodItem(String foodItemId) {
    FoodEntity foodItem = foodRepository.getFoodItemDetails(foodItemId);

    if (foodItem == null) {
      throw new BusinessException("No food item present");
    }
    if (foodItem.getImageUrl() != null) {
      String fileName =
          foodItem.getImageUrl().substring(foodItem.getImageUrl().lastIndexOf("/") + 1);
      boolean isFileDeleted = deleteFile(fileName);
      if (!isFileDeleted) {
        throw new BusinessException("Image deletion failed");
      }
    }
    foodRepository.deleteFoodItem(foodItemId);
    return SuccessResponse.builder()
        .status(1)
        .message(String.format(Messages.DELETED_SUCCESFULLY, foodItem.getName()))
        .id(String.valueOf(foodItem.getId()))
        .build();
  }
}
