package org.scu301.remoteserver.controller;

import org.scu301.remoteserver.util.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/ping")
    Response<String> ping() {
        return Response.ok("pong");
    }
}
