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

package com.lixiaocong.downloader.Aria2c;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.lixiaocong.downloader.DownloadTask;
import com.lixiaocong.downloader.DownloaderException;
import com.lixiaocong.downloader.IDownloader;
import com.lixiaocong.transmission4j.exception.JsonException;
import com.lixiaocong.transmission4j.utils.JsonUtil;
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
import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;

public class Aria2cDownloader implements IDownloader {
    private final Log log = LogFactory.getLog(getClass().getName());
    private final String uri = "http://127.0.0.1:6800/jsonrpc";
    private String token;

    private HttpClient httpClient;
    private JsonParser jsonParser;

    public Aria2cDownloader(String token) {
        this.token = token;
        this.httpClient = HttpClients.custom().build();
        this.jsonParser = new JsonParser();
    }

    private String post(Aria2cRequest request) throws DownloaderException {
        String json;
        try {
            json = JsonUtil.getJson(request);
        } catch (JsonException e) {
            log.error(e);
            throw new DownloaderException("Gen Json Error");
        }

        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(entity);
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            log.error(e);
            throw new DownloaderException("network error when post request");
        }finally {
            httpPost.releaseConnection();
        }

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == SC_OK) {
            try {
                return EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                log.error(e);
                throw new DownloaderException("network error when read post response");
            }
        } else {
            log.error("post to " + httpPost.getURI() + " error:" + statusCode);
            throw new DownloaderException("network error with error code " + statusCode);
        }
    }

    @Override
    public boolean addByMetainfo(String meatinfo) throws DownloaderException {
        Aria2cRequest addTorrentRequest = Aria2cReuqestFactory.getAddTorrentRequest(token, meatinfo);
        String resultJson = post(addTorrentRequest);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        JsonElement result = jsonObject.get("result");
        return result == null;
    }

    @Override
    public boolean addByMetalink(String meatlink) {
        return false;
    }

    @Override
    public boolean addByUrl(String url) throws DownloaderException {
        Aria2cRequest request = Aria2cReuqestFactory.getAddUriRequest(token, url);
        String resultJson = post(request);
        JsonObject jsonObject = (JsonObject) jsonParser.parse(resultJson);
        JsonElement result = jsonObject.get("result");
        return result == null;
    }

    @Override
    public boolean remove(String[] ids) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return false;
    }

    @Override
    public boolean remove() {
        return false;
    }

    @Override
    public boolean start(String[] ids) {
        return false;
    }

    @Override
    public boolean start(String id) {
        return false;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean stop(String[] ids) {
        return false;
    }

    @Override
    public boolean stop(String id) {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public List<DownloadTask> get() {
        return null;
    }
}
