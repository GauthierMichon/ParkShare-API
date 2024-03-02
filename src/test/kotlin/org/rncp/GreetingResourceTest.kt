package org.rncp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.Test

@QuarkusTest
class GreetingResourceTest {

    @Test
    fun testPingEndpoint() {
        given()
            .`when`().get("/api/ping")
            .then()
                .statusCode(200)
                .body(`is`("{\"status\":\"OK\"}"))
    }

}