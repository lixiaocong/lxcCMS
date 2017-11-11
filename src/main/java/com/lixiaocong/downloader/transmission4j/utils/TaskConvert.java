/**
 * Copyright (c) 2016, lixiaocong <lxccs@iCloud.com>
 * All rights reserved.
 * <p>
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * <p>
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * <p>
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * <p>
 * Neither the name of transmission4j nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.lixiaocong.downloader.transmission4j.utils;

import com.lixiaocong.downloader.DownloadFile;
import com.lixiaocong.downloader.DownloadStatus;
import com.lixiaocong.downloader.DownloadTask;
import com.lixiaocong.downloader.DownloadType;
import com.lixiaocong.downloader.transmission4j.response.Torrent;
import com.lixiaocong.downloader.transmission4j.response.TorrentFile;

import java.util.LinkedList;
import java.util.List;

public class TaskConvert {
    public static List<DownloadTask> convertToDownloadTask(List<Torrent> torrents) {
        List<DownloadTask> ret = new LinkedList<>();
        torrents.forEach(torrent -> {
            ret.add(convertToDownloadTask(torrent));
        });
        return ret;
    }

    private static DownloadTask convertToDownloadTask(Torrent torrent) {
        String id = String.valueOf(torrent.getId());
        DownloadStatus status;
        switch (torrent.getStatus()) {
            case 0:
                status = DownloadStatus.PAUSED;
                break;
            case 1:
            case 2:
            case 3:
                status = DownloadStatus.WAITING;
                break;
            case 4:
                status = DownloadStatus.ACTIVE;
                break;
            case 5:
            case 6:
                status = DownloadStatus.COMPLETED;
                break;
            case 7:
                status = DownloadStatus.ERROR;
                break;
            default:
                status = DownloadStatus.OTHER;
        }

        DownloadType type = DownloadType.TORRENT;
        String name = torrent.getName();
        long totalLength = torrent.getTotalSize();
        long downloadLength = torrent.getDownloadedEver();
        long downloadSpeed = torrent.getRateDownload();
        long uploadLength = (long) (torrent.getUploadRatio() * torrent.getTotalSize());
        long uploadSpeed = torrent.getRateUpload();
        String dir = torrent.getDownloadDir();
        List<DownloadFile> files = covertToDownloadFile(torrent.getFiles());
        return new DownloadTask(id, status, type, name, totalLength, downloadLength, downloadSpeed, uploadLength, uploadSpeed, dir, files);
    }

    private static List<DownloadFile> covertToDownloadFile(List<TorrentFile> files) {
        List<DownloadFile> ret = new LinkedList<>();
        files.forEach(file -> ret.add(convertToDownloadTask(file)));
        return ret;
    }

    private static DownloadFile convertToDownloadTask(TorrentFile file) {
        String name = file.getName();
        String path = file.getName();
        long totalLength = file.getLength();
        long downloadLength = file.getBytesCompleted();
        DownloadFile downloadFile = new DownloadFile(name, path, totalLength, downloadLength);
        return downloadFile;
    }
}
