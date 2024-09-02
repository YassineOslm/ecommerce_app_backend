package eafc.uccle.be.dto;

import eafc.uccle.be.entity.Comment;
import lombok.Data;

import java.util.Date;

@Data
public class CommentDto {

    private Long id;
    private Long idUser;
    private String userFirstName;
    private String userLastName;
    private String gender;
    private Date dateCreated;
    private String comment;
    private float grade;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.idUser = comment.getUser().getId();
        this.userFirstName = comment.getUser().getFirstname();
        this.userLastName = comment.getUser().getLastname();
        this.gender = comment.getUser().getGender();
        this.dateCreated = comment.getDateCreated();
        this.comment = comment.getComment();
        this.grade = comment.getGrade();
    }
}
