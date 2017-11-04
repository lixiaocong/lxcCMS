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

package com.lixiaocong.cms.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Service
public class ImageCodeService {
    private static final char[] ch = "ABCDEFGHJKLMNPQRSTUVWXY3456789".toCharArray();
    private static final int WIDTH = 70;
    private static final int HEIGHT = 30;

    public BufferedImage getImage(HttpSession session) {
        //background
        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bi.getGraphics();
        graphics.setColor(randomColor());
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        //content
        StringBuilder sb = new StringBuilder();
        int len = ch.length;
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(len);
            graphics.setColor(randomColor());
            graphics.setFont(new Font(null, Font.BOLD + Font.ITALIC, 30));
            graphics.drawString(ch[index] + "", i * 15 + 5, 25);
            sb.append(ch[index]);
        }
        session.setAttribute("imagecode", sb.toString());

        //lines
        for (int i = 0; i < 4; i++) {
            graphics.setColor(randomColor());
            graphics.drawLine(random.nextInt(WIDTH), random.nextInt(HEIGHT), random.nextInt(WIDTH), random.nextInt(HEIGHT));
        }

        return bi;
    }

    public boolean check(String code, HttpSession session) {
        Object imagecode = session.getAttribute("imagecode");
        return imagecode != null && code.toLowerCase().equals(((String) imagecode).toLowerCase());
    }

    private Color randomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}
