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

package com.lixiaocong.cms.downloader;

import com.lixiaocong.downloader.DownloaderConfigurer;
import com.lixiaocong.downloader.EnableDownloader;
import com.lixiaocong.downloader.IDownloader;
import com.lixiaocong.cms.downloader.aria2c.Aria2cDownloader;
import com.lixiaocong.cms.downloader.transmission.TransmissionDownloaderAdapter;
import com.lixiaocong.transmission4j.TransmissionClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableDownloader
public class DownloaderConfig implements DownloaderConfigurer {

    @Value("${aria2c.token:#{null}}")
    private String token;
    @Value("${transmission.username:#{null}}")
    private String username;
    @Value("${transmission.password:#{null}}")
    private String password;

    private Log log= LogFactory.getLog(getClass().getName());

    @Override
    public IDownloader getDownloader(){
        if(token!=null) {

            log.info("aria2c client created");
            return new Aria2cDownloader(token);
        }
        TransmissionClient client = new TransmissionClient(username, password, "http://127.0.0.1:9091/transmission/rpc");
        return new TransmissionDownloaderAdapter(client);
    }
}
