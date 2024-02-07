package org.rncp.okhttp3

import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import okhttp3.OkHttpClient

@ApplicationScoped
@RegisterForReflection
class OkHttp3Client {
    val client: OkHttpClient = OkHttpClient()
}