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

package com.gene.bioinfo.ms.gp2s.web;

import com.gene.bioinfo.ms.gp2s.domain.Comment;
import com.gene.bioinfo.ms.gp2s.domain.base.EntityType;
import com.gene.bioinfo.ms.gp2s.infrastructure.security.user.UserProvider;
import com.gene.bioinfo.ms.gp2s.service.CommentService;
import com.gene.bioinfo.ms.gp2s.service.attachment.CommentAttachmentService;
import com.gene.bioinfo.ms.gp2s.web.attachment.AttachmentRestController;
import com.gene.bioinfo.ms.gp2s.web.validator.CommentValidator;
import com.netflix.config.validation.ValidationException;
import io.swagger.annotations.Api;
import lombok.NonNull;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @author Przemysław Stankowski on 14.02.2018
 */
@CrossOrigin
@RestController
@Api(description = "Comments management", value = "comment")
@RequestMapping(value = API_V1 + "/comment", produces = APPLICATION_JSON_UTF8_VALUE)
public class CommentRestController extends AttachmentRestController {

    private final Validator validator;
    private final CommentService commentService;
    private final UserProvider userProvider;

    @Autowired
    protected CommentRestController(@NonNull final CommentService commentService,
                                    @NonNull final CommentValidator validator,
                                    @NonNull final UserProvider userProvider,
                                    @NonNull final CommentAttachmentService attachmentService) {
        super(attachmentService);
        this.validator = validator;
        this.commentService = commentService;
        this.userProvider = userProvider;
    }

    @NonNull
    @GetMapping("/{entityType}/{entityId}")
    @ResponseBody
    public List<Comment> getComments(@NonNull @PathVariable final String entityType,
                                     @NonNull @PathVariable final Long entityId) {
        if (!EnumUtils.isValidEnum(EntityType.class, entityType.toUpperCase())) {
            throw new ValidationException(String.format("Invalid entity type: '%s'", entityType));
        }
        return this.commentService.getComments(entityId, EntityType.valueOf(entityType.toUpperCase()));
    }

    @NonNull
    @GetMapping("/{entityType}/{entityId}/count")
    @ResponseBody
    public Integer countComments(@NonNull @PathVariable final String entityType,
                                 @NonNull @PathVariable final Long entityId) {
        if (!EnumUtils.isValidEnum(EntityType.class, entityType.toUpperCase())) {
            throw new ValidationException(String.format("Invalid entity type: '%s'", entityType));
        }
        return this.commentService.countComments(entityId, EntityType.valueOf(entityType.toUpperCase()));
    }

    @NonNull
    @PostMapping("/{entityType}/{entityId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Comment addComment(@NonNull @PathVariable final String entityType,
                              @NonNull @PathVariable final Long entityId,
                              @NonNull @Valid @RequestBody final String content) {
        if (!EnumUtils.isValidEnum(EntityType.class, entityType.toUpperCase())) {
            throw new ValidationException(String.format("Invalid entity type: '%s'", entityType));
        }
        return this.commentService.addComment(entityId, EntityType.valueOf(entityType.toUpperCase()), content);
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@NonNull @PathVariable final Long id) {
        this.checkOwnership(id);
        this.commentService.deleteComment(id);
    }

    @NonNull
    @ResponseBody
    @PutMapping("/{id}")
    public Comment updateComment(@NonNull @PathVariable final Long id,
                                 @RequestParam(value = "modified", required = false) Boolean forceSetModified,
                                 @NonNull @Valid @RequestBody final String content) {
        this.checkOwnership(id);
        return this.commentService.updateComment(id, content, forceSetModified);
    }

    protected void checkOwnership(@NonNull final Long id) {
        //Do not check owner when security is disabled
        if (!this.userProvider.getSecurityEnabled()) {
            return;
        }
        final Comment comment = this.commentService.getComment(id);
        if (!this.userProvider.getCurrentAuditor().equalsIgnoreCase(comment.getCreatedBy())) {
            throw new AccessDeniedException("Comment ownership violation");
        }
    }

    @InitBinder
    @SuppressWarnings("unused")
    protected void initBinder(@NonNull final WebDataBinder binder) {
        binder.setValidator(validator);
    }
}
