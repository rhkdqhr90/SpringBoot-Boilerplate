package com.community.domain.post.service.strategy;

import com.community.core.exception.ErrorCode;
import com.community.core.exception.custom.BadRequestException;
import com.community.domain.post.dto.request.PostCreateRequest;
import com.community.domain.post.dto.request.PostUpdateRequest;
import com.community.domain.post.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GalleryPostStrategy implements PostStrategy{

    private static final int MIN_IMAGES = 1;
    private static final int MAX_IMAGES = 20;

    @Override
    public void validateCreate(PostCreateRequest request) {
        List<Long> imageIds = request.getImageIds();

        //이미지 검증
        if(imageIds == null || imageIds.isEmpty()) {
            throw new BadRequestException(ErrorCode.IMAGE_REQUIRED);
        }
        //최소 개수 검증
        if (imageIds.size() < MIN_IMAGES) {
            throw new BadRequestException(ErrorCode.TOO_MANY_IMAGES);
        }
        // 최대 개수 검증
        if (imageIds.size() > MAX_IMAGES) {
            throw new BadRequestException(ErrorCode.TOO_MANY_IMAGES, MAX_IMAGES);
        }
    }

    @Override
    public void beforeCreate(Post post, PostCreateRequest request) {
        List<Long> imageIds = request.getImageIds();

        if (imageIds == null || imageIds.isEmpty()) {
            throw new BadRequestException(ErrorCode.IMAGE_REQUIRED);
        }

        if (imageIds.size() < MIN_IMAGES) {
            throw new BadRequestException(ErrorCode.IMAGE_REQUIRED);
        }

        if (imageIds.size() > MAX_IMAGES) {
            throw new BadRequestException(ErrorCode.TOO_MANY_IMAGES, MAX_IMAGES);
        }
    }

    @Override
    public void afterCreate(Post post) {
        if (!post.getImages().isEmpty()) {
            String thumbnailUrl = post.getImages().get(0).getUrl();

            Map<String, Object> extraFields = new HashMap<>();
            extraFields.put("thumbnailUrl", thumbnailUrl);

            post.setExtraFields(extraFields);

            log.debug("thumbnailUrl : {}", thumbnailUrl);
        }
    }

    @Override
    public void beforeUpdate(Post post, PostUpdateRequest request) {
        if (!post.getImages().isEmpty()) {
            String thumbnailUrl = post.getImages().get(0).getUrl();

            Map<String, Object> extraFields = new HashMap<>();
            extraFields.put("thumbnailUrl", thumbnailUrl);

            post.setExtraFields(extraFields);
        }
    }

    @Override
    public void afterUpdate(Post post) {
        log.info("갤러리 게시글 수정 완료: postId={}", post.getId());
    }
}
