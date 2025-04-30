package com.delivery.delivery.api.controller;

import com.delivery.delivery.api.dto.AuthorizationDto;
import com.delivery.delivery.core.service.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class SessionController {

    private final SessionService sessionService;

    @PostMapping("/sessions")
    public String createSession(@RequestBody AuthorizationDto session) {
        return sessionService.createSession(session);
    }
}
