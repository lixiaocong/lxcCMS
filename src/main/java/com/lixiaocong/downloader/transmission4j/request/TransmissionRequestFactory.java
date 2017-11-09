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

import java.util.List;

public class TransmissionRequestFactory {

    public static TorrentStartRequest getStartRequest(List<Integer> ids) {
        return new TorrentStartRequest(ids);
    }

    public static TorrentStartRequest getStartAllReuqest() {
        return new TorrentStartRequest(null);
    }

    public static TorrentStopRequest getStopRequest(List<Integer> ids) {
        return new TorrentStopRequest(ids);
    }

    public static TorrentStopRequest getStopAllRequest() {
        return new TorrentStopRequest(null);
    }

    public static TransmissionRequest getAddRequest(String metainfo) {
        return new TorrentAddRequest(metainfo);
    }

    public static TorrentRemoveRequest getRemoveAllRequest() {
        return new TorrentRemoveRequest(null, false);
    }

    public static TorrentRemoveRequest getRemoveRequest(List<Integer> ids) {
        return new TorrentRemoveRequest(ids, false);
    }

    public static TransmissionRequest getGetRequest(List<Integer> ids) {
        return new TorrentGetRequest(ids);
    }

    public static TransmissionRequest getgetAllRequest() {
        return new TorrentGetRequest(null);
    }
}
