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

package com.lixiaocong.downloader.Aria2c;

import com.lixiaocong.downloader.DownloadTask;
import com.lixiaocong.downloader.IDownloader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

public class Aria2cDownloader implements IDownloader {
    private Log log = LogFactory.getLog(getClass().getName());
    private String uri = "http://127.0.0.1:6800/jsonrpc";
    private String token;

    public Aria2cDownloader(String token) {
        this.token = token;
    }

    @Override
    public boolean addByMetainfo(String meatinfo) {
        return false;
    }

    @Override
    public boolean addByMetalink(String meatlink) {
        return false;
    }

    @Override
    public boolean addByUrl(String url) {
        return false;
    }

    @Override
    public boolean remove(String[] ids) {
        return false;
    }

    @Override
    public boolean remove(String id) {
        return false;
    }

    @Override
    public boolean remove() {
        return false;
    }

    @Override
    public boolean start(String[] ids) {
        return false;
    }

    @Override
    public boolean start(String id) {
        return false;
    }

    @Override
    public boolean start() {
        return false;
    }

    @Override
    public boolean stop(String[] ids) {
        return false;
    }

    @Override
    public boolean stop(String id) {
        return false;
    }

    @Override
    public boolean stop() {
        return false;
    }

    @Override
    public List<DownloadTask> get() {
        return null;
    }
}
