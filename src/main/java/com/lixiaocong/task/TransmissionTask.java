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

package com.lixiaocong.task;

import com.lixiaocong.transmission4j.TransmissionClient;
import com.lixiaocong.transmission4j.exception.AuthException;
import com.lixiaocong.transmission4j.exception.JsonException;
import com.lixiaocong.transmission4j.exception.NetworkException;
import com.lixiaocong.transmission4j.response.Torrent;
import com.lixiaocong.util.VideoFileHelper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Component
public class TransmissionTask {
    private Log logger = LogFactory.getLog(getClass());
    private TransmissionClient client;

    @Value("${nginx.root}")
    private String fileDestination;
    @Value("${videoType}")
    private String fileTypes;

    @Autowired
    public TransmissionTask(@Value("${transmission.username}") String username, @Value("${transmission.password}") String password) {
        this.client = new TransmissionClient(username, password, "http://127.0.0.1:9091/transmission/rpc");
    }

    @Scheduled(fixedDelay = 5000)
    public void task() throws JsonException, AuthException, NetworkException {
        List<Torrent> torrents = client.getAll();
        List<Integer> ids2remove = new LinkedList<>();
        for (Torrent torrent : torrents) {
            if (torrent.getDoneDate() > 0)//没有下载成功,结果是0
            {
                List<File> allVideos = VideoFileHelper.findAllVideos(new File(torrent.getDownloadDir()), fileTypes.split("\\|"));
                if (VideoFileHelper.moveFiles(allVideos, fileDestination)) {
                    logger.info(torrent.getName() + "下载完成,进行移动");
                    ids2remove.add((int) torrent.getId());
                } else logger.warn(torrent.getName() + "移动出现错误");
            }
        }
        if (ids2remove.size() > 0 && client.remove(ids2remove)) logger.info("完成任务已删除");
    }
}
