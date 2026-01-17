package in.noufal.foodsApi.repository;

import in.noufal.foodsApi.constants.Fields;
import in.noufal.foodsApi.entity.CartEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@AllArgsConstructor
@Repository
public class CartRepository {

  private final MongoTemplate mongoTemplate;

  public CartEntity findByUserId(String loggedInUserId) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Fields.ID).is(loggedInUserId));
    return mongoTemplate.findOne(query, CartEntity.class);
  }
}
