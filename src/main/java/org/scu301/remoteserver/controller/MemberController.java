package org.scu301.remoteserver.controller;

import lombok.extern.slf4j.Slf4j;
import org.scu301.remoteserver.dto.MemberInfo;
import org.scu301.remoteserver.entity.Account;
import org.scu301.remoteserver.entity.Member;
import org.scu301.remoteserver.security.Claims;
import org.scu301.remoteserver.util.Response;
import org.scu301.remoteserver.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/my/member")
public class MemberController {
    @GetMapping("/member")
    Result listHouseMember(@RequestAttribute("claims") Claims claims) {
        Optional<Account> accountOptional = dataBaseReadService.getAccount(claims.id());
        if (accountOptional.isPresent()) {
            Set<Member> members = accountOptional.get().getMembers();
            return Response.of(MemberInfo.of(members));
        }
        return Response.of(new ArrayList<>());
    }
}
