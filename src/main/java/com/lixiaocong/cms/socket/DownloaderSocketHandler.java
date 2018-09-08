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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lixiaocong.downloader.DownloadTask;
import com.lixiaocong.downloader.DownloaderException;
import com.lixiaocong.downloader.IDownloader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DownloaderSocketHandler extends TextWebSocketHandler {

    private static final Log log = LogFactory.getLog(DownloaderSocketHandler.class);

    private IDownloader downloader;
    private ObjectMapper mapper;
    private Set<WebSocketSession> webSocketSessions;

    public DownloaderSocketHandler(IDownloader downloader) {
        this.downloader = downloader;
        this.mapper = new ObjectMapper();
        this.webSocketSessions = new HashSet<>();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        this.webSocketSessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        this.webSocketSessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
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

        String method = jsonNode.path("method").asText();
        String result = null;
        switch (method) {
            case DownloaderSocketCommand.ADD_TASK:
                result = handleAddTask(jsonNode);
                break;
            case DownloaderSocketCommand.START_TASK:
                result = handleStartTask(jsonNode);
                break;
            case DownloaderSocketCommand.PAUSE_TASK:
                result = handlePauseTask(jsonNode);
                break;
            case DownloaderSocketCommand.REMOVE_TASK:
                result = handleRemoveTask(jsonNode);
                break;
        }

        if (result == null)
            return;

        try {
            session.sendMessage(new TextMessage(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcast(List<DownloadTask> torrents) {
        this.webSocketSessions.forEach(webSocketSession -> {
            try {
                String data = mapper.writeValueAsString(torrents);
                webSocketSession.sendMessage(new TextMessage(data));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private List<String> getIds(JsonNode jsonNode) {
        JsonNode ids = jsonNode.path("ids");
        List<String> idList = new LinkedList<>();
        ids.forEach(node -> idList.add(node.asText()));
        return idList;
    }

    private String handleAddTask(JsonNode jsonNode) {
        JsonNode addTaskInfo = jsonNode.path("addTaskInfo");
        String taskType = addTaskInfo.path("taskType").asText();
        JsonNode contents = addTaskInfo.get("content");
        contents.forEach(item -> {
            String content = item.asText();
            addSingleTask(taskType, content);
        });
        return null;
    }

    private void addSingleTask(String taskType, String content) {
        try {
            switch (taskType) {
                case "url":
                    downloader.addByUrl(content);
                    break;
                case "torrent":
                    downloader.addByMetainfo(content);
                    break;
                case "metalink":
                    downloader.addByMetalink(content);
                    break;
                default:
                    break;
            }
        } catch (DownloaderException e) {
            //TODO handle exception
            e.printStackTrace();
        }
    }

    private String handleStartTask(JsonNode jsonNode) {
        List<String> ids = getIds(jsonNode);
        try {
            downloader.start(ids);
        } catch (DownloaderException e) {
            //TODO handle exception
            e.printStackTrace();
        }
        return null;
    }

    private String handlePauseTask(JsonNode jsonNode) {
        List<String> ids = getIds(jsonNode);
        try {
            downloader.stop(ids);
        } catch (DownloaderException e) {
            //TODO handle exception
            e.printStackTrace();
        }
        return null;
    }

    private String handleRemoveTask(JsonNode jsonNode) {
        List<String> ids = getIds(jsonNode);
        try {
            downloader.remove(ids);
        } catch (DownloaderException e) {
            //TODO handle exception
            e.printStackTrace();
        }
        return null;
    }

    public void closeAll() {
        this.webSocketSessions.forEach(webSocketSession -> {
            try {
                webSocketSession.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        this.webSocketSessions.clear();
    }
}
