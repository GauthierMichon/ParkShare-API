package org.rncp.user.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.json.Json
import okhttp3.*
import org.rncp.okhttp3.OkHttp3Client

@ApplicationScoped
class LoginUseCase {
    @Inject
    private lateinit var okHttp3Client: OkHttp3Client

    fun execute(url: String, body: String): Response {
        val requestBody = RequestBody.create(MediaType.parse("application/json"), body)
        val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

        return okHttp3Client.client.newCall(request).execute()
    }
}