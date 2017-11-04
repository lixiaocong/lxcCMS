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

package com.lixiaocong.cms.dev;

import com.lixiaocong.cms.entity.User;
import com.lixiaocong.cms.repository.IUserRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;


//TODO if don't set order, there will be an error, to be fixed
@Profile("develop")
@Order(1)
@Configuration
public class DevWebSecurityConfig extends WebSecurityConfigurerAdapter{

    private Log log = LogFactory.getLog(getClass());

    private IUserRepository userRepository;

    @Autowired
    public DevWebSecurityConfig(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll().and().formLogin().loginPage("/signin").defaultSuccessUrl("/blog").and().logout().logoutUrl("/logout").logoutSuccessUrl("/blog").and().rememberMe().rememberMeParameter("remember-me").and().csrf().disable();

        User admin = new User("admin", "123456");
        admin.setAdmin(true);
        userRepository.save(admin);
        http.addFilterAfter(new DevSigninFilter(userRepository), SecurityContextPersistenceFilter.class);
    }
}
