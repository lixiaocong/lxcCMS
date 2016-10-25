/*
  BSD 3-Clause License

  Copyright (c) 2016, lixiaocong(lxccs@iCloud.com)
  All rights reserved.

  Redistribution and use in source and binary forms, with or without
  modification, are permitted provided that the following conditions are met:

  * Redistributions of source code must retain the above copyright notice, this
    list of conditions and the following disclaimer.

  * Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

  * Neither the name of the copyright holder nor the names of its
    contributors may be used to endorse or promote products derived from
  this software without specific prior written permission.

  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
  AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
  IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
  FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
  CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
  OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.lixiaocong.controller;

import com.lixiaocong.entity.Article;
import com.lixiaocong.entity.User;
import com.lixiaocong.exception.ControllerParamException;
import com.lixiaocong.model.ArticleForm;
import com.lixiaocong.model.CommentForm;
import com.lixiaocong.service.IArticleService;
import com.lixiaocong.service.ICommentService;
import com.lixiaocong.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/blog")
public class BlogController {
    private final IArticleService articleService;
    private final ICommentService commentService;
    private final IUserService userService;

    @Autowired
    public BlogController(IArticleService articleService, ICommentService commentService, IUserService userService) {
        this.articleService = articleService;
        this.commentService = commentService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView blog(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        ModelAndView ret = new ModelAndView("blog/list");

        //处理文章,默认每页10篇
        Page<Article> articles = articleService.get(page - 1, size);
        ret.addObject("articles", articles.getContent());

        //处理分页
        int pageNumber = articles.getTotalPages();
        if (pageNumber == 0) pageNumber++;

        int pageMin = page - 5 > 1 ? page - 5 : 1;
        int pageMax = page + 5 > pageNumber ? pageNumber : page + 5;
        ret.addObject("pageCurr", page);
        ret.addObject("pageMin", pageMin);
        ret.addObject("pageMax", pageMax);
        return ret;
    }

    @RolesAllowed("ROLE_USER")
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView post(@Valid ArticleForm articleForm, BindingResult result, Principal principal) throws ControllerParamException {
        if (result.hasErrors()) throw new ControllerParamException();

        User user = userService.getByUsername(principal.getName());
        Article article = articleService.create(user.getId(), articleForm.getTitle(), articleForm.getContent());
        return new ModelAndView("redirect:/blog/detail?id=" + article.getId());
    }

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ModelAndView detail(@RequestParam long id) {
        ModelAndView ret = new ModelAndView("/blog/detail");
        ret.addObject("article", articleService.get(id));
        ret.addObject("comments", commentService.getByArticle(id));
        return ret;
    }

    @RolesAllowed("ROLE_USER")
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.POST)
    public ModelAndView comment(@PathVariable long id, @Valid CommentForm comment, BindingResult result, Principal principal) throws ControllerParamException {
        if (result.hasErrors()) throw new ControllerParamException();

        User user = userService.getByUsername(principal.getName());
        commentService.create(id, user.getId(), comment.getContent());
        return new ModelAndView("redirect:/blog/detail?id=" + id);
    }

    @RolesAllowed("ROLE_USER")
    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView edit() {
        return new ModelAndView("/blog/edit");
    }
}
