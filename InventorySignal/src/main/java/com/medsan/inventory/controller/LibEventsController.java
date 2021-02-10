package com.medsan.inventory.controller;

import com.medsan.inventory.domain.LibEvennt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LibEventsController {

    @PostMapping("/publish")
    public ResponseEntity<LibEvennt> postlibEntity(@RequestBody LibEvennt libEvennt) {


        log.info("Before LibEventsController >>>");
        return ResponseEntity.status(HttpStatus.CREATED).body(libEvennt);
    }

}