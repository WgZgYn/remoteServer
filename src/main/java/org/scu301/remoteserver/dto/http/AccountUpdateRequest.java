package org.scu301.remoteserver.dto.http;

public record AccountUpdateRequest(String username, String password, String newUsername, String newPassword) {
}
