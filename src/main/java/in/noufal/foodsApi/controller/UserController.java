package in.noufal.foodsApi.controller;

import in.noufal.foodsApi.requests.UserRequest;
import in.noufal.foodsApi.response.UserResponse;
import in.noufal.foodsApi.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {
  private final UserService userService;

  @PostMapping("/user-register")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponse register(@Valid @RequestBody UserRequest request) {
    return userService.registerUser(request);
  }
}
