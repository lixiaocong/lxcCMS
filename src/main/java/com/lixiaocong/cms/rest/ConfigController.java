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

package com.lixiaocong.cms.rest;

import com.lixiaocong.cms.model.ConfigForm;
import com.lixiaocong.cms.service.IConfigService;
import com.lixiaocong.cms.service.impl.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RolesAllowed("ROLE_ADMIN")
@RequestMapping("/config")
public class ConfigController {
    private final IConfigService configService;

    @Autowired
    public ConfigController(IConfigService configService) {
        this.configService = configService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> get() {
        Map<String, Object> ret = new TreeMap<>();
        ret.put(ConfigService.BLOG_ENABLED, configService.isBlogEnabled());

        ret.put(ConfigService.APPLICATION_URL, configService.getApplicationUrl());
        ret.put(ConfigService.QQ_ENABLED, configService.isQQEnabled());
        ret.put(ConfigService.QQ_ID, configService.getQQId());
        ret.put(ConfigService.QQ_SECRET, configService.getQQSecret());

        ret.put(ConfigService.WEIXIN_ENABLED, configService.isWeixinEnabled());
        ret.put(ConfigService.WEIXIN_ID, configService.getWeixinId());
        ret.put(ConfigService.WEIXIN_TOKEN, configService.getWeixinToken());
        ret.put(ConfigService.WEIXIN_SECRET, configService.getWeixinSecret());
        ret.put(ConfigService.WEIXIN_KEY, configService.getWeixinKey());

        ret.put(ConfigService.DOWNLOADER_ENABLED, configService.isDownloaderEnabled());
        ret.put(ConfigService.DOWNLOADER_ARIA2C_URL, configService.getDownloaderAria2cUrl());
        ret.put(ConfigService.DOWNLOADER_ARIA2C_PASSWORD, configService.getDownloaderAria2cPassword());
        ret.put(ConfigService.DOWNLOADER_TRANSMISSION_URL, configService.getDownloaderTransmissionUrl());
        ret.put(ConfigService.DOWNLOADER_TRANSMISSION_USERNAME, configService.getDownloaderTransmissionUsername());
        ret.put(ConfigService.DOWNLOADER_TRANSMISSION_PASSWORD, configService.getDownloaderTransmissionPassword());

        return ResponseMsgFactory.createSuccessResponse("configs", ret);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void put(@RequestBody ConfigForm[] request) {
        for (ConfigForm configForm : request) {
            this.configService.setValue(configForm.getKey(), configForm.getValue());
        }
    }
}
