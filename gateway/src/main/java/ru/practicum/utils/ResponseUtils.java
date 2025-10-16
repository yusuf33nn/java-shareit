package ru.practicum.utils;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;

@UtilityClass
public class ResponseUtils {

    public static ResponseEntity<Object> createResponse(ResponseEntity<Object> responseEntity) {
        return ResponseEntity.status(responseEntity.getStatusCode()).body(responseEntity.getBody());
    }
}
