package org.rncp

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.rncp.feedback.domain.model.Feedback
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.*
import jakarta.transaction.Transactional
import org.junit.jupiter.api.*
import org.rncp.ad.domain.model.Ad
import org.rncp.ad.infra.api.AdDto

@QuarkusTest
@TestMethodOrder(OrderAnnotation::class)
class AdTest {

    fun clear() {
        val AdsEntity = given()
                .get("/api/ads")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)
        AdsEntity.map { AdEntity ->
            given()
                    .delete("/api/ads/${AdEntity.id}")
                    .then()
                    .statusCode(204)
        }

    }
    @Test
    @Order(1)
    fun testCreateAndGetById() {
        val requestAd = """{
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


        given()
                .delete("/api/ads/1")
                .then()
                .statusCode(204)


        val ad = AdDto(1, "Testeur", "Gauthier Ad", "Description de test", 56.3f, "-0.256245656", "30.29562656", true, "")
        assertEquals(ad, AdEntity)

    }

    @Test
    @Order(2)
    fun testDelete(){

        clear()
        val requestAd = """{
        "userId": "Testeur2",
        "name": "Gauthier Ad2",
        "description": "Description de test2",
        "hourPrice": 56.3,
        "latitude": "-0.256245656",
        "longitude": "30.29562656",
        "state": true,
        "link": "y en a pas"
    }""".trimIndent()

        // TODO : Récupérer mon Id ici
        io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .response()

        given()
                .delete("/api/ads/1")
                .then()
                .statusCode(204)

        val AdsEntity = given()
                .get("/api/ads")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(0, AdsEntity.size)

    }

    @Test
    @Order(3)
    fun testGetAll(){
        /*val AdsEntity = given()
                .get("/api/ads")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(0, AdsEntity.size)*/
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
