package com.lixiaocong;

import com.lixiaocong.downloader.Aria2c.Aria2cDownloader;
import com.lixiaocong.downloader.Aria2c.Aria2cRequest;
import com.lixiaocong.downloader.Aria2c.Aria2cReuqestFactory;
import com.lixiaocong.downloader.DownloaderException;
import com.lixiaocong.transmission4j.exception.JsonException;
import com.lixiaocong.transmission4j.utils.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.*;
import java.util.Base64;

public class Aria2cTest {
    private Log log = LogFactory.getLog(getClass().getName());
    private Aria2cDownloader downloader = new Aria2cDownloader("123456");

    @Test
    public void addTorrentTest() throws IOException, JsonException, DownloaderException {
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
    public void addUriTest() throws JsonException, DownloaderException {
        downloader.addByUrl("http://www.baidu.com");
    }
}
