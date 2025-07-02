package com.example.restaurantmanagement.infrastructure.exception;

import com.example.restaurantmanagement.response.ResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NVException.class)
    public ResponseEntity<ResponseObject> handleNVException(NVException ex) {
        ErrorCode errorCode = ErrorCode.valueOf(ex.getMessage());
        String message = formatErrorMessage(errorCode, ex.getArgs());

        return ResponseEntity.badRequest().body(
                new ResponseObject(errorCode.name(), message)
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ResponseObject("INTERNAL_ERROR", ex.getMessage())
        );
    }
    //Tạo ra thông báo lỗi (error message) rõ ràng và dễ hiểu cho
    private String formatErrorMessage(ErrorCode errorCode, Object[] args) {
        // Tuỳ biến ở đây – nếu dùng file message.properties thì load ở đây
        // Hoặc đơn giản như dưới:
        return switch (errorCode) {
            case TABLE_CONFLICT -> "Table number " + args[0] + " is already reserved.";
            case CUSTOMER_NOT_FOUND -> "Customer not found.";
            case STAFF_NOT_FOUND -> "Staff not found.";
            case RESERVATION_ID_NOT_NULL -> "Reservation ID must be null when creating.";
            case DUPLICATE_ACCOUNT -> "This username already exists.";
            case TIME_INVALID -> args.length > 0 ? args[0].toString() : "Invalid reservation time.";
            default -> errorCode.name(); // fallback nếu chưa define rõ
        };
    }
}
