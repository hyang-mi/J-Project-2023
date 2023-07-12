package com.mjuAppSW.appName.domain.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberApiController {

    @Autowired
    private MemberService memberService;

    @PostMapping("/createMember")
    public HttpStatus createMember(){
        boolean ok = memberService.createMember();
        return HttpStatus.OK;
    }
}
