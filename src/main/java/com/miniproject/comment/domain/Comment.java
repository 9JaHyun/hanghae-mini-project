package com.miniproject.comment.domain;

import com.miniproject.common.BaseEntity;
import com.miniproject.post.domain.Post;
import com.miniproject.user.domain.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //DB가 id를 자동 생성
    @Column(name = "comment_id")
    private Long commentId;

    @Column
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private Comment(User user, String content, Post post) {
        this.user = user;
        this.post = post;
        this.content = content;
    }

    public void addCommentToPost(Post post) {
        post.addComment(this);
    }
    // Comment 생성
    public static Comment createComment(User user, String content, Post post) {
        Comment comment = new Comment(user, content, post);
        comment.addCommentToPost(post);
        return comment;
    }



}
