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

package com.lixiaocong.cms.security;

import com.lixiaocong.cms.repository.IUserRepository;
import com.lixiaocong.cms.service.ImageCodeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@Component
public class DaoBasedUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;
    private Log log = LogFactory.getLog(getClass());
    private ImageCodeService codeService;

    @Autowired
    public DaoBasedUserDetailsService(IUserRepository userRepository, ImageCodeService codeService) {
        this.userRepository = userRepository;
        this.codeService = codeService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if (request.getRequestURI().equals("/signin")) {
            String postcode = request.getParameter("imagecode");
            if (!codeService.check(postcode, request.getSession()))
                throw new UsernameNotFoundException("image code error");
        }
        com.lixiaocong.cms.entity.User user = userRepository.findByUsername(username);
        if (user == null) {
            log.info("user not exists");
            throw new UsernameNotFoundException("user not exists");
        }
        Set<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        if (user.isAdmin()) authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return new DaoBasedUserDetails(user.getId(), user.getUsername(), user.getPassword(), authorities);
    }
}
