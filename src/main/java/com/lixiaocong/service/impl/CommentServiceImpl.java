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

package com.lixiaocong.service.impl;

import com.lixiaocong.entity.Article;
import com.lixiaocong.entity.Comment;
import com.lixiaocong.entity.User;
import com.lixiaocong.repository.IArticleRepository;
import com.lixiaocong.repository.ICommentRepository;
import com.lixiaocong.repository.IUserRepository;
import com.lixiaocong.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements ICommentService {
    private final ICommentRepository commentRepository;

    private final IArticleRepository articleRepository;

    private final IUserRepository userRepository;

    @Autowired
    public CommentServiceImpl(ICommentRepository commentRepository, IArticleRepository articleRepository, IUserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void create(long articleId, long userId, String content) {
        Article article = articleRepository.findOne(articleId);
        User user = userRepository.findOne(userId);
        commentRepository.save(new Comment(content, article, user));
    }

    @Override
    public void delete(long id) {
        commentRepository.delete(id);
    }

    @Override
    public void update(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    public Page<Comment> get(int page, int size) {
        PageRequest request = new PageRequest(page, size, Sort.Direction.ASC, "lastUpdateTime");
        return commentRepository.findAll(request);
    }

    @Override
    public List<Comment> getByArticle(long articleId) {
        return commentRepository.findByArticle_Id(articleId, new Sort(Sort.Direction.ASC, "createTime"));
    }

    @Override
    public Page<Comment> getByUser(int page, int size, long userId) {
        return commentRepository.findByUser_Id(userId, new PageRequest(page, size, Sort.Direction.ASC, "lastUpdateTime"));
    }

    @Override
    public Comment get(long id) {
        return commentRepository.getOne(id);
    }
}
