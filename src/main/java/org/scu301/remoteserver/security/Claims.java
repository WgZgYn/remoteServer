package org.scu301.remoteserver.security;

public record Claims(Integer id, String sub, String role, long exp) {
}
