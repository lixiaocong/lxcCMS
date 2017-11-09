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

package com.lixiaocong;

import com.lixiaocong.cms.downloader.UnionDownloader;
import com.lixiaocong.downloader.DownloadTask;
import com.lixiaocong.downloader.DownloaderException;
import com.lixiaocong.downloader.IDownloader;
import com.lixiaocong.downloader.aria2c4j.AriaClient;
import com.lixiaocong.downloader.transmission4j.TransmissionClient;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public class DownloaderTest {

    private String aria2cUrl = "http://localhost:6800/jsonrpc";
    private String aria2cPassword = "password";
    private String fileDir = "/cms/storage";

    private String transmissionUri = "http://localhost:9091/transmission/rpc";
    private String transmissionUsername = "username";
    private String transmissionPassword = "password";

    private IDownloader downloader;

    @Before
    public void before() {
        AriaClient ariaClient = new AriaClient(this.aria2cUrl, this.aria2cPassword, this.fileDir);
        TransmissionClient transmissionClient = new TransmissionClient(this.transmissionUsername, this.transmissionPassword, this.transmissionUri);
        this.downloader = new UnionDownloader(transmissionClient, ariaClient);
    }

    @Test
    public void addHttp() throws DownloaderException {
        downloader.addByUrl("http://www.baidu.com");
    }

    @Test
    public void addTorrent() throws IOException, DownloaderException {
        File file = new File("src/test/resources/test.torrent");
        InputStream in = new FileInputStream(file);
        int len = in.available();
        byte[] data = new byte[len];
        in.read(data, 0, len);
        in.close();
        String s = Base64.getEncoder().encodeToString(data);
        downloader.addByMetainfo(s);
    }

    @Test
    public void get() throws DownloaderException {
        List<DownloadTask> downloadTasks = downloader.get();
        downloadTasks.forEach(task->{
            System.out.println(task.getName());
        });
    }

    @Test
    public void getSingle() throws DownloaderException {
        List<DownloadTask> downloadTasks = downloader.get();
        for (DownloadTask task : downloadTasks) {
            DownloadTask singleTask = downloader.get(task.getId());
            System.out.println(singleTask.getName());
        }
    }

    @Test
    public void remove() throws DownloaderException {
        downloader.remove();
    }
}
