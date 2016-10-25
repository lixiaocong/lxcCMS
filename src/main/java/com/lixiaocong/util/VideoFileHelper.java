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

package com.lixiaocong.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * 封装常用文件操作
 */
public class VideoFileHelper {
    public static Log logger = LogFactory.getLog("helper");

    public static List<File> findAllVideos(File file, String[] types) {
        List<File> ret = new LinkedList<>();

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) for (File subFile : files) {
                List<File> subFiles = findAllVideos(subFile, types);
                ret.addAll(subFiles);
            }
        } else {
            String name = file.getName();
            for (String type : types)
                if (name.endsWith(type)) {
                    ret.add(file);
                    break;
                }
        }
        return ret;
    }

    public static boolean moveFiles(List<File> files, String destPath) {
        File dir = new File(destPath);
        if (!dir.isDirectory()) return false;

        boolean ret = true;
        for (File file : files) {
            if (!file.renameTo(new File(destPath + file.getName()))) {
                ret = false;
                logger.error("move " + file.getName() + " error");
            }
        }
        return ret;
    }
}
