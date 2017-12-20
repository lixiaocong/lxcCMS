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

import java.util.HashSet;
import java.util.Set;

@Service
public class ConfigService implements IConfigService {
    private static final String VALUE_TRUE = "1";
    private static final String VALUE_FALSE = "0";

    public static final String BLOG_ENABLED = "blogEnabled";

    public static final String APPLICATION_URL = "applicationUrl";
    public static final String QQ_ENABLED = "qqEnabled";
    public static final String QQ_ID = "qqId";
    public static final String QQ_SECRET = "qqSecret";

    public static final String WEIXIN_ENABLED = "weixinEnabled";
    public static final String WEIXIN_ID = "weixinId";
    public static final String WEIXIN_SECRET = "weixinSecret";
    public static final String WEIXIN_TOKEN = "weixinToken";
    public static final String WEIXIN_KEY = "weixinKey";

    public static final String DOWNLOADER_ENABLED = "downloaderEnabled";
    public static final String DOWNLOADER_ARIA2C_URL = "aria2cUrl";
    public static final String DOWNLOADER_ARIA2C_PASSWORD = "aria2cPassword";
    public static final String DOWNLOADER_TRANSMISSION_URL = "transmissionUrl";
    public static final String DOWNLOADER_TRANSMISSION_USERNAME = "transmissionUsername";
    public static final String DOWNLOADER_TRANSMISSION_PASSWORD = "transmissionPassword";
    static Set<String> keySet = new HashSet<>();

    static {
        keySet.add(BLOG_ENABLED);
        keySet.add(APPLICATION_URL);
        keySet.add(QQ_ENABLED);
        keySet.add(QQ_ID);
        keySet.add(QQ_SECRET);
        keySet.add(WEIXIN_ENABLED);
        keySet.add(WEIXIN_ID);
        keySet.add(WEIXIN_KEY);
        keySet.add(WEIXIN_SECRET);
        keySet.add(WEIXIN_TOKEN);
        keySet.add(DOWNLOADER_ENABLED);
        keySet.add(DOWNLOADER_ARIA2C_URL);
        keySet.add(DOWNLOADER_ARIA2C_PASSWORD);
        keySet.add(DOWNLOADER_TRANSMISSION_URL);
        keySet.add(DOWNLOADER_TRANSMISSION_USERNAME);
        keySet.add(DOWNLOADER_TRANSMISSION_PASSWORD);
    }

    private final IConfigRepository configRepository;

    @Autowired
    public ConfigService(IConfigRepository configRepository) {
        this.configRepository = configRepository;
    }

    private String getValue(String key) {
        Config config = this.configRepository.findByKey(key);
        return config == null ? "" : config.getValue();
    }

    @Override
    public void setValue(String key, String value) {
        if (!keySet.contains(key))
            return;
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
        return getValue(BLOG_ENABLED).equals(VALUE_TRUE);
    }

    @Override
    public void setBlogEnabled(boolean isEnabled) {
        setValue(BLOG_ENABLED, isEnabled);
    }

    @Override
    public boolean isQQEnabled() {
        return getValue(QQ_ENABLED).equals(VALUE_TRUE);
    }

    @Override
    public void setQQEnabled(boolean isEnabled) {
        setValue(QQ_ENABLED, isEnabled);
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
