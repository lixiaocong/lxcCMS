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

package com.lixiaocong.downloader.aria2c4j;

import com.google.gson.*;
import com.lixiaocong.downloader.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;

public class AriaClient implements IDownloader {
    private final Log log = LogFactory.getLog(getClass().getName());
    private final String uri;
    private final String token;
    private String baseDir;

    private HttpClient httpClient;
    private JsonParser jsonParser;
    private Gson gson;

    public AriaClient(String url, String token, String baseDir) {
        this.uri = url;
        this.token = token;
        this.baseDir = baseDir;
        if (!this.baseDir.endsWith("/"))
            this.baseDir += "/";

        this.httpClient = HttpClients.custom().build();
        this.jsonParser = new JsonParser();
        this.gson = new Gson();
    }

    private String post(AriaRequest request) throws DownloaderException {
        String json = gson.toJson(request);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(entity);
        String responseEntity;
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
            responseEntity = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (IOException e) {
            log.error(e);
            throw new DownloaderException("network error");
        }

        if (response.getStatusLine().getStatusCode() == SC_OK)
            return responseEntity;
        else {
            AriaErrorResponse ariaErrorResponse = gson.fromJson(responseEntity, AriaErrorResponse.class);
            throw new DownloaderException(ariaErrorResponse.getMessage());
        }
    }

