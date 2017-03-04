package com.lixiaocong.downloader;

import java.util.List;

public interface IDownloader {

    boolean addByMetainfo(String meatinfo);
    boolean addByMetalink(String meatlink);
    boolean addByUrl(String url);

    boolean remove(String[] ids);
    boolean remove(String id);
    boolean remove();

    boolean start(String[] ids);
    boolean start(String id);
    boolean start();

    boolean stop(String[] ids);
    boolean stop(String id);
    boolean stop();

    List<DownloadTask> get();
}
