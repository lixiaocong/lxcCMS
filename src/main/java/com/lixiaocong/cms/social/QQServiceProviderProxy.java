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

package com.lixiaocong.cms.social;

import com.lixiaocong.cms.service.IConfigService;
import com.lixiaocong.social.qq.api.QQ;
import com.lixiaocong.social.qq.connect.QQServiceProvider;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

public class QQServiceProviderProxy implements OAuth2ServiceProvider<QQ> {
    private IConfigService configService;

    private QQServiceProvider serviceProvider;
    private String id;
    private String secret;

    public QQServiceProviderProxy(IConfigService configService) {
        this.configService = configService;
        this.id=configService.getQQId();
        this.secret=configService.getQQSecret();
        this.serviceProvider = new QQServiceProvider(this.id,this.secret);
    }

    @Override
    public OAuth2Operations getOAuthOperations() {
        checkUpdate();
        return serviceProvider.getOAuthOperations();
    }

    @Override
    public QQ getApi(String accessToken) {
        checkUpdate();
        return serviceProvider.getApi(accessToken);
    }

    private void checkUpdate(){
        String newId = this.configService.getQQId();
        String newSecret = this.configService.getQQSecret();
        if(!newId.equals(this.id)||!newSecret.equals(this.secret)) {
            this.id=newId;
            this.secret=newSecret;
            this.serviceProvider = new QQServiceProvider(newId, newSecret);
        }
    }
}
