package in.noufal.foodsApi.repository;

import com.mongodb.MongoException;
import in.noufal.foodsApi.constants.Fields;
import in.noufal.foodsApi.constants.QueryParams;
import in.noufal.foodsApi.entity.FoodEntity;
import in.noufal.foodsApi.exceptions.BusinessException;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class FoodRepository {
  private final MongoTemplate mongoTemplate;

  public FoodEntity addFood(final FoodEntity foodEntity) throws MongoException {
    return mongoTemplate.save(foodEntity);
  }

  public Pair<Integer, List<FoodEntity>> getFoodsList(final QueryParams queryParams)
      throws MongoException {
    Query query = new Query();
    if (queryParams.getIsActive() != null) {
      query.addCriteria(Criteria.where(Fields.IS_ACTIVE).is(queryParams.getIsActive()));
    }
    if (queryParams.getIsRemoved() != null) {
      query.addCriteria(Criteria.where(Fields.IS_REMOVED).is(queryParams.getIsRemoved()));
    }

    Integer totalCount = Math.toIntExact(mongoTemplate.count(query, FoodEntity.class));
    List<FoodEntity> allFoodList = mongoTemplate.find(query, FoodEntity.class);
    return Pair.of(totalCount, allFoodList);
  }

  public FoodEntity getFoodItemDetails(final String foodItemId) {
    return mongoTemplate.findById(new ObjectId(foodItemId), FoodEntity.class);
  }

  public FoodEntity softDeleteFoodItem(final String foodItemId) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Fields._ID).is(foodItemId).and(Fields.IS_REMOVED).is(false));

    Update update = new Update();
    update.set(Fields.IS_REMOVED, true);
    update.set(Fields.UPDATED_ON, System.currentTimeMillis());

    FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true);

    FoodEntity deletedFood = mongoTemplate.findAndModify(query, update, options, FoodEntity.class);
    if (deletedFood == null) {
      throw new BusinessException("Food not found or already removed");
    }
    return deletedFood;
  }

  public void deleteFoodItem(final String foodItemId) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Fields._ID).is(foodItemId));

    mongoTemplate.remove(query, FoodEntity.class);
  }
}
