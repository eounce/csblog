package com.induk.csblog.service;

import com.induk.csblog.domain.Blog;
import com.induk.csblog.domain.UploadFile;
import com.induk.csblog.dto.BlogForm;
import com.induk.csblog.repository.BlogRepository;
import com.induk.csblog.util.PaginationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {
    private final BlogRepository blogRepository;

    public List<Blog> searchByBlogTitle(Long categoryId,String searchText, PaginationInfo paginationInfo){
        PageRequest pageRequest = PageRequest.of(paginationInfo.getCurrentPageNo()-1, paginationInfo.getRecordsPerPage());
        return blogRepository.findAllByCategoryIdAndTitleContainingOrderByCreateDateDesc(categoryId, searchText, pageRequest);
    }
    public Long searchByBlogTitleCount(Long categoryId, String searchText){
        return blogRepository.countByCategoryIdAndTitleContaining(categoryId, searchText);
    }

    public List<Blog> lastBlogList(){
        return blogRepository.findTop5ByOrderByCreateDateDesc();
    }

    public Blog getBlogById(Long blogId){

        return blogRepository.findById(blogId).orElse(null);
    }
    @Transactional
    public Long add(BlogForm blogForm){
        Blog blog;

        if(blogForm.getId() == null) {     //데이터 add
            blog = Blog.createBlog(blogForm.getTitle(), blogForm.getContent(),
                    blogForm.getImageName(), blogForm.getMember(), blogForm.getCategory());
        }
        else {  //데이터 update
            blog = blogRepository.findById(blogForm.getId()).orElse(null);
            blog.setTitle(blogForm.getTitle());
            blog.setContent(blogForm.getContent());
            if (blogForm.getImageName() != null) blog.setImage(blogForm.getImageName());
        }

        Blog saveBlog = blogRepository.save(blog);
        return saveBlog.getId();
    }
    @Transactional
    public void del(Long blogId){
        Optional<Blog> blog = blogRepository.findById(blogId);
        blog.ifPresent(delBlog ->{
            blogRepository.delete(delBlog);
        });
    }

}
