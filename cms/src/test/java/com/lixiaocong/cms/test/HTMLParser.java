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

package com.lixiaocong.cms.test;

import net.htmlparser.jericho.*;
import org.junit.Test;

public class HTMLParser {
    @Test
    public void ContentTest() {
        String str = "<div><b>O</b>ne</div><div title=\"Two\"><b>Th</b><script>//a script </script>ree</div>";
        Source source = new Source(str);
        Segment segment = new Segment(source, 0, str.length() - 1);
        TextExtractor textExtractor = new TextExtractor(segment);
        assert ("One Three".equals(textExtractor.toString()));
    }

    @Test
    public void ImgTest() {
        String str = "<img width='167' height='410' src='/images/nav_logo242.png' alt='Google'>";
        Source source = new Source(str);
        Element img = source.getFirstElement(HTMLElementName.IMG);
        assert (img != null);
        String href = img.getAttributeValue("src");
        System.out.println(href);
        assert ("/images/nav_logo242.png".equals(href));
    }
}
