package com.induk.csblog.service;

import com.induk.csblog.domain.Blog;
import com.induk.csblog.domain.Category;
import com.induk.csblog.domain.Comment;
import com.induk.csblog.dto.BlogForm;
import com.induk.csblog.dto.CommentForm;
import com.induk.csblog.repository.CommentRepository;
import com.induk.csblog.util.PaginationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;

    public List<Comment> commentList(){
        return commentRepository.findAll();
    }

    public List<Comment> commentListByBlogId(Long blog_id, PaginationInfo paginationInfo){
        PageRequest pageRequest = PageRequest.of(paginationInfo.getCurrentPageNo()-1, paginationInfo.getRecordsPerPage());
        return commentRepository.findAllByBlog_IdOrderByCreateDateDesc(blog_id, pageRequest);
    }

    public List<Comment> commentListByBlogId(Long blog_id){
        return commentRepository.findByBlog_IdOrderByCreateDateDesc(blog_id);
    }

    public Comment getCommentById(Long commentId){
        return commentRepository.getById(commentId);
    }

    public Comment findById(Long id){
        return commentRepository.findById(id).orElse(null);
    }

    public Long searchByBlogIdCount(Long blogId){
        return commentRepository.countByBlogId(blogId);
    }

    @Transactional
    public Long add(CommentForm commentForm){
        Comment comment;

        if(commentForm.getId() == null) {     //데이터 add
            comment = Comment.createComment(commentForm.getContent(), commentForm.getMember(),
                    commentForm.getBlog());
        }
        else {  //데이터 update
            comment = commentRepository.findById(commentForm.getId()).orElse(null);
            comment.setContent(commentForm.getContent());
        }

        Comment saveComment = commentRepository.save(comment);
        return saveComment.getId();
    }

    @Transactional
    public void del(Long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        comment.ifPresent(delComment ->{
            commentRepository.delete(delComment);
        });
    }
}
