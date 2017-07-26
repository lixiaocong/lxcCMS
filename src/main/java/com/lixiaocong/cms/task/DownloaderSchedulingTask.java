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
import com.lixiaocong.downloader.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DownloaderSchedulingTask {
    private Log log = LogFactory.getLog(getClass());
    private Set<String> typeSet;
    private IDownloader downloader;
    private DownloaderSocketHandler downloaderSocketHandler;
    private String fileDestination;

    @Autowired
    public DownloaderSchedulingTask(IDownloader downloader, @Value("${file.server.root}") String fileDestination, @Value("${file.types}") String fileTypes, DownloaderSocketHandler downloaderSocketHandler) {
        this.downloader = downloader;
        this.downloaderSocketHandler = downloaderSocketHandler;
        this.fileDestination = fileDestination;
        if(!this.fileDestination.endsWith("/"))
            this.fileDestination += "/";

        this.typeSet = new HashSet<>();
        String[] types = fileTypes.split("\\|");
        this.typeSet.addAll(Arrays.asList(types));
    }

    @Scheduled(fixedDelay = 1000)
    public void task() {
        List<DownloadTask> torrents = null;
        try {
            torrents = downloader.get();
            handleCompleteFiles(torrents);
        } catch (DownloaderException e) {
            e.printStackTrace();
        }
        this.downloaderSocketHandler.broadcast(torrents);
    }

    public void handleCompleteFiles(List<DownloadTask> tasks) {
        tasks.forEach(task -> {
            if (task.getStatus() == DownloadStatus.SEEDING || task.getStatus() == DownloadStatus.COMPLETED) {
                List<DownloadFile> files = task.getFiles();
                files.stream()
                        .filter(this::typeFilter)
                        .forEach(this::moveFile);
                try {
                    downloader.remove(task.getId());
                } catch (DownloaderException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean typeFilter(DownloadFile downloadFile) {
        int index = downloadFile.getName().lastIndexOf(".");
        String type = downloadFile.getName().substring(index+1);
        return this.typeSet.contains(type);
    }

    private void moveFile(DownloadFile downloadFile) {
        File file = new File(downloadFile.getPath());
        boolean result = file.renameTo(new File(fileDestination + file.getName()));
        if (!result)
            log.error("move file " + file.getName() + " error");
    }
}
