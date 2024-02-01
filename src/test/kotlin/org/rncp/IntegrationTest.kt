package org.rncp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.rncp.feedback.domain.model.Feedback
import org.testcontainers.junit.jupiter.Testcontainers

@QuarkusTest
class IntegrationTest {

    /*@Test
    fun testPost() {
        // Données à envoyer dans la requête POST
        val requestAd = """{
            "id": 1,
            "userId": "H9oF3SdqfOgs5uOkDiD1Q0OLDGP2",
            "name": "test2",
            "description": "test balablalaa",
            "hourPrice": 2.6,
            "latitude": "-0.256245656",
            "longitude": "30.29562656",
            "state": true,
            "link": "fnefreferz"
        }""".trimIndent()
        val requestBody = """
            {
                "adId": 1,
                "userId": "aaaaaaaaaaaaaaaaaaaa",
                "rating": 4,
                "description": "aaaaaaaaaaaaaaaaaaaa",
                "date": "aaaaaaaaaaaaaaaaaaaa"
            }
        """.trimIndent()

        // Effectuer la requête POST
        *//*val response = *//*
        io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .response()
        io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/feedback")
                .then()
                .statusCode(201)
                .extract()
                .response()

        // Vérifier la réponse ou d'autres assertions selon votre logique métier
        // val responseBody = response.body().asString()

        assertEquals("test", "test")
    }

    @Test
    fun testPostFeedback() {
        // Données à envoyer dans la requête POST
        val requestAd = """{
            "id": 1,
            "userId": "H9oF3SdqfOgs5uOkDiD1Q0OLDGP2",
            "name": "test2",
            "description": "test balablalaa",
            "hourPrice": 2.6,
            "latitude": "-0.256245656",
            "longitude": "30.29562656",
            "state": true,
            "link": "fnefreferz"
        }""".trimIndent()
        val requestBody = """
            {
                "adId": 1,
                "userId": "test",
                "rating": 4,
                "description": "testl",
                "date": "testc"
            }
        """.trimIndent()

        // Effectuer la requête POST
        *//*val response = *//*
        io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .response()
        io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .post("/api/feedback")
                .then()
                .statusCode(201)
                .extract()
                .response()

        // Vérifier la réponse ou d'autres assertions selon votre logique métier
        // val responseBody = response.body().asString()

        val feedbackEntity = given()
                .get("/api/feedback/ad/1")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", Feedback::class.java)

        assertNotNull(feedbackEntity)
        assertEquals("test", feedbackEntity[feedbackEntity.size-1].userId)
        assertEquals(4, feedbackEntity[feedbackEntity.size-1].rating)
        assertEquals("testl", feedbackEntity[feedbackEntity.size-1].description)
        assertEquals("testc", feedbackEntity[feedbackEntity.size-1].date)
    }*/
}
