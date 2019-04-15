package com.gene.bioinfo.ms.gp2s.web;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.gene.bioinfo.ms.gp2s.web.ApiParameters.API_V1;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Api(description = "User info controller", value = "user")
public class UserRestController {

    @RequestMapping(value = API_V1 + "/user", produces = APPLICATION_JSON_UTF8_VALUE)
    public Principal user(Principal user) {
        return user;
    }
}