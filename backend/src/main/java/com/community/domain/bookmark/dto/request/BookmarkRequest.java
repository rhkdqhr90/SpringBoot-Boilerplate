package com.community.domain.bookmark.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@NoArgsConstructor
public class BookmarkRequest {
    private String folder;
}
