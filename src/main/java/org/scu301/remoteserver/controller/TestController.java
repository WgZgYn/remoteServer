package org.scu301.remoteserver.controller;

import org.scu301.remoteserver.entity.Member;
import org.scu301.remoteserver.repository.AccountRepository;
import org.scu301.remoteserver.util.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private final AccountRepository accountRepository;

    public TestController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/ping")
    Response<String> ping() {
        return Response.of("pong");
    }

    @GetMapping("/list/member")
    Response<String> listMember() {
        accountRepository.findAll().forEach(account -> {
            System.out.println("--------------------");
            System.out.println(account.getId());
            System.out.println(account.getUsername());
            for (Member member : account.getMembers()) {
                System.out.print(member.getAccount().getUsername());
                System.out.print("<->");
                System.out.println(member.getHouse().getHouseName());
            }
        });
        return Response.of("ok");
    }

    @GetMapping("/list/accountId")
    Response<String> testAccountQuery() {
        accountRepository.findAllAccountId().forEach(account -> System.out.println(account.id()));
        return Response.of("ok");
    }


    @GetMapping("/list/houseId")
    Response<String> testHouseQuery() {
        accountRepository.findAllHouseIdById(3).forEach(account -> System.out.println(account.id()));
        return Response.of("ok");
    }
}
