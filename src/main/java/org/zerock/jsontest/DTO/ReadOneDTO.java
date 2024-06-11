package org.zerock.jsontest.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReadOneDTO {
    private Response response;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response {
        private Header header;
        private Body body;

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Header {
            private String resultCode;
            private String resultMsg;
        }

        @Getter
        @Setter
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Body {
            private Items items;
            private Integer numOfRows;
            private Integer pageNo;
            private Integer totalCount;

            @Getter
            @Setter
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Items {
                private Item[] item;

                @Getter
                @Setter
                @JsonIgnoreProperties(ignoreUnknown = true)
                public static class Item {
                    private String contentid;
                    private String contenttypeid;
                    private String title;
                    private String createdtime;
                    private String modifiedtime;
                    private String tel;
                    private String telname;
                    private String homepage;
                    private String booktour;
                    private String firstimage;
                    private String firstimage2;
                    private String cpyrhtDivCd;
                    private String areacode;
                    private String sigungucode;
                    private String cat1;
                    private String cat2;
                    private String cat3;
                    private String addr1;
                    private String addr2;
                    private String zipcode;
                    private String mapx;
                    private String mapy;
                    private String mlevel;
                    private String overview;
                }
            }
        }
    }
}
