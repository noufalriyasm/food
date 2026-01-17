package in.noufal.foodsApi.repository;

import com.mongodb.MongoException;
import in.noufal.foodsApi.constants.DbCollections;
import in.noufal.foodsApi.constants.Fields;
import in.noufal.foodsApi.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class UserRepository {
  private final MongoTemplate mongoTemplate;

  public UserEntity addUser(UserEntity userEntity) throws MongoException {
    return mongoTemplate.save(userEntity, DbCollections.USERS);
  }

  public UserEntity findByEmail(String email) {
    Query query = new Query();
    query.addCriteria(Criteria.where(Fields.EMAIL).is(email));
    return mongoTemplate.findOne(query, UserEntity.class);
  }
}
