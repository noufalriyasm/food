package in.noufal.foodsApi.service;

import in.noufal.foodsApi.entity.FoodEntity;
import in.noufal.foodsApi.mapper.FoodMapper;
import in.noufal.foodsApi.repository.FoodRepository;
import in.noufal.foodsApi.requests.FoodRequest;
import in.noufal.foodsApi.response.FoodResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FoodServiceImpl implements FoodService{

    private final S3Client s3Client;
    private final FoodMapper foodMapper;
    private final FoodRepository foodRepository;

    @Value("${aws.s3.bucketname}")
    private String bucketName;

    @Override
    public String uploadFile(MultipartFile file){

        /*
        extracting the file extension first
        */
        String fileNameExtension= Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename().lastIndexOf(".")+1);
        /*
        * creating a unique id for name the image in s3
        */
        String key=UUID.randomUUID().toString()+"."+fileNameExtension;

        try{
            /*
            this portion gives instructions for how to upload
            */
            PutObjectRequest putObjectRequest= PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .acl("public-read")
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse response=s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

            if(response.sdkHttpResponse().isSuccessful()){
                return "https://"+bucketName+".s3.amazonaws.com/"+key;
            }
            else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"file upload failed");
            }
        }
        catch(IOException ex){
            throw new ResponseStatusException
                    (HttpStatus.INTERNAL_SERVER_ERROR,"An error occurred while uploading the file");
        }


    }

    @Override
    public FoodResponse addFood(FoodRequest request,MultipartFile file){
        FoodEntity newFoodEntity=foodMapper.convertToEntity(request);
        String imageUrl=uploadFile(file);
        newFoodEntity.setImageUrl(imageUrl);
        foodRepository.addFood(newFoodEntity);
        return FoodResponse.builder()
                .status(1)
                .message("food Item added successfully")
                .name(newFoodEntity.getName())
                .id(newFoodEntity.getId())
                .price(newFoodEntity.getPrice())
                .description(newFoodEntity.getDescription())
                .category(newFoodEntity.getCategory())
                .imageUrl(newFoodEntity.getImageUrl())
                .build();


    }


}
