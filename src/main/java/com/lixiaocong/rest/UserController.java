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

import com.lixiaocong.entity.User;
import com.lixiaocong.exception.RestParamException;
import com.lixiaocong.model.UserUpdateForm;
import com.lixiaocong.service.IUserService;
import net.gplatform.spring.social.qq.api.QQ;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.inject.Provider;
import javax.validation.Valid;
import java.security.Principal;
import java.util.Map;

@RestController
@RolesAllowed("ROLE_USER")
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;
    private final BCryptPasswordEncoder encoder;
    private final Provider<ConnectionRepository> connectionRepositoryProvider;
    private Log logger = LogFactory.getLog(getClass());

    @Autowired
    public UserController(IUserService userService, Provider<ConnectionRepository> connectionRepositoryProvider) {
        this.userService = userService;
        this.encoder = new BCryptPasswordEncoder();
        this.connectionRepositoryProvider = connectionRepositoryProvider;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Map<String, Object> info(Principal principal) {
        User user = userService.getByUsername(principal.getName());
        Map<String, Object> ret = ResponseMsgFactory.createSuccessResponse("user", user);
        ConnectionRepository connectionRepository = connectionRepositoryProvider.get();

        Connection<Facebook> facebookConnection = connectionRepository.findPrimaryConnection(Facebook.class);
        ret.put("facebook", facebookConnection != null);
        Connection<QQ> qqConnection = connectionRepository.findPrimaryConnection(QQ.class);
        ret.put("qq", qqConnection != null);

        return ret;
    }

    @RolesAllowed("ROLE_ADMIN")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@PathVariable long id) {
        userService.delete(id);
        return ResponseMsgFactory.createSuccessResponse();
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Map<String, Object> put(@RequestBody @Valid UserUpdateForm userUpdateForm, BindingResult result, Principal principal) throws RestParamException {
        if (result.hasErrors()) throw new RestParamException();

        User user = userService.getByUsername(principal.getName());
        user.setPassword(this.encoder.encode(userUpdateForm.getPassword()));
        userService.update(user);
        return ResponseMsgFactory.createSuccessResponse();
    }

    @RolesAllowed("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> get(@RequestParam(value = "page", required = false, defaultValue = "1") int page, @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseMsgFactory.createSuccessResponse("users", userService.get(page - 1, size));
    }
}