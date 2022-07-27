package com.miniproject.post.model;

import com.miniproject.common.BaseEntity;
import com.miniproject.post.dto.ImageRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Image extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long imageId;

    @Column(nullable = false)
    private String imageFileName;

    @Column(nullable = false)
    private String uploadImageUrl;


    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Image(String imageFileName, String uploadImageUrl) {
        this.imageFileName = imageFileName;
        this.uploadImageUrl = uploadImageUrl;
    }

    public Image(ImageRequestDto requestDto) {
        this.imageFileName = requestDto.getImageFileName();
        this.uploadImageUrl = requestDto.getUploadImageUrl();

    }
}
