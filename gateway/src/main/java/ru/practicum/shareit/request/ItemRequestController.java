package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader(Variables.USER_ID) Long userId,
                                                 @RequestBody @Validated ItemRequestDto itemRequestDto) {
        log.info("Add item request={} by userId={}", itemRequestDto, userId);
        return itemRequestClient.createRequest(userId, itemRequestDto);
    }

    @GetMapping(value = "/{requestId}")
    public ResponseEntity<Object> getItemRequest(@RequestHeader(Variables.USER_ID) Long userId,
                                                     @PathVariable Long requestId) {
        log.info("Get item requestId={} by userId={}", requestId, userId);
        return itemRequestClient.getItemRequest(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllOwnItemRequests(@RequestHeader(Variables.USER_ID) Long userId) {
        log.info("Get all own item requests by userId={}", userId);
        return itemRequestClient.getAllOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllOthersItemRequests(@RequestHeader(Variables.USER_ID) Long userId,
                                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Get other item requests by userId={}, from={}, size={}", userId, from, size);
        return itemRequestClient.getAllOthersItemRequests(userId, from, size);
    }
}