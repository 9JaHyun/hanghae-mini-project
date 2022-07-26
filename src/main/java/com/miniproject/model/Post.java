package com.miniproject.model;

import com.miniproject.dto.PostRequestDto;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.nio.file.Path;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@EntityListeners(AuditingEntityListener.class)

public class Post extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

// user 파일 없어서 주석처리
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="user_id", nullable = false)
//    private User user;


//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name="comment_id", nullable = false)
//    private Comment comment;


    @Column(nullable = false)
    private String nickname;

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
        this.imageFileName = requestDto.getImageFileName();
        this.lat = requestDto.getLat();
        this.lng = requestDto.getLng();
        this.address = requestDto.getAddress();
    }
}
