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

package com.lixiaocong.downloader.transmission4j.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Torrent {
    private long id;
    private String name;
    @JsonProperty("isFinished")
    private boolean finished;
    private String downloadDir;
    private long activityDate;
    private long addedDate;
    private long doneDate;
    private long dateCreated;
    private long bandwidthPriority;
    private String creator;
    private long desiredAvailable;
    private List<TorrentFile> files;
    @JsonProperty("isStalled")
    private boolean stalled;
    private String magnetLink;
    private long rateDownload;
    private long rateUpload;
    private long secondsDownloading;
    private long secondsSeeding;
    private long totalSize;
    private double uploadRatio;
    private long downloadedEver;
    private double percentDone;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getDownloadDir() {
        return downloadDir;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }

    public long getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(long activityDate) {
        this.activityDate = activityDate;
    }

    public long getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(long addedDate) {
        this.addedDate = addedDate;
    }

    public long getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(long doneDate) {
        this.doneDate = doneDate;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }

    public long getBandwidthPriority() {
        return bandwidthPriority;
    }

    public void setBandwidthPriority(long bandwidthPriority) {
        this.bandwidthPriority = bandwidthPriority;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public long getDesiredAvailable() {
        return desiredAvailable;
    }

    public void setDesiredAvailable(long desiredAvailable) {
        this.desiredAvailable = desiredAvailable;
    }

    public List<TorrentFile> getFiles() {
        return files;
    }

    public void setFiles(List<TorrentFile> files) {
        this.files = files;
    }

    public boolean isStalled() {
        return stalled;
    }

    public void setStalled(boolean stalled) {
        this.stalled = stalled;
    }

    public String getMagnetLink() {
        return magnetLink;
    }

    public void setMagnetLink(String magnetLink) {
        this.magnetLink = magnetLink;
    }

    public long getRateDownload() {
        return rateDownload;
    }

    public void setRateDownload(long rateDownload) {
        this.rateDownload = rateDownload;
    }

    public long getRateUpload() {
        return rateUpload;
    }

    public void setRateUpload(long rateUpload) {
        this.rateUpload = rateUpload;
    }

    public long getSecondsDownloading() {
        return secondsDownloading;
    }

    public void setSecondsDownloading(long secondsDownloading) {
        this.secondsDownloading = secondsDownloading;
    }

    public long getSecondsSeeding() {
        return secondsSeeding;
    }

    public void setSecondsSeeding(long secondsSeeding) {
        this.secondsSeeding = secondsSeeding;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public double getUploadRatio() {
        return uploadRatio;
    }

    public void setUploadRatio(double uploadRatio) {
        this.uploadRatio = uploadRatio;
    }

    public long getDownloadedEver() {
        return downloadedEver;
    }

    public void setDownloadedEver(long downloadedEver) {
        this.downloadedEver = downloadedEver;
    }

    public double getPercentDone() {
        return percentDone;
    }

    public void setPercentDone(double percentDone) {
        this.percentDone = percentDone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
