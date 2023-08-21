package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.Common;
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
    public ItemRequestResponseDto addItemRequest(@RequestHeader(Common.X_HEADER_NAME) Long userId,
                                                 @RequestBody @Validated ItemRequestDto itemRequestDto) {
        return requestService.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getAllOwnItemRequests(@RequestHeader(Common.X_HEADER_NAME) Long userId) {
        return requestService.getAllOwnItemRequests(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAllOthersItemRequests(@RequestHeader(Common.X_HEADER_NAME) Long userId,
                                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        return requestService.getAllOthersItemRequests(userId, from, size);
    }

    @GetMapping(value = "/{requestId}")
    public ItemRequestResponseDto getItemRequestById(@RequestHeader(Common.X_HEADER_NAME) Long userId,
                                                     @PathVariable Long requestId) {
        return requestService.getRequestById(userId, requestId);
    }

}
