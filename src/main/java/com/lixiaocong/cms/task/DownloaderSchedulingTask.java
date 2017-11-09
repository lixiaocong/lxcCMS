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

import com.lixiaocong.cms.socket.DownloaderSocketHandler;
import com.lixiaocong.downloader.DownloadTask;
import com.lixiaocong.downloader.DownloaderException;
import com.lixiaocong.downloader.IDownloader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DownloaderSchedulingTask {

    private static final Log log = LogFactory.getLog(DownloaderSchedulingTask.class);

    private IDownloader downloader;
    private DownloaderSocketHandler downloaderSocketHandler;

    @Autowired
    public DownloaderSchedulingTask(IDownloader downloader, DownloaderSocketHandler downloaderSocketHandler, @Value("${application.url}") String applicationUrl) {
        this.downloader = downloader;
        this.downloaderSocketHandler = downloaderSocketHandler;
        log.info("application is running on " + applicationUrl);
    }

    @Scheduled(fixedRate = 1000)
    public void broadcastTask() {
        List<DownloadTask> torrents = null;
        try {
            torrents = downloader.get();
        } catch (DownloaderException e) {
            log.error(e);
        }
        this.downloaderSocketHandler.broadcast(torrents);
    }
}
