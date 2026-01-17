package in.noufal.foodsApi.controller;

import in.noufal.foodsApi.entity.UserEntity;
import in.noufal.foodsApi.requests.AuthenticationRequest;
import in.noufal.foodsApi.response.AuthenticationResponse;
import in.noufal.foodsApi.service.AppUserDetailsService;
import in.noufal.foodsApi.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthController {
  private final AuthenticationManager authenticationManager;
  private final AppUserDetailsService userDetailsService;
  private final JwtUtil jwtUtil;

  @PostMapping("/login")
  public AuthenticationResponse login(@Valid @RequestBody AuthenticationRequest request) {

    // im commenting the below code and assign it into a variable because im fetching the user
    // details again,inorder to avoid that db hit im using already fetched data
    //    authenticationManager.authenticate(
    //        new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    Authentication auth =
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
    /* flow of this authentication
     * 1.UsernamePasswordAuthenticationToken this constructor automatically trigger userDetailsService.loadUserByUsername from AppUserDetailService class
     * 2.that will return userEntity
     * 3.internally checks the password (in securityConfig file contain passwordEncoder and AuthenticationManager function,this automatically calls and check password is matching or not)
     * 4.if not matching give exception with bad credentials;
     * 5.if match that will return a auth type value
     * 6.that value contain our userEntity
     */

    // commenting this line because im already fetching user details in authentication
    // final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
    UserEntity userDetails = (UserEntity) auth.getPrincipal();

    final String jwtToken = jwtUtil.generateToken(userDetails);
    return AuthenticationResponse.builder()
        .status(1)
        .email(userDetails.getEmail())
        .token(jwtToken)
        .build();
  }
}
