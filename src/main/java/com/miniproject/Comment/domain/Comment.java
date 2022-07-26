package com.miniproject.Comment.domain;



import com.miniproject.Comment.Dto.CommentResponseDto;
import lombok.*;

import javax.persistence.*;



@Getter
@Setter
@NoArgsConstructor
@Entity

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 id를 자동 생성
    @Column(name = "comment_id")
    private Long commentId;

    @Column
    private String content;

    @Column
    private String nickname;
    @Column
    private String username;


    @ManyToOne
    @JoinColumn(name ="post_id")
    private Post post;

    @Column
    private Long modifiedAt;




    public Comment(Post post, String content) {
        this.post=post;
        this.content=content;
    }


}
