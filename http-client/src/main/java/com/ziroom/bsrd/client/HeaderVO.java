package com.ziroom.bsrd.client;

import lombok.Getter;
import lombok.Setter;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chengys4
 *         2018-01-05 19:13
 **/
@Getter
@Setter
public class HeaderVO {

    private CookieStore cookieStore;

    private List<Header> headerList;

    public void addCookie(Cookie cookie) {

        if (cookieStore == null) {
            cookieStore = new BasicCookieStore();
        }
        cookieStore.addCookie(cookie);
    }

    public void addHeader(Header header) {
        if (this.headerList == null) {
            this.headerList = new ArrayList<>();
        }
        this.headerList.add(header);
    }
}
