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

package com.lixiaocong.cms.service;

public interface IConfigService {
    String getApplicationUrl();
    void setApplicationUrl(String applicationUrl);

    boolean isBlogEnabled();
    void setBlogEnabled(boolean isEnabled);

    boolean isQQEnabled();
    void setQQEnabled(boolean isEnabled);
    String getQQId();
    void setQQId(String qqId);
    String getQQSecret();
    void setQQSecret(String qqSecret);

    boolean isWeixinEnabled();
    void setWeixinEnabled(boolean isEnabled);
    String getWeixinId();
    void setWeixinId(String weixinId);
    String getWeixinSecret();
    void setWeixinSecret(String weixinSecret);
    String getWeixinToken();
    void setWeixinToken(String weixinToken);
    String getWeixinKey();
    void setWeixinKey(String weixinKey);

    boolean isDownloaderEnabled();
    void setDownloaderEnabled(boolean isEnabled);
    String getDownloaderAria2cUrl();
    void setDownloaderAria2cUrl(String aria2cUrl);
    String getDownloaderAria2cPassword();
    void setDownloaderAria2cPassword(String aria2cPassword);
    String getDownloaderTransmissionUrl();
    void setDownloaderTransmissionUrl(String transmissionUrl);
    String getDownloaderTransmissionUsername();
    void setDownloaderTransmissionUsername(String transmissionUsername);
    String getDownloaderTransmissionPassword();
    void setDownloaderTransmissionPassword(String transmissionPassword);
}
