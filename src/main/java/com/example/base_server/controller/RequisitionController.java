package com.example.base_server.controller;

import com.example.base_server.dto.RequisitionDTO;
import com.example.base_server.dto.UserDTO;
import com.example.base_server.model.User;
import com.example.base_server.service.RequisitionService;
import com.example.base_server.service.UserService;
import com.example.base_server.utils.UserExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/requisition")
public class RequisitionController {

    @Autowired
    private RequisitionService requisitionService;

    @Autowired
    private UserExtractor userExtractor;

    @PostMapping("/new")
    public ResponseEntity<String> newRequisition(Authentication authentication,
                                @RequestParam String title,
                                 @RequestParam String author) {
        User user = userExtractor.getUserFromAuth(authentication);
        if (user == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authenticated.");
        }

        RequisitionDTO newRequisition = new RequisitionDTO(requisitionService.saveRequisition(title, author, user));

        return ResponseEntity.status(HttpStatus.CREATED).body("Requisition successfully created!\n" + newRequisition.toString());
    }
}
