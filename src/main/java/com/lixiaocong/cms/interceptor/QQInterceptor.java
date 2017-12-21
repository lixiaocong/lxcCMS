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

package com.lixiaocong.cms.interceptor;

import com.lixiaocong.cms.exception.ModuleDisabledException;
import com.lixiaocong.cms.service.IConfigService;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QQInterceptor extends HandlerInterceptorAdapter {

    private final IConfigService configService;
    private final ConnectController connectController;
    private final ProviderSignInController signInController;
    private String applicationUrl;


    public QQInterceptor(IConfigService configService, ConnectController connectController, ProviderSignInController signInController) {
        this.configService = configService;
        this.connectController = connectController;
        this.signInController = signInController;
        this.applicationUrl = configService.getApplicationUrl();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(this.configService.isQQEnabled()) {
            String newApplicationUrl = this.configService.getApplicationUrl();
            if(!newApplicationUrl.equals(this.applicationUrl)) {
                this.applicationUrl = newApplicationUrl;
                connectController.setApplicationUrl(newApplicationUrl);
                connectController.afterPropertiesSet();
                signInController.setApplicationUrl(newApplicationUrl);
                signInController.afterPropertiesSet();
            }
            return true;
        }
        throw new ModuleDisabledException("QQ module is disabled");
    }
}
