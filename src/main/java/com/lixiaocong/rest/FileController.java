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

import com.lixiaocong.service.ImageCodeService;
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
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/file")
public class FileController {
    private final ImageCodeService codeService;
    private Log logger = LogFactory.getLog(getClass());
    @Value("${image.folder}")
    private String imageFolder;
    @Value("${image.server}")
    private String imageServer;
    @Value("${nginx.root}")
    private String downloadFilePath;
    @Value("${nginx.url}")
    private String serverUrl;

    @Autowired
    public FileController(ImageCodeService codeService) {
        this.codeService = codeService;
    }

    @RolesAllowed("ROLE_USER")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String post(MultipartFile imageFile) {
        try {
            File newFile = new File(imageFolder + UUID.randomUUID() + imageFile.getOriginalFilename());
            imageFile.transferTo(newFile);
            return imageServer + newFile.getName();
        } catch (Exception e) {
            logger.error(e);
        }
        return imageServer + "error.jpg";
    }

    @RolesAllowed("ROLE_ADMIN")
    @RequestMapping(value = "/video", method = RequestMethod.GET)
    public Map<String, Object> video() {
        File file = new File(downloadFilePath);
        if (!file.exists()) return ResponseMsgFactory.createFailedResponse("目标文件夹不存在");
        List<String> fileList = new LinkedList<>();
        File[] files = file.listFiles();
        if (files == null) return ResponseMsgFactory.createFailedResponse("files 为空");
        for (File video : files) {
            if (video.isFile()) fileList.add(video.getName());
        }
        Map<String, Object> ret = ResponseMsgFactory.createSuccessResponse("videos", fileList);
        ret.put("serverUrl", serverUrl);
        return ret;
    }

    @RolesAllowed("ROLE_ADMIN")
    @RequestMapping(value = "/video", method = RequestMethod.DELETE)
    public Map<String, Object> delete(@RequestParam String fileName) {
        File file = new File(downloadFilePath + fileName);
        if (!file.exists()) return ResponseMsgFactory.createFailedResponse("文件不存在");
        else if (!file.isFile()) return ResponseMsgFactory.createFailedResponse("不是文件");
        else if (!file.delete()) return ResponseMsgFactory.createFailedResponse("删除不成功");
        return ResponseMsgFactory.createSuccessResponse();
    }

    @RequestMapping(value = "/imagecode")
    public void imagecode(HttpSession session, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(codeService.getImage(session), "png", os);
    }
}
