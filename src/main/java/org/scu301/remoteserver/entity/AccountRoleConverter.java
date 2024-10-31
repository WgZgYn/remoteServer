package org.scu301.remoteserver.entity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AccountRoleConverter implements AttributeConverter<AccountRole, String> {

    @Override
    public String convertToDatabaseColumn(AccountRole accountRole) {
        return switch (accountRole) {
            case user -> "user";
            case admin -> "admin";
        };
    }

    @Override
    public AccountRole convertToEntityAttribute(String s) {
        return switch (s) {
            case "user" -> AccountRole.user;
            case "admin" -> AccountRole.admin;
            default -> throw new IllegalStateException("Unexpected value: " + s);
        };
    }
}
