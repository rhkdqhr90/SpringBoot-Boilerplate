package com.community.domain.post.service.strategy;

import com.community.domain.post.dto.request.PostCreateRequest;
import com.community.domain.post.dto.request.PostUpdateRequest;
import com.community.domain.post.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MarketPostStrategy implements PostStrategy{

    private static final int MAX_IMAGES = 10;

    @Override
    public void validateCreate(PostCreateRequest request) {

    }

    @Override
    public void beforeCreate(Post post, PostCreateRequest request) {

    }

    @Override
    public void afterCreate(Post post) {

    }

    @Override
    public void beforeUpdate(Post post, PostUpdateRequest request) {

    }

    @Override
    public void afterUpdate(Post post) {

    }
}
