package me.j360.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.InitBinder;

import javax.annotation.PostConstruct;

/**
 * Package: me.j360.conf
 * User: min_xu
 * Date: 16/3/22 下午10:53
 * 说明：
 */

@Service
public class TestService {
    @Value("#{app.logRoot}")
    private String logRoot;

    @PostConstruct
    public void init(){
        System.out.println(logRoot);
    }
}
