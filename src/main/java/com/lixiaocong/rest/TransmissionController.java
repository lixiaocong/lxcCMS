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

import com.lixiaocong.transmission4j.TransmissionClient;
import com.lixiaocong.transmission4j.response.Torrent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RolesAllowed("ROLE_ADMIN")
@RequestMapping("/transmission")
public class TransmissionController {
    private Log logger = LogFactory.getLog(getClass());
    private TransmissionClient client;

    @Autowired
    public TransmissionController(@Value("${transmission.username}") String username, @Value("${transmission.password}") String password) {
        this.client = new TransmissionClient(username, password, "http://127.0.0.1:9091/transmission/rpc");
    }

    @RequestMapping(method = RequestMethod.POST)
    public Map<String, Object> post(MultipartFile file) {
        try {
            String metainfo = Base64.getEncoder().encodeToString(file.getBytes());
            if (client.add(metainfo)) return ResponseMsgFactory.createSuccessResponse();
            return ResponseMsgFactory.createFailedResponse("transmission内部错误");
        } catch (Exception e) {
            logger.error("transmission 配置错误");
            return ResponseMsgFactory.createFailedResponse("transmission配置错误");
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public Map<String, Object> delete(@RequestParam(required = false, defaultValue = "-1") int id) {
        List<Integer> ids = null;
        if (id != -1) {
            ids = new LinkedList<>();
            ids.add(id);
        }
        try {
            if (client.remove(ids)) return ResponseMsgFactory.createSuccessResponse();
            return ResponseMsgFactory.createFailedResponse("transmission内部错误");
        } catch (Exception e) {
            logger.error("transmission 配置错误");
            return ResponseMsgFactory.createFailedResponse("transmission配置错误");
        }
    }

    @RequestMapping(value = "/start", method = RequestMethod.PUT)
    public Map<String, Object> start(@RequestParam(required = false, defaultValue = "-1") int id) {
        List<Integer> ids = null;
        if (id != -1) {
            ids = new LinkedList<>();
            ids.add(id);
        }
        try {
            if (client.start(ids)) return ResponseMsgFactory.createSuccessResponse();
            return ResponseMsgFactory.createFailedResponse("transmission内部错误");
        } catch (Exception e) {
            logger.error("transmission 配置错误");
            return ResponseMsgFactory.createFailedResponse("transmission配置错误");
        }
    }

    @RequestMapping(value = "/stop", method = RequestMethod.PUT)
    public Map<String, Object> stop(@RequestParam(required = false, defaultValue = "-1") int id) {
        List<Integer> ids = null;
        if (id != -1) {
            ids = new LinkedList<>();
            ids.add(id);
        }
        try {
            if (client.stop(ids)) return ResponseMsgFactory.createSuccessResponse();
            return ResponseMsgFactory.createFailedResponse("transmission内部错误");
        } catch (Exception e) {
            logger.error("transmission 配置错误");
            return ResponseMsgFactory.createFailedResponse("transmission配置错误");
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> get() {
        try {
            List<Torrent> torrents = client.getAll();
            return ResponseMsgFactory.createSuccessResponse("torrents", torrents);
        } catch (Exception e) {
            logger.error("transmission 配置错误");
            return ResponseMsgFactory.createFailedResponse("transmission配置错误");
        }
    }
}
