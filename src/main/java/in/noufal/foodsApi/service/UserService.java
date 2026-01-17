package in.noufal.foodsApi.service;

import in.noufal.foodsApi.requests.UserRequest;
import in.noufal.foodsApi.response.UserResponse;

public interface UserService {
  UserResponse registerUser(UserRequest userRequest);

  String findByUserId();
}
