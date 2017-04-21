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

package com.lixiaocong.cms.rest;

import com.lixiaocong.cms.entity.Article;
import com.lixiaocong.cms.entity.User;
import com.lixiaocong.cms.exception.RestParamException;
import com.lixiaocong.cms.model.ArticleForm;
import com.lixiaocong.cms.security.DaoBasedUserDetails;
import com.lixiaocong.cms.service.IArticleService;
import com.lixiaocong.cms.service.IUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RolesAllowed("ROLE_USER")
@RestController
@RequestMapping("/article")
public class ArticleController {
    private final IArticleService articleService;
    private final IUserService userService;
    private Log log= LogFactory.getLog(getClass());

    @Autowired
    public ArticleController(IArticleService articleService, IUserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> post(@RequestBody @Valid ArticleForm article, BindingResult result) throws RestParamException {
        if (result.hasErrors()) throw new RestParamException();

        DaoBasedUserDetails user = (DaoBasedUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        articleService.create(user.getId(), article.getTitle(), article.getContent());
        return ResponseMsgFactory.createSuccessResponse();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or isArticleOwner(#id)")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable long id) {
        Article article = articleService.get(id);
        if (article == null) return ResponseMsgFactory.createFailedResponse("文章不存在");
        articleService.delete(id);
        return ResponseMsgFactory.createSuccessResponse();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or isArticleOwner(#id)")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Map<String, Object> put(@PathVariable long id, @RequestBody @Valid ArticleForm article, BindingResult result) throws RestParamException {
        if (result.hasErrors()) throw new RestParamException();

        Article myArticle = articleService.get(id);
        myArticle.setContent(article.getContent());
        myArticle.setTitle(article.getTitle());
        articleService.update(myArticle);
        return ResponseMsgFactory.createSuccessResponse();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> get(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        if (user.isAdmin())
            return ResponseMsgFactory.createSuccessResponse("articles", articleService.get(page - 1, size));
        else
            return ResponseMsgFactory.createSuccessResponse("articles", articleService.getByUser(page - 1, size, user.getId()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Map<String, Object> getByID(@PathVariable long id) {
        return ResponseMsgFactory.createSuccessResponse("article", articleService.get(id));
    }
}
