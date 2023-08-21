package ru.practicum.shareit.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Common {
    public static final String DT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String X_HEADER_NAME = "X-Sharer-User-Id";
}
