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

import com.lixiaocong.downloader.DownloadTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public class Aria2cDownloaderTest {
    private Log log = LogFactory.getLog(getClass().getName());
    private Aria2cDownloader downloader = new Aria2cDownloader("123456");
    private final String gid = "2980925a101e212a";

    @Test
    public void addByMetainfo() throws Exception {
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
    public void addByMetalink() throws Exception {

    }

    @Test
    public void addByUrl() throws Exception {
        downloader.addByUrl("http://www.baidu.com");
    }

    @Test
    public void remove() throws Exception {
        assert (downloader.remove(gid));
    }

    @Test
    public void remove1() throws Exception {
        String[] ids = {gid};
        assert (downloader.remove(ids));
    }

    @Test
    public void remove2() throws Exception {
        assert (downloader.remove());
    }

    @Test
    public void start() throws Exception {
        assert (downloader.start(gid));
    }

    @Test
    public void start1() throws Exception {
        String[] ids = {gid};
        assert (downloader.start(ids));
    }

    @Test
    public void start2() throws Exception {
        assert (downloader.start());
    }

    @Test
    public void stop() throws Exception {
        assert (downloader.stop(gid));
    }

    @Test
    public void stop1() throws Exception {
        String[] ids = {gid};
        assert (downloader.stop(ids));
    }

    @Test
    public void stop2() throws Exception {
        assert (downloader.stop());
    }

    @Test
    public void get() throws Exception {
        List<DownloadTask> downloadTasks = downloader.get();
        downloadTasks.forEach(System.out::println);
    }

    @Test
    public void test() throws Exception {
        start();
        Thread.sleep(1000);
        stop();
        Thread.sleep(1000);
        start1();
        Thread.sleep(1000);
        stop1();
        Thread.sleep(1000);
        start2();
        Thread.sleep(1000);
        stop2();
    }
}