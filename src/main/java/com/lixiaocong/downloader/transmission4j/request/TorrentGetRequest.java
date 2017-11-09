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

package com.lixiaocong.downloader.transmission4j.request;

import java.util.LinkedList;
import java.util.List;

public class TorrentGetRequest extends TransmissionRequest {

    public TorrentGetRequest(List<Integer> ids) {
        super(TransmissionRequestMethod.TORRENT_GET);
        if (ids != null)
            arguments.put("ids", ids);

        LinkedList<String> fields = new LinkedList<>();
        fields.add("id");
        fields.add("name");
        fields.add("isFinished");
        fields.add("downloadDir");
        fields.add("activityDate");
        fields.add("addedDate");
        fields.add("doneDate");
        fields.add("dateCreated");
        fields.add("bandwidthPriority");
        fields.add("creator");
        fields.add("desiredAvailable");
        fields.add("files");
        fields.add("isStalled");
        fields.add("magnetLink");
        fields.add("rateDownload");
        fields.add("rateUpload");
        fields.add("secondsDownloading");
        fields.add("secondsSeeding");
        fields.add("totalSize");
        fields.add("uploadRatio");
        fields.add("downloadedEver");
        fields.add("percentDone");
        fields.add("status");
        arguments.put("fields", fields);
    }
}
