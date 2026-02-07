package com.community.domain.bookmark.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookmarkToggleResponse {

    private boolean isBookmarked;

    public static BookmarkToggleResponse of(boolean isBookmarked) {
        return BookmarkToggleResponse.builder()
                .isBookmarked(isBookmarked)
                .build();
    }
}
