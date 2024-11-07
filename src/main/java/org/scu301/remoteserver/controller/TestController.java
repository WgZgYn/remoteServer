package org.scu301.remoteserver.controller;

import org.scu301.remoteserver.entity.House;
import org.scu301.remoteserver.entity.Member;
import org.scu301.remoteserver.repository.AccountRepository;
import org.scu301.remoteserver.repository.MemberRepository;
import org.scu301.remoteserver.util.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    private final AccountRepository accountRepository;
    private final MemberRepository memberRepository;

    public TestController(AccountRepository accountRepository, MemberRepository memberRepository) {
        this.accountRepository = accountRepository;
        this.memberRepository = memberRepository;
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
        accountRepository.findAllAccountId().forEach(System.out::println);
        return Response.of("ok");
    }


    @GetMapping("/list/houseId")
    Response<String> testHouseQuery() {
//        accountRepository.findAllHouseIdById(3).forEach(System.out::println);
        return Response.of("ok");
    }

//    @GetMapping("/list/house")
//    Response<String> testHouse() {
//        List<House> houses = memberRepository.findHouseByAccountId(3);
//        houses.forEach(house -> System.out.println(house.getId()));
//        return Response.of("ok");
//    }
}
