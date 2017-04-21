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

package com.lixiaocong.cms.controller;

import com.lixiaocong.cms.entity.User;
import com.lixiaocong.cms.exception.ControllerParamException;
import com.lixiaocong.cms.model.UserSignUpForm;
import com.lixiaocong.cms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class SignController {
    private final ProviderSignInUtils providerSignInUtils;
    private final IUserService userService;
    private final BCryptPasswordEncoder encoder;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SignController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository connectionRepository, IUserService userService, UserDetailsService userDetailsService) {
        this.providerSignInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
        this.userService = userService;
        this.encoder = new BCryptPasswordEncoder();
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping("/signin")
    public String signin() {
        return "sign/signin";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signup(WebRequest request) {
        ModelAndView ret = new ModelAndView("sign/signup");
        Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);

        if (connection != null) {
            String name = connection.fetchUserProfile().getName();
            ret.addObject("username", name);
            ret.addObject("message", "请设置密码");
        }
        return ret;
    }

    @RequestMapping(value = "/singup", method = RequestMethod.POST)
    public ModelAndView post(@Valid UserSignUpForm user, BindingResult result, WebRequest request) throws ControllerParamException {
        if (result.hasErrors()) throw new ControllerParamException();

        try {
            User localUser = userService.create(user.getUsername(), encoder.encode(user.getPassword()));
            providerSignInUtils.doPostSignUp(user.getUsername(), request);

            UserDetails userDetails = userDetailsService.loadUserByUsername(localUser.getUsername());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return new ModelAndView("/");
        } catch (Exception e) {
            ModelAndView ret = new ModelAndView("sign/signup");
            ret.addObject("message", "注册失败,请尝试其他用户名");
            return ret;
        }
    }
}
