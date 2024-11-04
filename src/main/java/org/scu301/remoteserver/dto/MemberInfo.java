package org.scu301.remoteserver.dto;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.scu301.remoteserver.entity.Member;

import java.util.*;

public record MemberInfo(Map<String, List<String>> house_member) {
    @Contract("_ -> new")
    public static @NotNull MemberInfo of(@NotNull Set<Member> members) {
        Map<String, List<String>> house_member = new HashMap<>();
        for (Member member : members) {
            house_member.computeIfAbsent(member.getHouse().getHouseName(), k -> new ArrayList<>()).add(member.getAccount().getUsername());
        }
        return new MemberInfo(house_member);
    }
}
