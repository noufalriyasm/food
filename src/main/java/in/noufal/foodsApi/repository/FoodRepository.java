package in.noufal.foodsApi.repository;

import com.mongodb.MongoException;
import in.noufal.foodsApi.entity.FoodEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class FoodRepository {
    private final MongoTemplate mongoTemplate;

    public void addFood(final FoodEntity foodEntity) throws MongoException{
        mongoTemplate.save(foodEntity);
    }

}
