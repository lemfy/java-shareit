package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestResponseDto createRequest(@RequestHeader(Variables.USER_ID) Integer userId,
                                                 @RequestBody @Validated ItemRequestDto itemRequestDto) {
        return requestService.createRequest(userId, itemRequestDto);
    }

    @GetMapping(value = "/{requestId}")
    public ItemRequestResponseDto getItemRequest(@RequestHeader(Variables.USER_ID) Integer userId,
                                                 @PathVariable Integer requestId) {
        return requestService.getItemRequest(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getAllOwnItemRequests(@RequestHeader(Variables.USER_ID) Integer userId) {
        return requestService.getAllOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllOthersItemRequests(@RequestHeader(Variables.USER_ID) Integer userId,
                                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        return requestService.getAllOthersItemRequests(userId, from, size);
    }
}