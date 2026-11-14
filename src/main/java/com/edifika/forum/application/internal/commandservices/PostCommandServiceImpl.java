package com.edifika.forum.application.internal.commandservices;

import com.edifika.forum.infrastructure.authorization.sfs.CurrentUser;
import com.edifika.forum.domain.model.aggregates.Post;
import com.edifika.forum.domain.services.PostCommandService; // 👈 IMPORTAMOS TU NUEVA INTERFAZ
import com.edifika.forum.infrastructure.persistence.jpa.repositories.PostRepository;
import com.edifika.forum.interfaces.rest.resources.CreatePostResource;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class PostCommandServiceImpl implements PostCommandService {

    private final PostRepository postRepository;

    public PostCommandServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Post handle(CreatePostResource resource, String uploadedImageUrl) {
        // El autor sale del JWT (user_id y rol); el residentId del body se ignora.
        Long residentId = CurrentUser.id();
        if (residentId == null) {
            throw new IllegalArgumentException("El token no identifica al usuario (falta user_id)");
        }
        // Tu lógica exacta y perfecta de la restricción diaria:
        postRepository.findFirstByResidentIdOrderByCreatedAtDesc(residentId)
                .ifPresent(lastPost -> {
                    LocalDate lastPostDate = lastPost.getCreatedAt().toLocalDate();
                    if (lastPostDate.equals(LocalDate.now())) {
                        throw new IllegalArgumentException("Restricción del mini foro: Cada usuario solo podrá realizar una publicación diaria.");
                    }
                });

        Post post = new Post();
        post.setTitle(resource.title());
        post.setContent(resource.content());
        post.setResidentId(residentId);
        post.setAuthorRole(CurrentUser.role());
        post.setImageUrl(uploadedImageUrl);

        return postRepository.save(post);
    }
}