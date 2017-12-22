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

package com.lixiaocong.cms.rest;

import com.lixiaocong.cms.service.IConfigService;
import com.lixiaocong.cms.service.impl.ImageCodeService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private static final Log logger = LogFactory.getLog(FileController.class);

    private final ImageCodeService codeService;
    private final IConfigService configService;

    private String fileDir;

    @Autowired
    public FileController(ImageCodeService codeService, IConfigService configService) {
        this.codeService = codeService;
        this.configService = configService;
        this.fileDir = configService.getStorageDir();
        if (this.fileDir.endsWith("/"))
            this.fileDir = fileDir.substring(0, fileDir.length() - 1);
    }

    @RolesAllowed("ROLE_USER")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String post(MultipartFile imageFile) {
        try {
            File newFile = new File(fileDir + "image/" + UUID.randomUUID() + imageFile.getOriginalFilename());
            imageFile.transferTo(newFile);
            return "image/" + newFile.getName();
        } catch (Exception e) {
            logger.error(e);
        }
        return "image/" + "error.jpg";
    }

    @RolesAllowed("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET)
    public Map<String, Object> get(@RequestParam String path) {
        class Item {
            private boolean isFile;
            private String name;

            public Item(String name, boolean isFile) {
                this.name = name;
                this.isFile = isFile;
            }

            public boolean isFile() {
                return isFile;
            }

            public void setFile(boolean file) {
                isFile = file;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }

        File folderFile = new File(fileDir + path);
        if (!folderFile.exists()) return ResponseMsgFactory.createFailedResponse("目标文件夹不存在");
        List<Item> ret = new LinkedList<>();
        File[] fileList = folderFile.listFiles();
        for (File file : fileList)
            ret.add(new Item(file.getName(), file.isFile()));
        Map<String, Object> response = ResponseMsgFactory.createSuccessResponse("files", ret);
        return response;
    }

    @RolesAllowed("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.DELETE)
    public Map<String, Object> delete(@RequestParam String fileName) {
        File file = new File(fileDir + fileName);
        if (!file.exists()) return ResponseMsgFactory.createFailedResponse("文件不存在");
        else {
            if (file.isFile()) {
                if (!file.delete())
                    return ResponseMsgFactory.createFailedResponse("删除不成功");
            } else {
                try {
                    FileUtils.deleteDirectory(file);
                } catch (IOException e) {
                    return ResponseMsgFactory.createFailedResponse("删除不成功");
                }
            }
        }
        return ResponseMsgFactory.createSuccessResponse();
    }

    @RequestMapping(value = "/imagecode")
    public void imagecode(HttpSession session, HttpServletResponse response) throws IOException {
        response.setContentType("image/png");
        OutputStream os = response.getOutputStream();
        ImageIO.write(codeService.getImage(session), "png", os);
    }
}
