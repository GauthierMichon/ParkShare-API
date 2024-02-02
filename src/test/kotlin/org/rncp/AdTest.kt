package org.rncp

import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.*
import org.rncp.ad.infra.api.AdDto

@QuarkusTest
@TestMethodOrder(OrderAnnotation::class)
class AdTest {

    fun clear() {
        val adsEntity = given()
                .get("/api/ads")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)
        adsEntity.map { adEntity ->
            given()
                    .delete("/api/ads/${adEntity.id}")
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

        val adGiven = io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .`as`(AdDto::class.java)

        val adEntity = given()
                .get("/api/ads/${adGiven.id}")
                .then()
                .statusCode(200)
                .extract()
                .`as`(AdDto::class.java)


        given()
                .delete("/api/ads/${adGiven.id}")
                .then()
                .statusCode(204)


        val ad = AdDto(adGiven.id, "Testeur", "Gauthier Ad", "Description de test", 56.3f, "-0.256245656", "30.29562656", true, "")
        assertEquals(ad, adEntity)

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

        val adGiven = io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .`as`(AdDto::class.java)

        given()
                .delete("/api/ads/${adGiven.id}")
                .then()
                .statusCode(204)

        val adsEntity = given()
                .get("/api/ads")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(0, adsEntity.size)

    }

    @Test
    @Order(3)
    fun testGetAll(){
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

        io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .`as`(AdDto::class.java)

        io.restassured.RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .`as`(AdDto::class.java)
        val adsEntity = given()
                .get("/api/ads")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(2, adsEntity.size)
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
