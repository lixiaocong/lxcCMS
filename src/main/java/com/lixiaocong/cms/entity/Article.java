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

package com.lixiaocong.cms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.htmlparser.jericho.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Article extends AbstractEntity {
    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @ManyToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private String summary;

    @Lob
    @Column
    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE})
    private Set<Comment> comments;

    public Article() {
        comments = new HashSet<>();
    }

    public Article(String title, String content, User user) {
        comments = new HashSet<>();
        this.title = title;
        this.content = content;
        this.user = user;
    }

    @PrePersist
    public void prePersist() {
        super.prePersist();
        getSummaryAndImage();
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
        getSummaryAndImage();
    }

    private void getSummaryAndImage() {
        String summary = this.getContent();
        Source source = new Source(summary);

        //设置摘要
        Segment segment = new Segment(source, 0, summary.length() - 1);
        TextExtractor textExtractor = new TextExtractor(segment);
        summary = textExtractor.toString();
        if (summary.length() > 200) summary = summary.substring(0, 200) + "......";
        this.setSummary(summary);

        //处理图片
        Element img = source.getFirstElement(HTMLElementName.IMG);
        if (img != null) {
            String href = img.getAttributeValue("src");
            if (href != null) this.setImage(href);
        }
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
