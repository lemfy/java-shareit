package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Variables;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestResponseDto createRequest(@RequestHeader(Variables.USER_ID) Long userId,
                                                 @RequestBody @Validated ItemRequestDto itemRequestDto) {
        return requestService.createRequest(userId, itemRequestDto);
    }

    @GetMapping(value = "/{requestId}")
    public ItemRequestResponseDto getItemRequest(@RequestHeader(Variables.USER_ID) Long userId,
                                                 @PathVariable Long requestId) {
        return requestService.getItemRequest(userId, requestId);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getAllOwnItemRequests(@RequestHeader(Variables.USER_ID) Long userId) {
        return requestService.getAllOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllOthersItemRequests(@RequestHeader(Variables.USER_ID) Long userId,
                                                                 @RequestParam(defaultValue = "0") int from,
                                                                 @RequestParam(defaultValue = "10") int size) {
        return requestService.getAllOthersItemRequests(userId, from, size);
    }
}