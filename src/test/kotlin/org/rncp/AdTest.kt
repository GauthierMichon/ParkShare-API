package org.rncp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.rncp.feedback.domain.model.Feedback
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*
import jakarta.transaction.Transactional
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.infra.api.AdDto

@QuarkusTest
class AdTest {

    @Test
    fun testCreateAndGetById() {
        val requestAd = """{
            "id": 1,
            "userId": "Testeur",
            "name": "Gauthier Ad",
            "description": "Description de test",
            "hourPrice": 56.3,
            "latitude": "-0.256245656",
            "longitude": "30.29562656",
            "state": true,
            "link": "y en a pas"
        }""".trimIndent()

        io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .response()

        val AdEntity = given()
                .get("/api/ads/1")
                .then()
                .statusCode(200)
                .extract()
                .`as`(AdDto::class.java)

        /*given()
                .delete("/api/ads/1")
                .then()
                .statusCode(200)*/

        val ad = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, "-0.256245656", "30.29562656", true, "")
        assertEquals(ad, AdEntity)
    }

    @Test
    fun testDelete(){
        val requestAd = """{
            "id": 1,
            "userId": "Testeur",
            "name": "Gauthier Ad",
            "description": "Description de test",
            "hourPrice": 56.3,
            "latitude": "-0.256245656",
            "longitude": "30.29562656",
            "state": true,
            "link": "y en a pas"
        }""".trimIndent()

        io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .response()

        /*given()
                .delete("/api/ads/1")
                .then()
                .statusCode(200)*/
    }

    @Test
    fun testGetAll(){

    }

    @Test
    fun testPublish(){

    }

    @Test
    fun testUnpublish(){

    }

    @Test
    fun testUpdate(){

    }


}
