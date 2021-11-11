package com.induk.csblog.controller;

import com.induk.csblog.domain.Comment;
import com.induk.csblog.domain.Member;
import com.induk.csblog.domain.UploadFile;
import com.induk.csblog.dto.BlogForm;
import com.induk.csblog.dto.CommentForm;
import com.induk.csblog.service.BlogService;
import com.induk.csblog.service.CategoryService;
import com.induk.csblog.service.CommentService;
import com.induk.csblog.service.MemberService;
import com.induk.csblog.util.FileStore;
import com.induk.csblog.util.PaginationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/csblog/blogs")
public class BlogController {
    private final BlogService blogService;
    private final MemberService memberService;
    private final CategoryService categoryService;
    private final FileStore fileStore;
    private final CommentService commentService;

    @GetMapping("/category/{categoryId}")
    public String listForm(@PathVariable Long categoryId, Model model, @ModelAttribute("searchText") String searchText,
                           @RequestParam(value = "page", defaultValue = "1") String page) {
        //페이즈당 레코드 개수, 페이즈 갯수
        PaginationInfo paginationInfo = new PaginationInfo(Integer.parseInt(page), 4, 5);
        paginationInfo.setTotalRecordCount(blogService.searchByBlogTitleCount(categoryId,searchText).intValue());

        model.addAttribute("lastBlogList", blogService.lastBlogList());
        model.addAttribute("blogList", blogService.searchByBlogTitle(categoryId,searchText, paginationInfo));
        model.addAttribute("newLineChar", '\n');
        model.addAttribute("category", categoryService.findById(categoryId));
        model.addAttribute("page", paginationInfo);

        return "blog/blog/listForm";
    }

    @GetMapping("/add/category/{categoryId}")
    public String addForm(@PathVariable Long categoryId, Model model){
        model.addAttribute("category", categoryService.findById(categoryId));
        model.addAttribute("blogForm", new BlogForm());
        return "blog/blog/addForm";
    }

    @PostMapping("/add/category/{categoryId}")
    public String addForm(@PathVariable Long categoryId, @Valid BlogForm blogForm,
                          BindingResult bindingResult, Model model, HttpServletRequest request) throws IOException {

        if(blogForm.getImage().isEmpty() || bindingResult.hasErrors()){
            if(blogForm.getImage().isEmpty())
                bindingResult.rejectValue("image", "imageEmpty", "사진을 등록해 주세요");

            model.addAttribute("category", categoryService.findById(categoryId));
            model.addAttribute("blogForm", blogForm);
            return "blog/blog/addForm";
        }

        UploadFile uploadFile = fileStore.storeFile(blogForm.getImage(), "blog");
        blogForm.setImageName(uploadFile.getStoreFileName());

        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("loginMember");
        blogForm.setMember(member);
        blogForm.setCategory(categoryService.findById(categoryId));

        blogService.add(blogForm);

        return "redirect:/csblog/blogs/category/" + categoryId;
    }

    @GetMapping("/{blogId}")
    public String detailForm(@PathVariable Long blogId, Model model){

        model.addAttribute("lastBlogList", blogService.lastBlogList());
        model.addAttribute("blog", blogService.getBlogById(blogId));
        model.addAttribute("newLineChar", '\n');
        return "blog/blog/detailForm";
    }

    @PostMapping("/comments/addAjax")
    @ResponseBody
    public List<Comment> commentAddForm(@RequestBody  HashMap<String, Object> map) throws IOException {

        CommentForm commentForm = new CommentForm();
        commentForm.setContent((String)map.get("content"));
        commentForm.setBlog(blogService.getBlogById(Long.valueOf(String.valueOf(map.get("blog_id")))));
        commentForm.setMember(memberService.getMemberById(Long.valueOf(String.valueOf(map.get("member_id")))));
        if(map.get("id") != null) {
            commentForm.setId(Long.valueOf(String.valueOf(map.get("id"))));
        }
        commentService.add(commentForm);

        List<Comment> comments = commentService.commentListByBlogId(Long.valueOf(String.valueOf(map.get("blog_id"))));
        System.out.println("comments.get(0).getMember().getName() = " + comments.get(0).getMember().getName());
        
        return comments;
    }

    @PostMapping("/comments/delAjax")
    @ResponseBody
    public List<Comment> commentDelForm(@RequestBody  HashMap<String, Object> map) throws IOException {

        System.out.println(" 1 " + commentService.getCommentById(Long.valueOf(String.valueOf(map.get("commentId")))));
        CommentForm commentForm = CommentForm.commentToCommentForm(commentService.getCommentById(Long.valueOf(String.valueOf(map.get("commentId")))));        System.out.println("2map.get(\"commentId\") = " + map.get("commentId"));
        commentService.del(Long.valueOf(String.valueOf(map.get("commentId"))));
        List<Comment> comments = commentService.commentListByBlogId(commentForm.getBlog().getId());
        System.out.println(comments.size());
        return comments;
    }


    @GetMapping("/edit/{blogId}")
    public String editForm(@PathVariable Long blogId, Model model){
        BlogForm blogForm = BlogForm.blogToBlogFrom(blogService.getBlogById(blogId));

        model.addAttribute("blogForm", blogForm);
        model.addAttribute("category", blogForm.getCategory());
        return "blog/blog/editForm";
    }

    @PostMapping("/edit/{blogId}")
    public String editForm(@Valid BlogForm blogForm, BindingResult bindingResult,
                           @PathVariable Long blogId, Model model) throws IOException {

        BlogForm temp = BlogForm.blogToBlogFrom(blogService.getBlogById(blogId));
        blogForm.setId(temp.getId());
        blogForm.setImageName(temp.getImageName());
        //데이터 체크
        if(bindingResult.hasErrors()){
            model.addAttribute("blogForm", blogForm);
            model.addAttribute("category", temp.getCategory());
            return "blog/blog/editForm";
        }

        if(!blogForm.getImage().isEmpty()){      //사진 업로드 시
            UploadFile uploadFile = fileStore.storeFile(blogForm.getImage(), "blog");
            blogForm.setImageName(uploadFile.getStoreFileName());
        }
        blogService.add(blogForm);

        return "redirect:/csblog/blogs/" + String.valueOf(blogForm.getId());
    }
    @RequestMapping(value = "/del/{blogId}", method = {RequestMethod.POST})
    public String delForm(@PathVariable Long blogId){
        BlogForm blogForm = BlogForm.blogToBlogFrom(blogService.getBlogById(blogId));
        blogService.del(blogId);
        return "redirect:/csblog/blogs/category/" + blogForm.getCategory().getId();
    }

    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename, "blog"));
    }
}
