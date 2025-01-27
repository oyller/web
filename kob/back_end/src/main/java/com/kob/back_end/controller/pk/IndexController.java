package com.kob.back_end.controller.pk;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.support.AsyncHandlerMethodReturnValueHandler;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/pk/")
public class IndexController {
    @RequestMapping("index/")
    public String index()
    {

        return "/pk/index.html";
    }


}
