package com.example.demoproject.helper;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component("sessionGetter")
public class SessionGetter {

    public HttpSession getSession(){
HttpSession session = null;
        try {
           session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        }

        catch (Exception e){e.printStackTrace();}
 return session;
    }

}
