package com.miniproject.post.domain;

import com.miniproject.comment.domain.Comment;
import com.miniproject.common.BaseEntity;
import com.miniproject.post.dto.PostRequestDto;
import com.miniproject.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@Builder
@NoArgsConstructor (access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Comment> comment = new ArrayList<>();

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private String imageFileName;

    @Column(nullable = false)
    private String lat;

    @Column(nullable = false)
    private String lng;

    @Column(nullable = false)
    private String address;


    public void update(PostRequestDto requestDto) {
        this.contents = requestDto.getContents();
        this.lat = requestDto.getLat();
        this.lng = requestDto.getLng();
        this.address = requestDto.getAddress();
    }

    // Post에서 Comment에 대한 정보 넣기.
    public void addComment(Comment comment) {
        this.comment.add(comment);
    }
}
