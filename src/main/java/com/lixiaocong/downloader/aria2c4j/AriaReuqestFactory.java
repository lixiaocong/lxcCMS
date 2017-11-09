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

package com.lixiaocong.downloader.aria2c4j;

import java.io.File;
import java.util.List;
import java.util.UUID;

//TODO change the way to get request json
class AriaReuqestFactory {

    static AriaRequest getAddUriRequest(String token, String uri) {
        AriaRequest request = new AriaRequest(token, AriaRequestMethod.ADD_URI);
        List<Object> params = request.getParams();
        params.add(new String[]{uri});
        return request;
    }

    static AriaRequest getAddTorrentRequest(String token, String torrentBase64Content, String baseDir) {
        AriaRequest request = new AriaRequest(token, AriaRequestMethod.ADD_TORRENT);
        List<Object> params = request.getParams();
        params.add(torrentBase64Content);
        params.add(new String[]{""});
        String dir = baseDir + UUID.randomUUID().toString();//random folder
        new File(dir).mkdir();
        params.add(new DirConfig(dir));
        return request;
    }

    static AriaRequest getTellActiveReuqest(String token) {
        return new AriaRequest(token, AriaRequestMethod.TELL_ACTIVE);
    }

    static AriaRequest getTellWaitingRequest(String token) {
        AriaRequest request = new AriaRequest(token, AriaRequestMethod.TELL_WAITING);
        List<Object> params = request.getParams();
        params.add(0);
        params.add(1000);
        return request;
    }

    static AriaRequest getTellStoppedRequest(String token) {
        AriaRequest request = new AriaRequest(token, AriaRequestMethod.TELL_STOPPED);
        List<Object> params = request.getParams();
        params.add(0);
        params.add(1000);
        return request;
    }

    static AriaRequest getPauseReuqest(String token, String gid) {
        AriaRequest request = new AriaRequest(token, AriaRequestMethod.PAUSE);
        request.getParams().add(gid);
        return request;
    }

    static AriaRequest getPauseAllReuqest(String token) {
        return new AriaRequest(token, AriaRequestMethod.PAUSE_ALL);
    }

    static AriaRequest getUnpauseReuqest(String token, String gid) {
        AriaRequest request = new AriaRequest(token, AriaRequestMethod.UNPAUSE);
        request.getParams().add(gid);
        return request;
    }

    static AriaRequest getUnpauseAllReuqest(String token) {
        return new AriaRequest(token, AriaRequestMethod.UNPAUSE_ALL);
    }

    static AriaRequest getRemoveReuqest(String token, String gid) {
        AriaRequest request = new AriaRequest(token, AriaRequestMethod.REMOVE);
        request.getParams().add(gid);
        return request;
    }

    static AriaRequest getRemoveResultReuqest(String token, String gid) {
        AriaRequest request = new AriaRequest(token, AriaRequestMethod.REMOVE_DOWNLOAD_RESULT);
        request.getParams().add(gid);
        return request;
    }

    static AriaRequest getTellStatusRequest(String token, String gid) {
        AriaRequest request = new AriaRequest(token, AriaRequestMethod.TELL_STATUS);
        request.getParams().add(gid);
        return request;
    }

    private static class DirConfig {
        private String dir;

        DirConfig(String dir) {
            this.dir = dir;
        }
    }
}
