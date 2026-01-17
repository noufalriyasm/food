package in.noufal.foodsApi.service;

import in.noufal.foodsApi.entity.UserEntity;
import in.noufal.foodsApi.exceptions.BusinessException;
import in.noufal.foodsApi.repository.UserRepository;
import in.noufal.foodsApi.requests.UserRequest;
import in.noufal.foodsApi.response.UserResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationFacade authenticationFacade;

  @Override
  public UserResponse registerUser(UserRequest userRequest) {
    UserEntity userEntity = convertToEntity(userRequest);
    UserEntity userData = userRepository.addUser(userEntity);
    return UserResponse.builder()
        .status(1)
        .id(userData.getId())
        .email(userData.getEmail())
        .name(userData.getName())
        .build();
  }

  @Override
  public String findByUserId() {
    String loggedInUserEmail = authenticationFacade.getAuthentication().getName();
    UserEntity loggedInUserData = userRepository.findByEmail(loggedInUserEmail);
    if (loggedInUserData == null) {
      throw new BusinessException("User not found");
    }
    return loggedInUserData.getId();
  }

  private UserEntity convertToEntity(UserRequest request) {
    return UserEntity.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .name(request.getName())
        .build();
  }
}
