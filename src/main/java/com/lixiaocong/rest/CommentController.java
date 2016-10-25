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

package com.lixiaocong.rest;

import com.lixiaocong.entity.Comment;
import com.lixiaocong.entity.User;
import com.lixiaocong.exception.RestParamException;
import com.lixiaocong.model.CommentForm;
import com.lixiaocong.service.ICommentService;
import com.lixiaocong.service.IUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RolesAllowed("ROLE_USER")
@RestController
@RequestMapping("/comment")
public class CommentController {
    private final ICommentService commentService;
    private final IUserService userService;
    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    public CommentController(ICommentService commentService, IUserService userService) {
        this.commentService = commentService;
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> post(@RequestParam long articleId, @RequestBody @Valid CommentForm comment, BindingResult result, Principal principal) throws RestParamException {
        if (result.hasErrors()) throw new RestParamException();
        User user = userService.getByUsername(principal.getName());

        commentService.create(articleId, user.getId(), comment.getContent());
        return ResponseMsgFactory.createSuccessResponse();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or isCommentOwner(#id)")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable long id) {
        commentService.delete(id);
        return ResponseMsgFactory.createSuccessResponse();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or isCommentOwner(#id)")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Map<String, Object> put(@PathVariable long id, @RequestBody @Valid CommentForm comment, BindingResult result) throws RestParamException {
        if (result.hasErrors()) throw new RestParamException();

        Comment comment2Update = commentService.get(id);
        if (comment2Update == null) return ResponseMsgFactory.createFailedResponse("评论不存在");
        comment2Update.setContent(comment.getContent());
        commentService.update(comment2Update);
        return ResponseMsgFactory.createSuccessResponse();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> get(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size, Principal principal) {
        User user = userService.getByUsername(principal.getName());
        if (user.isAdmin())
            return ResponseMsgFactory.createSuccessResponse("comments", commentService.get(page - 1, size));
        else
            return ResponseMsgFactory.createSuccessResponse("comments", commentService.getByUser(page - 1, size, user.getId()));
    }
}
