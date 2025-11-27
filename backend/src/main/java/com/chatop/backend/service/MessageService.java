package com.chatop.backend.service;

import com.chatop.backend.dto.MessageRequest;
import com.chatop.backend.dto.MessageResponse;
import com.chatop.backend.model.Message;
import com.chatop.backend.model.Rental;
import com.chatop.backend.model.User;
import com.chatop.backend.repository.MessageRepository;
import com.chatop.backend.repository.RentalRepository;
import com.chatop.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RentalRepository rentalRepository;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public MessageResponse createMessage(MessageRequest request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        Rental rental = rentalRepository.findById(request.getRental_id())
                .orElseThrow(() -> new RuntimeException("RENTAL_NOT_FOUND"));

        Message message = Message.builder()
                .message(request.getMessage())
                .user(user)
                .rental(rental)
                .createdAt(LocalDate.now().format(FORMATTER))
                .build();

        messageRepository.save(message);

        return new MessageResponse("Message send with success");
    }
}
