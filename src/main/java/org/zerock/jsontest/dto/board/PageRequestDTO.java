package org.zerock.jsontest.dto.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PageRequestDTO {
    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 20;
    private String type; //t, c, w, tc, tw, twc
    private String keyword;
    private String link;
    private String sort;

    public String[] getTypes() {
        if (type == null || type.isEmpty()) {
            return null;
        }
        return type.split("");
    }

    public Pageable getSortedPageable() {
        if (this.sort != null) {
            switch (this.sort) {
                case "viewCount":
                    return PageRequest.of(this.page - 1, this.size, Sort.by("viewCount").descending());
                case "likeCount":
                    return PageRequest.of(this.page - 1, this.size, Sort.by("likeCount").descending());
                default:
                    return PageRequest.of(this.page - 1, this.size, Sort.by("bno").descending());
            }
        } else {
            return PageRequest.of(this.page - 1, this.size, Sort.by("bno").descending());
        }
    }

    public String getLink() {
        if (link == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
            builder.append("&size=" + this.size);
            if (type != null && type.length() > 0) {
                builder.append("&type=" + type);
            }
            if (keyword != null) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            link = builder.toString();
        }
        return link;
    }
}
