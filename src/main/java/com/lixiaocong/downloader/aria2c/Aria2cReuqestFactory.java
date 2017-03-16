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


package com.lixiaocong.downloader.aria2c;

import java.util.List;

class Aria2cReuqestFactory {

    static Aria2cRequest getAddUriRequest(String token, String uri) {
        Aria2cRequest request = new Aria2cRequest(token, Aria2cRequestMethod.ADD_URI);
        List<Object> params = request.getParams();
        params.add(new String[]{uri});
        return request;
    }

    static Aria2cRequest getAddTorrentRequest(String token, String torrentBase64Content){
        Aria2cRequest request = new Aria2cRequest(token,Aria2cRequestMethod.ADD_TORRENT);
        List<Object> params = request.getParams();
        params.add(torrentBase64Content);
        return request;
    }

    static Aria2cRequest getTellActiveReuqest(String token) {
        return new Aria2cRequest(token, Aria2cRequestMethod.TELL_ACTIVE);
    }

    static Aria2cRequest getTellWaitingRequest(String token) {
        Aria2cRequest request = new Aria2cRequest(token, Aria2cRequestMethod.TELL_WAITING);
        List<Object> params = request.getParams();
        params.add(0);
        params.add(1000);
        return request;
    }

    static Aria2cRequest getTellStoppedRequest(String token) {
        Aria2cRequest request = new Aria2cRequest(token, Aria2cRequestMethod.TELL_STOPPED);
        List<Object> params = request.getParams();
        params.add(0);
        params.add(1000);
        return request;
    }

    static Aria2cRequest getPauseReuqest(String token, String gid) {
        Aria2cRequest request = new Aria2cRequest(token, Aria2cRequestMethod.PAUSE);
        request.getParams().add(gid);
        return request;
    }

    static Aria2cRequest getPauseAllReuqest(String token) {
        return new Aria2cRequest(token, Aria2cRequestMethod.PAUSE_ALL);
    }

    static Aria2cRequest getUnpauseReuqest(String token, String gid) {
        Aria2cRequest request = new Aria2cRequest(token, Aria2cRequestMethod.UNPAUSE);
        request.getParams().add(gid);
        return request;
    }

    static Aria2cRequest getUnpauseAllReuqest(String token) {
        return new Aria2cRequest(token, Aria2cRequestMethod.UNPAUSE_ALL);
    }

    static Aria2cRequest getRemoveReuqest(String token, String gid) {
        Aria2cRequest request = new Aria2cRequest(token, Aria2cRequestMethod.REMOVE);
        request.getParams().add(gid);
        return request;
    }

    static Aria2cRequest getPargeRquest(String token) {
        return new Aria2cRequest(token, Aria2cRequestMethod.PURGE_DOWNLOAD_RESULT);
    }
}
