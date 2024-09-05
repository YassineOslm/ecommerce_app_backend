package eafc.uccle.be.service;

import eafc.uccle.be.dao.CommentRepository;
import eafc.uccle.be.dao.ProductRepository;
import eafc.uccle.be.dao.UserRepository;
import eafc.uccle.be.dto.CommentDto;
import eafc.uccle.be.dto.ProductRatingDto;
import eafc.uccle.be.entity.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CommentService(CommentRepository commentRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> getComments(Long id, String sortBy, boolean ascending, Pageable pageable) {
        if (!"none".equalsIgnoreCase(sortBy)) {
            Sort sort = Sort.by(ascending ? Sort.Direction.ASC : Sort.Direction.DESC, sortBy);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<Comment> commentsByProductId = commentRepository.findByProductId(id, pageable);
        List<CommentDto> commentsDtoList = commentsByProductId.getContent()
                .stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
        return Map.of(
                "number", commentsByProductId.getNumber(),
                "size", commentsByProductId.getSize(),
                "totalElements", commentsByProductId.getTotalElements(),
                "totalPages", commentsByProductId.getTotalPages(),
                "content", commentsDtoList
        );
    }

    @Transactional
    public void addNewComment(Comment comment) {
        Comment newComment = Comment.builder()
                .comment(comment.getComment())
                .grade(comment.getGrade())
                .dateCreated(new Date())
                .product(productRepository.findById(comment.getProduct().getId()).orElse(null))
                .user(userRepository.findById(comment.getUser().getId()).orElse(null))
                .build();
        commentRepository.save(newComment);
    }

    public Integer getCommentCountByProductId(Long productId) {
        return commentRepository.countByProductId(productId);
    }

    public Double getAverageGradeByProductId(Long productId) {
        return Math.round(commentRepository.findAverageGradeByProductId(productId) * 10.0) / 10.0;
    }

    public Map<Long, Double> getAverageRatingForProducts() {
        List<Object[]> results = commentRepository.findAverageRatingForProducts();

        return results.stream().collect(
                Collectors.toMap(
                        result -> ((Number) result[0]).longValue(),
                        result -> ((Number) result[1]).doubleValue()
                )
        );
    }
}
