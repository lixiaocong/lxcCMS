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

package com.lixiaocong.downloader.aria2c;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lixiaocong.downloader.DownloadTask;
import com.lixiaocong.downloader.DownloaderException;
import com.lixiaocong.downloader.IDownloader;
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

public class Aria2cDownloader implements IDownloader {
    private final Log log = LogFactory.getLog(getClass().getName());
    private final String uri;
    private final String token;

    private HttpClient httpClient;
    private JsonParser jsonParser;

    public Aria2cDownloader(String token) {
        this.token = token;
        this.httpClient = HttpClients.custom().build();
        this.jsonParser = new JsonParser();
        this.uri = "http://127.0.0.1:6800/jsonrpc";
    }

    private String post(Aria2cRequest request) throws DownloaderException {
        Gson gson = new Gson();
        String json = gson.toJson(request);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(entity);
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            log.error(e);
            throw new DownloaderException("network error when post request");
        }

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == SC_OK) {
            try {
                return EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                log.error(e);
                throw new DownloaderException("network error when read post response");
            } finally {
                httpPost.releaseConnection();
            }
        } else {
            log.error("post to " + httpPost.getURI() + " error:" + statusCode);
            try {
                log.error(EntityUtils.toString(response.getEntity()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            throw new DownloaderException("network error with error code " + statusCode);
        }
    }

    @Override
    public boolean addByMetainfo(String meatinfo) throws DownloaderException {
        Aria2cRequest addTorrentRequest = Aria2cReuqestFactory.getAddTorrentRequest(token, meatinfo);
        String resultJson = post(addTorrentRequest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
        return result != null;
    }

    @Override
    public boolean addByMetalink(String meatlink) throws DownloaderException {
        throw new DownloaderException("method not supported");
    }

    @Override
    public boolean addByUrl(String url) throws DownloaderException {
        Aria2cRequest request = Aria2cReuqestFactory.getAddUriRequest(token, url);
        String resultJson = post(request);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
        return result != null;
    }

    @Override
    public boolean remove(String[] ids) throws DownloaderException {
        boolean success = true;
        for (String id : ids) {
            if (!remove(id))
                success = false;
        }
        return success;
    }

    @Override
    public boolean remove(String id) throws DownloaderException {
        Aria2cRequest removeReuqest = Aria2cReuqestFactory.getRemoveReuqest(token, id);
        String resultJson = post(removeReuqest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
        purge();
        return result != null;
    }

    @Override
    public boolean remove() throws DownloaderException {
        boolean success = true;
        List<DownloadTask> downloadTasks = get();

        for (DownloadTask downloadTask : downloadTasks) {
            try {
                remove(downloadTask.getId());
            } catch (DownloaderException e) {
                log.error(e);
                success = false;
            }
        }
        purge();
        return success;
    }

    @Override
    public boolean start(String[] ids) throws DownloaderException {
        boolean success = true;
        for (String id : ids) {
            if (!start(id))
                success = false;
        }
        return success;
    }

    @Override
    public boolean start(String id) throws DownloaderException {
        Aria2cRequest pauseReuqest = Aria2cReuqestFactory.getUnpauseReuqest(token, id);
        String resultJson = post(pauseReuqest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
        return result != null;
    }

    @Override
    public boolean start() throws DownloaderException {
        Aria2cRequest pauseReuqest = Aria2cReuqestFactory.getUnpauseAllReuqest(token);
        String resultJson = post(pauseReuqest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
        return result != null;
    }

    @Override
    public boolean stop(String[] ids) throws DownloaderException {
        boolean success = true;
        for (String id : ids) {
            if (!stop(id))
                success = false;
        }
        return success;
    }

    @Override
    public boolean stop(String id) throws DownloaderException {
        Aria2cRequest pauseReuqest = Aria2cReuqestFactory.getPauseReuqest(token, id);
        String resultJson = post(pauseReuqest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
        return result != null;
    }

    @Override
    public boolean stop() throws DownloaderException {
        Aria2cRequest pauseReuqest = Aria2cReuqestFactory.getPauseAllReuqest(token);
        String resultJson = post(pauseReuqest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        String result = jsonObject.get("result").getAsString();
        return result != null;
    }

    @Override
    public List<DownloadTask> get() throws DownloaderException {
        Aria2cRequest tellActiveReuqest = Aria2cReuqestFactory.getTellActiveReuqest(token);
        String activeResultJson = post(tellActiveReuqest);
        List<DownloadTask> tasks = getTasksFromJson(activeResultJson);

        Aria2cRequest tellWaitingRequest = Aria2cReuqestFactory.getTellWaitingRequest(token);
        String waitingResultJson = post(tellWaitingRequest);
        tasks.addAll(getTasksFromJson(waitingResultJson));

        Aria2cRequest tellStoppedRequest = Aria2cReuqestFactory.getTellStoppedRequest(token);
        String stoppedRequestJson = post(tellStoppedRequest);
        tasks.addAll(getTasksFromJson(stoppedRequestJson));

        return tasks;
    }

    private List<DownloadTask> getTasksFromJson(String tellJson) {
        List<DownloadTask> ret = new LinkedList<>();
        JsonObject jsonObject = (JsonObject) jsonParser.parse(tellJson);
        JsonArray jsonArray = jsonObject.getAsJsonArray("result");
        jsonArray.forEach(element -> {
            DownloadTask task = new DownloadTask();
            JsonObject object = element.getAsJsonObject();
            task.setId(object.get("gid").getAsString());
            JsonObject bitTorrent = object.getAsJsonObject("bittorrent");
            if (bitTorrent != null) {
                task.setDownloadType(DownloadTask.TYPE_TORRENT);
                JsonObject info = bitTorrent.get("info").getAsJsonObject();
                String name = info.get("name").getAsString();
                task.setName(name);
            } else {
                task.setDownloadType(DownloadTask.TYPE_URL);
                task.setName("todo: get file name");
            }
            task.setTotalLength(object.get("totalLength").getAsLong());
            task.setDownloadedLength(object.get("completedLength").getAsLong());
            task.setSpeed(object.get("downloadSpeed").getAsLong());
            String status = object.get("status").getAsString();
            task.setStatus(status);
            task.setFinished(task.getDownloadedLength() == task.getTotalLength());
            ret.add(task);
        });
        return ret;
    }

    private void purge() {
        Aria2cRequest pauseReuqest = Aria2cReuqestFactory.getPargeRquest(token);
        try {
            post(pauseReuqest);
        } catch (DownloaderException e) {
            log.error(e);
        }
    }
}