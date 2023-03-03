package com.project.backend.common.response;

import com.project.backend.common.models.AppResponse;
import org.springframework.http.ResponseEntity;

public interface MainResponse {

    ResponseEntity<AppResponse> success(Object data);

    ResponseEntity<AppResponse> clientError(String message, Object data);

    ResponseEntity<AppResponse> clientError(String message);

    ResponseEntity<AppResponse> serverError(String message);

}
