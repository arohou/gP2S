/*
 * Copyright 2018 Genentech Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gene.bioinfo.ms.gp2s.exception;

import com.gene.bioinfo.ms.gp2s.exception.responses.BaseErrorResponse;
import com.gene.bioinfo.ms.gp2s.exception.responses.ValidationError;
import com.netflix.config.validation.ValidationException;
import lombok.NonNull;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@ResponseBody
@ControllerAdvice
public class ExceptionController {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ExceptionController.class);

    @Autowired
    private Environment environment;


    @ExceptionHandler(value = {ValidationException.class, IllegalArgumentException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseErrorResponse badRequestException(Exception e) {
        return new BaseErrorResponse(e.getMessage());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationError validationError(MethodArgumentNotValidException e) {
        return ValidationError.fromBindingErrors(e.getBindingResult());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ValidationError handleMissingParam(MissingServletRequestParameterException e) {
        ValidationError error = new ValidationError(e.getMessage());
        error.addValidationError(e.getParameterName(), e.getMessage());
        return error;
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    public BaseErrorResponse dataConflict(Exception e) {
        LOGGER.info(e.getMessage());
        return new BaseErrorResponse(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)  // 403
    public BaseErrorResponse accessDenied(Exception e) {
        LOGGER.debug(e.getMessage());
        return new BaseErrorResponse(e.getMessage());
    }

    @ExceptionHandler({EmptyResultDataAccessException.class, NoMolecularDaException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)  // 404
    public BaseErrorResponse notFound(Exception e) {
        LOGGER.debug(e.getMessage());
        return new BaseErrorResponse(e.getMessage());
    }

    /**
     * Method catch all internal exception and return JSON object with information.
     *
     * @param e exception
     * @return JSON object with error information
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public BaseErrorResponse baseException(HttpServletRequest req, Exception e) throws Exception {
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        LOGGER.error(e.getMessage(), e);
        LOGGER.error("Request: " + req.getRequestURL() + " raised " + e);
        if (Arrays.stream(environment.getActiveProfiles())
                  .anyMatch(env -> env.equalsIgnoreCase("prod")))
        {
            return new BaseErrorResponse("Internal server error");
        } else {
            return new BaseErrorResponse(e.getMessage());
        }
    }

    @ExceptionHandler(ThirdPartyServiceException.class)
    public ResponseEntity<BaseErrorResponse> thirdPartyServiceException(@NonNull final ThirdPartyServiceException e) {
        return ResponseEntity.status(e.getStatus().is4xxClientError() ? e.getStatus() : HttpStatus.BAD_GATEWAY)
                             .body(new BaseErrorResponse(e.getErrorMessage()));
    }

}
