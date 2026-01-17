package in.noufal.foodsApi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import in.noufal.foodsApi.constants.DbCollections;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = DbCollections.USERS)
public class UserEntity implements UserDetails {
  // here i implemented UserDetails for getting complete user details when a user is authorized
  @Id private String id;
  private String name;
  private String email;
  @ToString.Exclude @JsonIgnore private String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    // this is for role based authorization check
    return Collections.emptyList();
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
