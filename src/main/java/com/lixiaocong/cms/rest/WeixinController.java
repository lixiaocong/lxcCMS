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
import com.lixiaocong.cms.service.IArticleService;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutNewsMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weixin")
public class WeixinController {
    private Log log = LogFactory.getLog(getClass());
    private IArticleService articleService;

    @Autowired
    public WeixinController(@Value("${weixin.id}") String id, @Value("${weixin.secret}") String secret, @Value("${weixin.token") String token, @Value("${weixin.key") String key, IArticleService articleService) {
        this.articleService = articleService;

        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(id);
        config.setSecret(secret);
        config.setToken(token);
        config.setAesKey(key);

        WxMpService wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(config);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String doGet(String signature, String timestamp, String nonce, String echostr) {
        return echostr;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String doPost(@RequestBody String xml) {
        WxMpXmlMessage message = WxMpXmlMessage.fromXml(xml);
        switch (message.getMsgType()) {
            default:
                WxMpXmlOutNewsMessage m = WxMpXmlOutMessage.NEWS().fromUser(message.getToUserName()).toUser(message.getFromUserName()).build();

                Page<Article> articles = articleService.get(0, 4);
                for (Article article : articles) {
                    WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
                    item.setDescription(article.getTitle());
                    if (article.getImage() == null) item.setPicUrl("http://www.lixiaocong.com/image/logo.png");
                    else item.setPicUrl(article.getImage());
                    item.setTitle(article.getTitle());
                    item.setUrl("http://www.lixiaocong.com/blog/detail?id=" + article.getId());
                    m.addArticle(item);
                }
                return m.toXml();
        }
    }
}