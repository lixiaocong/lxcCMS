package com.lixiaocong;

import com.lixiaocong.downloader.Aria2c.Aria2cRequest;
import com.lixiaocong.downloader.Aria2c.Aria2cReuqestFactory;
import com.lixiaocong.transmission4j.exception.JsonException;
import com.lixiaocong.transmission4j.utils.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import java.io.*;
import java.util.Base64;

public class Aria2cTest {
    private Log log = LogFactory.getLog(getClass().getName());
    @Test
    public void addTorrentTest() throws IOException, JsonException {
        File file = new File("src/test/resources/test.torrent");
        InputStream in = new FileInputStream(file);
        int len = in.available();
        byte[] data = new byte[len];
        in.read(data, 0, len);
        in.close();
        String s = Base64.getEncoder().encodeToString(data);
        Aria2cRequest request = Aria2cReuqestFactory.getAddTorrentRequest("123456", s);
        String json = JsonUtil.getJson(request);
        log.info(json);
    }

    @Test
    public void addUriTest() throws JsonException {
        Aria2cRequest addUriRequest = Aria2cReuqestFactory.getAddUriRequest("123456", "www.baidu.com");
        String json = JsonUtil.getJson(addUriRequest);
        log.info(json);
    }
}
