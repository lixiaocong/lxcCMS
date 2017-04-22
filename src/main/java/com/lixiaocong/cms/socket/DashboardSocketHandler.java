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


package com.lixiaocong.cms.socket;

import com.lixiaocong.cms.repository.IArticleRepository;
import com.lixiaocong.cms.repository.ICommentRepository;
import com.lixiaocong.cms.repository.IUserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class DashboardSocketHandler extends TextWebSocketHandler{

    private final Log log;
    private final IUserRepository userRepository;
    private final IArticleRepository articleRepository;
    private final ICommentRepository commentRepository;

    private ObjectMapper mapper;
    public DashboardSocketHandler(IUserRepository userRepository, IArticleRepository articleRepository, ICommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
        this.log = LogFactory.getLog(getClass().getName());
        this.mapper = new ObjectMapper();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message){
        String payload = message.getPayload();
        log.info(payload);
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(payload);
        } catch (IOException e) {
            //TODO handle exception
            e.printStackTrace();
            return;
        }

        String method = jsonNode.path("method").getValueAsText();
        String result = null;
        switch (method){
            case DashboardCommand.GET_USER_NUMBER:result = handleGetUserNumber();break;
            case DashboardCommand.GET_ARTICLE_NUMBER:result = handleGetArticleNumber();break;
            case DashboardCommand.GET_COMMENT_NUMBER:result = handleGetCommentNumber();break;
            default:break;
        }

        if (result == null)
            return;

        try {
            session.sendMessage(new TextMessage(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String handleGetCommentNumber() {
        long count = commentRepository.count();
        SocketResult socketResult = new SocketResult(DashboardCommand.GET_COMMENT_NUMBER,count);
        try {
            return mapper.writeValueAsString(socketResult);
        } catch (IOException e) {
            //TODO handle exception
            e.printStackTrace();
        }
        return null;
    }

    private String handleGetArticleNumber() {
        long count = articleRepository.count();
        SocketResult socketResult = new SocketResult(DashboardCommand.GET_ARTICLE_NUMBER,count);
        try {
            return mapper.writeValueAsString(socketResult);
        } catch (IOException e) {
            //TODO handle exception
            e.printStackTrace();
        }
        return null;
    }

    private String handleGetUserNumber() {
        long count = userRepository.count();
        SocketResult socketResult = new SocketResult(DashboardCommand.GET_USER_NUMBER,count);
        try {
            return mapper.writeValueAsString(socketResult);
        } catch (IOException e) {
            //TODO handle exception
            e.printStackTrace();
        }
        return null;
    }
}
