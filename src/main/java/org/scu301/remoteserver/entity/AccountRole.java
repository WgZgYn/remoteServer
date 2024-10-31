package org.scu301.remoteserver.entity;

import org.hibernate.annotations.Type;

/*
This is for account_role postgresql type,
it will not work if ADMIN but admin
 */
// FIXME:
public enum AccountRole {
    admin,
    user
}