    @Override
    public void addByMetainfo(String metainfo) throws DownloaderException {
        AriaRequest addTorrentRequest = AriaReuqestFactory.getAddTorrentRequest(token, metainfo, baseDir);
        String resultJson = post(addTorrentRequest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
    }

    @Override
    public void addByMetalink(String metalink) throws DownloaderException {
        throw new DownloaderException("method not supported");
    }

    @Override
    public void addByUrl(String url) throws DownloaderException {
        AriaRequest request = AriaReuqestFactory.getAddUriRequest(token, url);
        String resultJson = post(request);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
    }

    @Override
    public void remove(List<String> ids) throws DownloaderException {
        for (String id : ids) {
            remove(id);
        }
    }

    @Override
    public void remove(String id) throws DownloaderException {
        DownloadTask downloadTask = get(id);
        if (downloadTask.getStatus() == DownloadStatus.ACTIVE) {
            AriaRequest removeRequest = AriaReuqestFactory.getRemoveReuqest(token, id);
            post(removeRequest);
        }
        AriaRequest removeResultRequest = AriaReuqestFactory.getRemoveResultReuqest(token, id);
        post(removeResultRequest);
    }

    @Override
    public void remove() throws DownloaderException {
        List<DownloadTask> downloadTasks = get();

        for (DownloadTask downloadTask : downloadTasks) {
            try {
                remove(downloadTask.getId());
            } catch (DownloaderException e) {
                log.error(e);
            }
        }
    }

    @Override
    public void start(List<String> ids) throws DownloaderException {
        boolean success = true;
        for (String id : ids) {
            start(id);
        }
    }

    @Override
    public void start(String id) throws DownloaderException {
        AriaRequest pauseReuqest = AriaReuqestFactory.getUnpauseReuqest(token, id);
        String resultJson = post(pauseReuqest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
    }

    @Override
    public void start() throws DownloaderException {
        AriaRequest pauseReuqest = AriaReuqestFactory.getUnpauseAllReuqest(token);
        String resultJson = post(pauseReuqest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
    }

    @Override
    public void stop(List<String> ids) throws DownloaderException {
        for (String id : ids) {
            stop(id);
        }
    }

    @Override
    public void stop(String id) throws DownloaderException {
        AriaRequest pauseReuqest = AriaReuqestFactory.getPauseReuqest(token, id);
        String resultJson = post(pauseReuqest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
    }

    @Override
    public void stop() throws DownloaderException {
        AriaRequest pauseReuqest = AriaReuqestFactory.getPauseAllReuqest(token);
        String resultJson = post(pauseReuqest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
    }

    @Override
    public DownloadTask get(String gid) throws DownloaderException {
        AriaRequest tellStatusRequest = AriaReuqestFactory.getTellStatusRequest(token, gid);
        String resultJson = post(tellStatusRequest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        JsonObject result = jsonObject.get("result").getAsJsonObject();
        return getTaskFromJson(result);
    }

    @Override
    public List<DownloadTask> get() throws DownloaderException {
        AriaRequest tellActiveReuqest = AriaReuqestFactory.getTellActiveReuqest(token);
        String activeResultJson = post(tellActiveReuqest);
        List<DownloadTask> tasks = getTasksFromJson(activeResultJson);

        AriaRequest tellWaitingRequest = AriaReuqestFactory.getTellWaitingRequest(token);
        String waitingResultJson = post(tellWaitingRequest);
        tasks.addAll(getTasksFromJson(waitingResultJson));

        AriaRequest tellStoppedRequest = AriaReuqestFactory.getTellStoppedRequest(token);
        String stoppedRequestJson = post(tellStoppedRequest);
        tasks.addAll(getTasksFromJson(stoppedRequestJson));

        return tasks;
    }

    private List<DownloadTask> getTasksFromJson(String tellJson) {
        List<DownloadTask> ret = new LinkedList<>();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(tellJson);
        JsonArray jsonArray = jsonObject.getAsJsonArray("result");
        jsonArray.forEach(element -> {
            JsonObject result = element.getAsJsonObject();
            ret.add(getTaskFromJson(result));
        });
        return ret;
    }

    private DownloadTask getTaskFromJson(JsonObject result) {
        String id = result.get("gid").getAsString();
        DownloadStatus status = handleStatus(result);
        DownloadType type = handleType(result);
        String name = handleName(result);
        long totalLength = result.get("totalLength").getAsLong();
        long downloadedLength = result.get("completedLength").getAsLong();
        long downloadSpeed = result.get("downloadSpeed").getAsLong();
        long uploadLength = result.get("uploadLength").getAsLong();
        long uploadSpeed = result.get("uploadSpeed").getAsLong();
        String dir = result.get("dir").getAsString();
        List<DownloadFile> files = handleFiles(result);

        return new DownloadTask(id, status, type, name, totalLength, downloadedLength, downloadSpeed, uploadLength, uploadSpeed, dir, files);
    }

    private List<DownloadFile> handleFiles(JsonObject result) {
        JsonArray files = result.getAsJsonArray("files");
        List<DownloadFile> ret = new LinkedList<>();
        files.forEach(element -> {
            JsonObject fileJsonObject = element.getAsJsonObject();
            DownloadFile file = handleFile(fileJsonObject);
            ret.add(file);
        });
        return ret;
    }

    private DownloadFile handleFile(JsonObject fileJsonObject) {
        String path = fileJsonObject.get("path").getAsString();
        int index = path.lastIndexOf("/");
        String name = path.substring(index + 1);//if there is no /, then use full name

        long totalLength = fileJsonObject.get("length").getAsLong();
        long downloadLength = fileJsonObject.get("completedLength").getAsLong();
        return new DownloadFile(name, path, totalLength, downloadLength);
    }

    private String handleName(JsonObject result) {
        JsonObject bitTorrent = result.getAsJsonObject("bittorrent");
        if (bitTorrent != null) {
            JsonObject info = bitTorrent.get("info").getAsJsonObject();
            return info.get("name").getAsString();
        } else {
            JsonArray files = result.getAsJsonArray("files");
            if (files.size() == 0)
                return "error: file empty";
            else {
                JsonElement file = files.get(0);
                String filepath = file.getAsJsonObject().get("path").getAsString();
                int index = filepath.lastIndexOf("/");
                return filepath.substring(++index);
            }
        }
    }

    private DownloadType handleType(JsonObject result) {
        JsonObject bitTorrent = result.getAsJsonObject("bittorrent");
        if (bitTorrent == null)
            return DownloadType.URL;
        return DownloadType.TORRENT;
    }

    private DownloadStatus handleStatus(JsonObject result) {
        String status = result.get("status").getAsString();
        switch (status) {
            case "active":
                return DownloadStatus.ACTIVE;
            case "waiting":
                return DownloadStatus.WAITING;
            case "paused":
                return DownloadStatus.PAUSED;
            case "error":
                return DownloadStatus.ERROR;
            case "complete":
                return DownloadStatus.COMPLETED;
            default:
                return DownloadStatus.OTHER;
        }
    }
}