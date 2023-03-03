package com.project.backend.common.response;

import com.project.backend.common.models.AppResponse;
import com.project.backend.common.properties.SpringProfile;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MainResponseImpl implements MainResponse {

    private final SpringProfile springProfile;

    @Override
    public ResponseEntity<AppResponse> success(Object data) {
        return ResponseEntity.ok().body(
                AppResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .data(data)
                        .message("Success")
                        .build()
        );
    }

    @Override
    public ResponseEntity<AppResponse> clientError(String message, Object data) {
        return ResponseEntity.badRequest().body(
                AppResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .data(data)
                        .message(message)
                        .build()
        );
    }

    @Override
    public ResponseEntity<AppResponse> clientError(String message) {
        return clientError(message, null);
    }


    @Override
    public ResponseEntity<AppResponse> serverError(String message) {
        return ResponseEntity.badRequest().body(
                AppResponse.builder()
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .message(message)
                        .build()
        );
    }
}
