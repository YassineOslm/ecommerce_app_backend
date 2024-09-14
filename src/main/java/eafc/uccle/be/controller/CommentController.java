package eafc.uccle.be.controller;

import eafc.uccle.be.dto.CommentDto;
import eafc.uccle.be.entity.Comment;
import eafc.uccle.be.service.CommentService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin("http://localhost:4200")
@RestController
@RequestMapping("/api/products")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Map<String, Object>> getComments(
            @PathVariable Long id,
            @RequestParam(defaultValue = "none") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending, 
            Pageable pageable) {
        Map<String, Object> response = commentService.getComments(id, sortBy, ascending, pageable);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/addComment")
    public ResponseEntity<Map<String, Object>> addNewComment(@RequestBody Comment comment) {
        commentService.addNewComment(comment);
        Map<String, Object> response = Map.of(
                "content", "added"
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/commentCount")
    public ResponseEntity<Map<String, Object>> getCommentCountByProductId(@PathVariable Long id) {
        Integer count = commentService.getCommentCountByProductId(id);
        Map<String, Object> response = Map.of(
                "id", id,
                "commentCounts", count
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/averageGrade")
    public ResponseEntity<Map<String, Object>> getAverageGradeByProductId(@PathVariable Long id) {
        Double averageGrade = commentService.getAverageGradeByProductId(id);
        Map<String, Object> response = Map.of(
                "id", id,
                "averageGrade", averageGrade
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/average-ratings")
    public ResponseEntity<Map<Long, Double>> getAverageRatingForProducts() {
        Map<Long, Double> averageRatings = commentService.getAverageRatingForProducts();
        return new ResponseEntity<>(averageRatings, HttpStatus.OK);
    }

    @GetMapping("comments/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        CommentDto commentDto = commentService.getCommentById(id);
        return ResponseEntity.ok(commentDto);
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody CommentDto commentDto) {
        Comment updatedComment = commentService.updateComment(id, commentDto);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteCommentById(id);
        return ResponseEntity.noContent().build();
    }

}
