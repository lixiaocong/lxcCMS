/**
 * Copyright (c) 2016, lixiaocong <lxccs@iCloud.com>
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p>
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p>
 * Neither the name of transmission4j nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.lixiaocong.downloader.transmission4j;

import com.lixiaocong.downloader.DownloadTask;
import com.lixiaocong.downloader.DownloaderException;
import com.lixiaocong.downloader.IDownloader;
import com.lixiaocong.downloader.transmission4j.exception.JsonException;
import com.lixiaocong.downloader.transmission4j.request.TorrentStartRequest;
import com.lixiaocong.downloader.transmission4j.request.TorrentStopRequest;
import com.lixiaocong.downloader.transmission4j.request.TransmissionRequest;
import com.lixiaocong.downloader.transmission4j.request.TransmissionRequestFactory;
import com.lixiaocong.downloader.transmission4j.response.TorrentGetResponse;
import com.lixiaocong.downloader.transmission4j.response.TransmissionResponse;
import com.lixiaocong.downloader.transmission4j.utils.JsonUtil;
import com.lixiaocong.downloader.transmission4j.utils.TaskConvert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

/**
 * Transmission java client
 */
public class TransmissionClient implements IDownloader {
    private static Log log = LogFactory.getLog(TransmissionClient.class.getName());

    private String username;
    private String password;
    private String id;  //id is used in transmission rpc, details in https://trac.transmissionbt.com/browser/trunk/extras/rpc-spec.txt

    private HttpClient httpClient;
    private HttpPost httpPost;

    public TransmissionClient(String username, String password, String uri) {
        log.info("new TransmissionClient username:" + username + " password:" + password + " uri:" + uri);
        this.username = username;
        this.password = password;
        this.id = null;

        buildHttpClient();

        httpPost = new HttpPost(uri);
        RequestConfig config = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).setConnectionRequestTimeout(2000).build();
        httpPost.setConfig(config);
    }

    private void buildHttpClient() {
        log.info("build client with X-Transmission-Session-Id:" + id);
        Header authHeader = new BasicHeader(HttpHeaders.AUTHORIZATION, String.format("Basic %s", Base64.getEncoder().encodeToString((username + ":" + password).getBytes(StandardCharsets.UTF_8))));
        Header idHeader = new BasicHeader("X-Transmission-Session-Id", id);
        List<Header> headers = new ArrayList<>();
        headers.add(authHeader);
        headers.add(idHeader);
        httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
    }

    private <T extends TransmissionResponse> T execute(TransmissionRequest request, Class<T> responseClass) throws DownloaderException {
        String requestStr;
        try {
            requestStr = JsonUtil.getJson(request);
        } catch (JsonException e) {
            log.error(e);
            throw new RuntimeException(e.getMessage());
        }

        //log.info("execute request " + requestStr);
        httpPost.setEntity(new StringEntity(requestStr, ContentType.APPLICATION_JSON));
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
        } catch (IOException e) {
            log.error(e);
            throw new DownloaderException(e.getMessage());
        }

        int code = response.getStatusLine().getStatusCode();
        if (code == HttpStatus.SC_OK) {
            String responseStr;
            try {
                responseStr = EntityUtils.toString(response.getEntity());
            } catch (IOException e) {
                log.warn("read content of " + requestStr + ". exception:", e);
                throw new DownloaderException(e.getMessage());
            }
//            log.info("execute response " + responseStr);
            try {
                return JsonUtil.getObject(responseClass, responseStr);
            } catch (JsonException e) {
                log.error(e);
                throw new RuntimeException(e.getMessage());
            }
        } else if (code == HttpStatus.SC_CONFLICT) {
            log.info("execute response 409");
            Header[] headers = response.getHeaders("X-Transmission-Session-Id");
            if (headers.length == 0)
                throw new RuntimeException("transmission return 409 without id");
            id = headers[0].getValue();
            buildHttpClient();
            return execute(request, responseClass);
        } else if (code == HttpStatus.SC_UNAUTHORIZED) {
            log.info("execute response 401");
            throw new DownloaderException("username: " + username + " or password " + password + " incorrect");
        }
        log.error("execute error with response code " + code);
        throw new DownloaderException("execute error with response code " + code);
    }

    @Override
    public void addByMetainfo(String metainfo) throws DownloaderException {
        TransmissionRequest request = TransmissionRequestFactory.getAddRequest(metainfo);
        TransmissionResponse response = execute(request, TransmissionResponse.class);
        response.getResult().equals("success");
    }

    @Override
    public void addByMetalink(String metalink) throws DownloaderException {
        throw new DownloaderException("not supported");
    }

    @Override
    public void addByUrl(String url) throws DownloaderException {
        throw new DownloaderException("not supported");
    }

    @Override
    public void remove(List<String> ids) throws DownloaderException {
        List<Integer> list = new LinkedList<>();
        for (String s : ids) {
            list.add(Integer.valueOf(s));
        }
        TransmissionRequest request = TransmissionRequestFactory.getRemoveRequest(list);
        TransmissionResponse response = execute(request, TransmissionResponse.class);
        response.getResult().equals("success");
    }

    @Override
    public void remove(String id) throws DownloaderException {
        throw new DownloaderException("not supported");
    }

    @Override
    public void remove() throws DownloaderException {
        TransmissionRequest request = TransmissionRequestFactory.getRemoveAllRequest();
        TransmissionResponse response = execute(request, TransmissionResponse.class);
        response.getResult().equals("success");
    }

    @Override
    public void start(List<String> ids) throws DownloaderException {
        List<Integer> list = new LinkedList<>();
        for (String s : ids) {
            list.add(Integer.valueOf(s));
        }
        TransmissionRequest request = TransmissionRequestFactory.getStartRequest(list);
        TransmissionResponse response = execute(request, TransmissionResponse.class);
        response.getResult().equals("success");
    }

    @Override
    public void start(String id) throws DownloaderException {

    }

    @Override
    public void start() throws DownloaderException {
        TorrentStartRequest startAllReuqest = TransmissionRequestFactory.getStartAllReuqest();
        TransmissionResponse response = execute(startAllReuqest, TransmissionResponse.class);
        response.getResult().equals("success");
    }

    @Override
    public void stop(List<String> ids) throws DownloaderException {
        List<Integer> list = new LinkedList<>();
        for (String s : ids) {
            list.add(Integer.valueOf(s));
        }
        TransmissionRequest request = TransmissionRequestFactory.getStopRequest(list);
        TransmissionResponse response = execute(request, TransmissionResponse.class);
        response.getResult().equals("success");
    }

    @Override
    public void stop(String id) throws DownloaderException {
        TorrentStopRequest stopAllRequest = TransmissionRequestFactory.getStopAllRequest();
        TransmissionResponse response = execute(stopAllRequest, TransmissionResponse.class);
        response.getResult().equals("success");
    }

    @Override
    public void stop() throws DownloaderException {

    }

    @Override
    public DownloadTask get(String gid) throws DownloaderException {
        List<Integer> list = new LinkedList<>();
        list.add(Integer.valueOf(gid));
        TransmissionRequest request = TransmissionRequestFactory.getGetRequest(list);
        TorrentGetResponse response = execute(request, TorrentGetResponse.class);
        return TaskConvert.convertToDownloadTask(response.getArguments().getTorrents()).get(0);
    }

    @Override
    public List<DownloadTask> get() throws DownloaderException {
        TransmissionRequest request = TransmissionRequestFactory.getgetAllRequest();
        TorrentGetResponse response = execute(request, TorrentGetResponse.class);
        return TaskConvert.convertToDownloadTask(response.getArguments().getTorrents());
    }
}