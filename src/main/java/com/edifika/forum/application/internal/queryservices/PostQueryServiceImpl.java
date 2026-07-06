package com.edifika.forum.application.internal.queryservices;

import com.edifika.forum.domain.model.aggregates.Post;
import com.edifika.forum.domain.services.PostQueryService;
import com.edifika.forum.infrastructure.persistence.jpa.repositories.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class PostQueryServiceImpl implements PostQueryService {
n    private final PostRepository postRepository;
n    public PostQueryServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
n    @Override
    public Page<Post> list(Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        } else if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
n    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }
}