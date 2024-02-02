package org.rncp.user.domain.model

data class User(
        var id: Int? = null,
        var uid: String? = null,
        var firstname: String,
        var lastname: String,
        var email: String,
        var roleId: Int,
) {
    constructor(): this(null, "", "", "", "", 1)
}
