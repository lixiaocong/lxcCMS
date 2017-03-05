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

package com.lixiaocong.rest;

import com.lixiaocong.downloader.DownloadTask;
import com.lixiaocong.downloader.DownloaderException;
import com.lixiaocong.downloader.IDownloader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
@RolesAllowed("ROLE_ADMIN")
@RequestMapping("/downloader")
public class DownloaderController {
    private Log log = LogFactory.getLog(getClass().getName());
    private IDownloader downloader;

    @Autowired
    public DownloaderController(IDownloader downloader) {
        this.downloader = downloader;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> post(MultipartFile file) {
        try {
            String metainfo = Base64.getEncoder().encodeToString(file.getBytes());
            if (downloader.addByMetainfo(metainfo))
                return ResponseMsgFactory.createSuccessResponse();
        } catch (Exception e) {
            log.error(e);
        }
        return ResponseMsgFactory.createFailedResponse("下载服务器异常");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Map<String, Object> delete(String id) throws DownloaderException {
        if(downloader.remove(id))
        return ResponseMsgFactory.createSuccessResponse();
        return ResponseMsgFactory.createFailedResponse("error");
    }

    @RequestMapping(value = "/start", method = RequestMethod.PUT)
    public Map<String, Object> start(String id) throws DownloaderException {
        downloader.start(id);
        return ResponseMsgFactory.createSuccessResponse();
    }

    @RequestMapping(value = "/stop", method = RequestMethod.PUT)
    public Map<String, Object> stop(String id) throws DownloaderException {
        downloader.stop(id);
        return ResponseMsgFactory.createSuccessResponse();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> get() throws DownloaderException {
        List<DownloadTask> tasks = downloader.get();
        return ResponseMsgFactory.createSuccessResponse("tasks", tasks);
    }
}
