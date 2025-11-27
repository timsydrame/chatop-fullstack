package com.chatop.backend.controller;

import com.chatop.backend.dto.MessageRequest;
import com.chatop.backend.dto.MessageResponse;
import com.chatop.backend.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping
    public MessageResponse createMessage(
            Principal principal,
            @Valid @RequestBody MessageRequest request
    ) {
        String email = principal.getName(); // vient du JWT
        return messageService.createMessage(request, email);
    }
}
