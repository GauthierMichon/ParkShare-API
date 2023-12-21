package org.rncp

import kotlinx.serialization.Serializable

@Serializable
data class PingResponse(val status: String)
