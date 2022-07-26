package com.miniproject.Comment.Service;

import com.miniproject.Comment.Dto.CommentRequestDto;
import com.miniproject.Comment.Dto.CommentResponseDto;
import com.miniproject.Comment.Dto.CommentResponselistDto;
import com.miniproject.Comment.Dto.UserContentResponseDto;
import com.miniproject.Comment.Repository.CommentRepository;
import com.miniproject.Comment.Repository.PostRepository;
import com.miniproject.Comment.domain.Comment;
import com.miniproject.Comment.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    @Transactional
    public Comment saveComment(Long postId, CommentRequestDto commentRequestDto) {

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글을 찾을 수 없습니다.")
        );

        String content = commentRequestDto.getContent();

        Comment savecomment = new Comment(post, content);

        commentRepository.save(savecomment);
        return savecomment;
    }


    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }


    public List<CommentResponseDto> listcomment(Long postId) {


        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
//        List<CommentResponselistDto> CommentResponselistDto = new ArrayList<>();
//        List<UserContentResponseDto> userContentResponseDto = new ArrayList<>();

        List<Comment> commentList = commentRepository.findAllByPost(postId);

        for (Comment comment : commentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }


        return commentResponseDtoList;







    }
}













