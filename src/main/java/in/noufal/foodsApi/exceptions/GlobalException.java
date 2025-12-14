package in.noufal.foodsApi.exceptions;

import com.mongodb.MongoException;
import in.noufal.foodsApi.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;

@RestControllerAdvice
public class GlobalException {
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
    ErrorResponse errorResponse = ErrorResponse.builder().status(0).message(e.getMessage()).build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(org.springframework.validation.BindException.class)
  public ResponseEntity<ErrorResponse> handleBindException(
      org.springframework.validation.BindException e) {

    String firstError = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

    ErrorResponse response = ErrorResponse.builder().status(0).message(firstError).build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException e) {
    List<String> messages =
        e.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getDefaultMessage())
            .toList();

    ErrorResponse errorResponse =
        ErrorResponse.builder().status(0).message(messages.get(0)).build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(NullPointerException.class)
  public ResponseEntity<ErrorResponse> nullPointerException(NullPointerException e) {
    ErrorResponse errorResponse = ErrorResponse.builder().status(0).message(e.getMessage()).build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<ErrorResponse> handleMissingPart(MissingServletRequestPartException e) {
    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .status(0)
            .message("Required file part is missing: " + e.getRequestPartName())
            .build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MongoException.class)
  public ResponseEntity<ErrorResponse> handleMongoException(MongoException e) {
    ErrorResponse errorResponse =
        ErrorResponse.builder().status(0).message("Database error : " + e.getMessage()).build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
    ErrorResponse errorResponse = ErrorResponse.builder().status(0).message(e.getMessage()).build();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
