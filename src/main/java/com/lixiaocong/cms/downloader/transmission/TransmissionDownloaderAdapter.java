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

package com.lixiaocong.cms.downloader.transmission;

import com.lixiaocong.downloader.DownloadTask;
import com.lixiaocong.downloader.IDownloader;
import com.lixiaocong.transmission4j.TransmissionClient;
import com.lixiaocong.transmission4j.exception.AuthException;
import com.lixiaocong.transmission4j.exception.NetworkException;
import com.lixiaocong.transmission4j.response.Torrent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class TransmissionDownloaderAdapter implements IDownloader {
    private Log log = LogFactory.getLog(getClass().getName());
    private TransmissionClient client;

    public TransmissionDownloaderAdapter(TransmissionClient client) {
        this.client = client;
    }

    @Override
    public boolean addByMetainfo(String meatinfo) {
        try {
            client.add(meatinfo);
            return true;
        } catch (AuthException | NetworkException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addByMetalink(String meatlink) {
        log.warn("method not supported yet");
        return false;
    }

    @Override
    public boolean addByUrl(String url) {
        log.warn("method not supported yet");
        return false;
    }

    @Override
    public boolean remove(String[] ids) {
        List<Integer> list = new LinkedList<>();
        Arrays.stream(ids).forEach(id -> list.add(Integer.parseInt(id)));
        try {
            client.remove(list);
            return true;
        } catch (AuthException | NetworkException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove(String id) {
        List<Integer> list = new LinkedList<>();
        list.add(Integer.parseInt(id));
        try {
            client.remove(list);
            return true;
        } catch (AuthException | NetworkException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove() {
        try {
            return client.removeAll();
        } catch (AuthException | NetworkException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean start(String[] ids) {
        List<Integer> list = new LinkedList<>();
        Arrays.stream(ids).forEach(id -> list.add(Integer.parseInt(id)));
        try {
            return client.start(list);
        } catch (AuthException | NetworkException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean start(String id) {
        List<Integer> list = new LinkedList<>();
        list.add(Integer.parseInt(id));
        try {
            return client.start(list);
        } catch (AuthException | NetworkException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean start() {
        try {
            return client.startAll();
        } catch (AuthException | NetworkException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean stop(String[] ids) {
        List<Integer> list = new LinkedList<>();
        Arrays.stream(ids).forEach(id -> list.add(Integer.parseInt(id)));
        try {
            return client.stop(list);
        } catch (AuthException | NetworkException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean stop(String id) {
        List<Integer> list = new LinkedList<>();
        list.add(Integer.parseInt(id));
        try {
            return client.stop(list);
        } catch (AuthException | NetworkException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean stop() {
        try {
            return client.stopAll();
        } catch (AuthException | NetworkException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<DownloadTask> get() {
        LinkedList<DownloadTask> taskList = new LinkedList<>();
        List<Torrent> torrentList;
        try {
            torrentList = client.getAll();
        } catch (AuthException | NetworkException e) {
            e.printStackTrace();
            return taskList;
        }
        torrentList.forEach(torrent -> {
            DownloadTask task = new DownloadTask();
            task.setDownloadedLength(torrent.getDownloadedEver());
            task.setDownloadType(DownloadTask.TYPE_TORRENT);
            task.setId(String.valueOf(torrent.getId()));
            task.setName(torrent.getName());
            task.setSpeed(torrent.getRateDownload());
            task.setTotalLength(torrent.getTotalSize());
            switch (torrent.getStatus()) {
                case 0:
                    task.setStatus("停止");
                    break;
                case 1:
                    task.setStatus("等待检查文件");
                    break;
                case 2:
                    task.setStatus("检查文件");
                    break;
                case 3:
                    task.setStatus("等待下载");
                    break;
                case 4:
                    task.setStatus("下载中");
                    break;
                case 5:
                    task.setStatus("等待做种");
                    break;
                case 6:
                    task.setStatus("做种中");
                    break;
                case 7:
                    task.setStatus("找不到可用节点");
                    break;
                default:
                    task.setStatus("未知状态");
                    break;
            }
            task.setFinished(torrent.getDoneDate() > 0);
            taskList.add(task);
        });
        return taskList;
    }
}
