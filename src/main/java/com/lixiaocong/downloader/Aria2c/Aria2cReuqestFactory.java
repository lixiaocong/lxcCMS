package com.lixiaocong.downloader.Aria2c;

public class Aria2cReuqestFactory {

    public static Aria2cRequest getAddUriRequest(String token, String uri) {
        Aria2cRequest request = new Aria2cRequest(token, Aria2cRequestMethod.ADD_URI);
        Object[] params = request.getParams();
        params[1] = new String[]{uri};
        return request;
    }

    public static Aria2cRequest getAddTorrentRequest(String token, String torrentBase64Content){
        Aria2cRequest request = new Aria2cRequest(token,Aria2cRequestMethod.ADD_TORRENT);
        Object[] params = request.getParams();
        params[1] = torrentBase64Content;
        return request;
    }
}
