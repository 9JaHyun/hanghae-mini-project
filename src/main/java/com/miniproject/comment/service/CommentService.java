package com.miniproject.comment.service;

import com.miniproject.comment.domain.Comment;
import com.miniproject.comment.dto.CommentRequestDto;
import com.miniproject.comment.dto.CommentResponseDto;
import com.miniproject.comment.repository.CommentRepository;
import com.miniproject.config.security.domain.UserDetailsImpl;
import com.miniproject.post.domain.Post;
import com.miniproject.post.repository.PostRepository;
import com.miniproject.user.domain.User;
import com.miniproject.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void saveComment(String username, Long postId, CommentRequestDto commentRequestDto) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글을 찾을 수 없습니다.")
        );

        User user = userRepository.findByUsername(username)
              .orElseThrow(() -> new UsernameNotFoundException("다시 로그인해 주세요."));

        String content = commentRequestDto.getContent();

        commentRepository.save(Comment.createComment(user, content, post));
    }


    public void deleteComment(Long commentId, UserDetailsImpl userDetails) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("해당하는 게시글을 찾을 수 없습니다."));

        if(userDetails.getUser().getId().equals(comment.getUser().getId())) {
            commentRepository.deleteById(commentId);
        }else {
            throw new IllegalArgumentException("댓글 삭제할 권한이 없습니다");
        }
    }


    public List<CommentResponseDto> listComment(Long postId) {
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        List<Comment> commentList = commentRepository.findAllByPostIdOrderByCreatedAtDesc(postId);

        for (Comment comment : commentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }
}













