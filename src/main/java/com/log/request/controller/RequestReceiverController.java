package com.log.request.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@RestController
@RequestMapping("api")
public class RequestReceiverController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestReceiverController.class);

    @PostMapping("/*")
    public ResponseEntity receiveRequest(HttpServletRequest httpRequest) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            httpRequest.getReader().lines().forEach(stringBuilder::append);
            LOGGER.info("Logged Request {}",stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
       return new ResponseEntity<>(HttpStatus.OK);
    }
}
