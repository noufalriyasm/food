package in.noufal.foodsApi.service;

import in.noufal.foodsApi.constants.Messages;
import in.noufal.foodsApi.entity.UserEntity;
import in.noufal.foodsApi.exceptions.BusinessException;
import in.noufal.foodsApi.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity user = userRepository.findByEmail(email);
    if (user == null) {
      throw new BusinessException(Messages.USER_NOT_FOUND);
    } else {
      // this line return spring default user with username and password,but for getting all user
      // details im returning user entity
      //      return new User(user.getEmail(), user.getPassword(), Collections.emptyList());

      return user; // this line will return all user
    }
  }
}
