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

package com.lixiaocong.cms.task;

import com.lixiaocong.downloader.DownloadTask;
import com.lixiaocong.downloader.DownloaderException;
import com.lixiaocong.downloader.IDownloader;
import com.lixiaocong.transmission4j.exception.AuthException;
import com.lixiaocong.transmission4j.exception.JsonException;
import com.lixiaocong.transmission4j.exception.NetworkException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DownloaderTask {
    private Log log = LogFactory.getLog(getClass());

    private IDownloader downloader;

    @Value("${nginx.root}")
    private String fileDestination;
    @Value("${videoType}")
    private String fileTypes;

    @Autowired
    public DownloaderTask(IDownloader downloader) {
        this.downloader = downloader;
    }

    @Scheduled(fixedDelay = 5000)
    public void task() throws JsonException, AuthException, NetworkException {
        List<DownloadTask> torrents;
        try {
            torrents = downloader.get();
            torrents.forEach(torrent ->{
                if(torrent.isFinished()) {
                    log.info(torrent.getName()+" 完成");
                    try {
                        //TODO 移动文件到Nginx目录下
                        downloader.remove(torrent.getId());
                    } catch (DownloaderException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (DownloaderException e) {
            e.printStackTrace();
        }
    }
}
