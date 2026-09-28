package com.bilheteria.controller;

import com.bilheteria.dto.request.CreateLotRequest;
import com.bilheteria.dto.response.LotResponse;
import com.bilheteria.service.LotService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class LotController {

    private final LotService lotService;

    @PostMapping("/api/v1/events/{eventId}/lots")
    public ResponseEntity<LotResponse> create(
            @PathVariable UUID eventId,
            @Valid @RequestBody CreateLotRequest request) {
        LotResponse body = lotService.create(eventId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/lots/{id}")
                .buildAndExpand(body.id())
                .toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/api/v1/lots/{id}")
    public LotResponse get(@PathVariable UUID id) {
        return lotService.get(id);
    }
}
