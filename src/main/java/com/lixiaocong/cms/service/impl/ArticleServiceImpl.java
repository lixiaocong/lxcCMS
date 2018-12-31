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

package com.lixiaocong.cms.service.impl;

import com.lixiaocong.cms.entity.Article;
import com.lixiaocong.cms.entity.User;
import com.lixiaocong.cms.repository.IArticleRepository;
import com.lixiaocong.cms.repository.IUserRepository;
import com.lixiaocong.cms.service.IArticleService;
import com.lixiaocong.cms.utils.PageInfo;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ArticleServiceImpl implements IArticleService {
    private Log log = LogFactory.getLog(getClass());

    private final IArticleRepository articleRepository;
    private final IUserRepository userRepository;
    private final RestHighLevelClient elasticClient;

    @Autowired
    public ArticleServiceImpl(IArticleRepository articleRepository, IUserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        // todo add es address to config file
        this.elasticClient = new RestHighLevelClient(RestClient.builder(new HttpHost("data.lixiaocong.com", 9200, "http")));
    }

    @Override
    public Article create(long userId, String title, String content) {
        User user = userRepository.findOne(userId);
        Article article = new Article(title, content, user);
        articleRepository.save(article);
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("id", String.valueOf(article.getId()));
        jsonMap.put("publish_time", String.valueOf(article.getCreateTime().getTime()));
        jsonMap.put("title", article.getTitle());
        IndexRequest indexRequest = new IndexRequest("article", "article", String.valueOf(article.getId())).source(jsonMap);
        try {
            elasticClient.index(indexRequest);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return article;
    }

    @Override
    public void delete(long id) {
        articleRepository.delete(id);
    }

    @Override
    public void update(Article article) {
        articleRepository.save(article);
    }

    @Override
    public PageInfo<Article> get(int page, int size, String keyWord) {
        if (keyWord == null || "".equals(keyWord)) {
            PageRequest request = new PageRequest(page, size, Sort.Direction.DESC, "lastUpdateTime");
            Page<Article> articles = articleRepository.findAll(request);
            PageInfo<Article> pageInfo = new PageInfo<>();
            pageInfo.items = articles.getContent();
            pageInfo.totalItems = articles.getTotalElements();
            return pageInfo;
        } else {
            SearchRequest searchRequest = new SearchRequest();
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.matchQuery("title", keyWord));
            searchSourceBuilder.from(size * page).size(size);
            searchRequest.source(searchSourceBuilder);
            searchRequest.indices("article");
            searchRequest.types("article");
            try {
                SearchResponse searchResponse = elasticClient.search(searchRequest);
                SearchHit[] hits = searchResponse.getHits().getHits();
                List<Long> ids = new LinkedList<>();
                for (SearchHit hit : hits) {
                    ids.add(Long.parseLong(hit.getId()));
                }
                List<Article> all = articleRepository.findAll(ids);
                PageInfo<Article> pageInfo = new PageInfo<>();
                pageInfo.totalItems = searchResponse.getHits().totalHits;
                pageInfo.totalPages = ((pageInfo.totalItems - 1) / size) + 1;
                pageInfo.items = all;
                return pageInfo;
            } catch (IOException e) {
                log.error(e.getMessage());
            }
            return new PageInfo<>();
        }
    }

    @Override
    public Page<Article> getByUser(int page, int size, long userId) {
        return articleRepository.findByUser_Id(userId, new PageRequest(page, size, Sort.Direction.DESC, "lastUpdateTime"));
    }

    @Override
    public Article get(long id) {
        return articleRepository.findOne(id);
    }
}

