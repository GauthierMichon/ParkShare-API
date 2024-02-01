package org.rncp.role.domain.model

data class Role(
        var id: Int? = null,
        var label: String,
) {
    constructor(): this(null, "")
}
