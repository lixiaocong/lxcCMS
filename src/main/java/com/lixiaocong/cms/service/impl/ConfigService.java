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

package com.lixiaocong.cms.service.impl;

import com.lixiaocong.cms.entity.Config;
import com.lixiaocong.cms.repository.IConfigRepository;
import com.lixiaocong.cms.service.IConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfigService implements IConfigService {
    private static final String VALUE_TRUE = "1";
    private static final String VALUE_FALSE = "0";

    private static final String BLOG_ENABLE = "BLOG_ENABLE";

    private static final String APPLICATION_URL = "APPLICATION_URL";
    private static final String QQ_ENABLE = "QQ_ENABLE";
    private static final String QQ_ID = "QQ_ID";
    private static final String QQ_SECRET = "QQ_SECRET";

    private static final String WEIXIN_ENABLED = "WEIXIN_ENABLED";
    private static final String WEIXIN_ID = "WEIXIN_ID";
    private static final String WEIXIN_SECRET = "WEIXIN_SECRET";
    private static final String WEIXIN_TOKEN = "WEIXIN_TOKEN";
    private static final String WEIXIN_KEY = "WEIXIN_KEY";

    private static final String DOWNLOADER_ENABLED = "DOWNLOADER_ENABLED";
    private static final String DOWNLOADER_ARIA2C_URL = "DOWNLOADER_ARIA2C_URL";
    private static final String DOWNLOADER_ARIA2C_PASSWORD = "DOWNLOADER_ARIA2C_PASSWORD";
    private static final String DOWNLOADER_TRANSMISSION_URL = "DOWNLOADER_TRANSMISSION_URL";
    private static final String DOWNLOADER_TRANSMISSION_USERNAME = "DOWNLOADER_TRANSMISSION_USERNAME";
    private static final String DOWNLOADER_TRANSMISSION_PASSWORD = "DOWNLOADER_TRANSMISSION_PASSWORD";

    private final IConfigRepository configRepository;

    @Autowired
    public ConfigService(IConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    private String getValue(String key) {
        Config config = this.configRepository.findByKey(key);
        return config == null ? "" : config.getValue();
    }

    private void setValue(String key, String value) {
        Config config = this.configRepository.findByKey(key);
        if (config == null) {
            config = new Config();
            config.setKey(key);
        }
        config.setValue(value);
        this.configRepository.save(config);
    }

    private void setValue(String key, Boolean isEnable) {
        if (isEnable)
            setValue(key, VALUE_TRUE);
        else
            setValue(key, VALUE_FALSE);
    }

    @Override
    public String getApplicationUrl() {
        return getValue(APPLICATION_URL);
    }

    @Override
    public void setApplicationUrl(String applicationUrl) {
        setValue(APPLICATION_URL, applicationUrl);
    }

    @Override
    public boolean isBlogEnabled() {
        return getValue(BLOG_ENABLE).equals(VALUE_TRUE);
    }

    @Override
    public void setBlogEnabled(boolean isEnabled) {
        setValue(BLOG_ENABLE, isEnabled);
    }

    @Override
    public boolean isQQEnabled() {
        return getValue(QQ_ENABLE).equals(VALUE_TRUE);
    }

    @Override
    public void setQQEnabled(boolean isEnabled) {
        setValue(QQ_ENABLE, isEnabled);
    }

    @Override
    public String getQQId() {
        return getValue(QQ_ID);
    }

    @Override
    public void setQQId(String qqId) {
        setValue(QQ_ID, qqId);
    }

    @Override
    public String getQQSecret() {
        return getValue(QQ_SECRET);
    }

    @Override
    public void setQQSecret(String qqSecret) {
        setValue(QQ_SECRET, qqSecret);
    }

    @Override
    public boolean isWeixinEnabled() {
        return getValue(WEIXIN_ENABLED).equals(VALUE_TRUE);
    }

    @Override
    public void setWeixinEnabled(boolean isEnabled) {
        setValue(WEIXIN_ENABLED, isEnabled);
    }

    @Override
    public String getWeixinId() {
        return getValue(WEIXIN_ID);
    }

    @Override
    public void setWeixinId(String weixinId) {
        setValue(WEIXIN_ID, weixinId);
    }

    @Override
    public String getWeixinSecret() {
        return getValue(WEIXIN_SECRET);
    }

    @Override
    public void setWeixinSecret(String weixinSecret) {
        setValue(WEIXIN_SECRET, weixinSecret);
    }

    @Override
    public String getWeixinToken() {
        return getValue(WEIXIN_TOKEN);
    }

    @Override
    public void setWeixinToken(String weixinToken) {
        setValue(WEIXIN_TOKEN, weixinToken);
    }

    @Override
    public String getWeixinKey() {
        return getValue(WEIXIN_KEY);
    }

    @Override
    public void setWeixinKey(String weixinKey) {
        setValue(WEIXIN_KEY, weixinKey);
    }

    @Override
    public boolean isDownloaderEnabled() {
        return getValue(DOWNLOADER_ENABLED).equals(VALUE_TRUE);
    }

    @Override
    public void setDownloaderEnabled(boolean isEnabled) {
        setValue(DOWNLOADER_ENABLED, isEnabled);
    }

    @Override
    public String getDownloaderAria2cUrl() {
        return getValue(DOWNLOADER_ARIA2C_URL);
    }

    @Override
    public void setDownloaderAria2cUrl(String aria2cUrl) {
        setValue(DOWNLOADER_ARIA2C_URL, aria2cUrl);
    }

    @Override
    public String getDownloaderAria2cPassword() {
        return getValue(DOWNLOADER_ARIA2C_PASSWORD);
    }

    @Override
    public void setDownloaderAria2cPassword(String aria2cPassword) {
        setValue(DOWNLOADER_ARIA2C_PASSWORD, aria2cPassword);
    }

    @Override
    public String getDownloaderTransmissionUrl() {
        return getValue(DOWNLOADER_TRANSMISSION_URL);
    }

    @Override
    public void setDownloaderTransmissionUrl(String transmissionUrl) {
        setValue(DOWNLOADER_TRANSMISSION_URL, transmissionUrl);
    }

    @Override
    public String getDownloaderTransmissionUsername() {
        return getValue(DOWNLOADER_TRANSMISSION_USERNAME);
    }

    @Override
    public void setDownloaderTransmissionUsername(String transmissionUsername) {
        setValue(DOWNLOADER_TRANSMISSION_USERNAME, transmissionUsername);
    }

    @Override
    public String getDownloaderTransmissionPassword() {
        return getValue(DOWNLOADER_TRANSMISSION_PASSWORD);
    }

    @Override
    public void setDownloaderTransmissionPassword(String transmissionPassword) {
        setValue(DOWNLOADER_TRANSMISSION_PASSWORD, transmissionPassword);
    }
}
