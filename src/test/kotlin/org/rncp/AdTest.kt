package org.rncp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.rncp.ad.infra.api.AdDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@QuarkusTest
class AdTest {

    private fun clearAds() {
        val ads = given().get("/api/ads")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        ads.forEach { ad ->
            given().delete("/api/ads/${ad.id}")
                    .then()
                    .statusCode(204)
        }
    }

    private fun createAd(requestAd: AdDto): AdDto {
        return given().contentType(ContentType.JSON)
                .body(Json.encodeToString(requestAd))
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .`as`(AdDto::class.java)
    }

    private fun getAdById(adId: Int?): AdDto {
        return given().get("/api/ads/$adId")
                .then()
                .statusCode(200)
                .extract()
                .`as`(AdDto::class.java)
    }

    private fun deleteAd(adId: Int?) {
        given().delete("/api/ads/$adId")
                .then()
                .statusCode(204)
    }

    @Test
    fun testCreateAndGetById() {
        clearAds()
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")

        val adGiven = createAd(requestAd)
        val adEntity = getAdById(adGiven.id)

        val expectedAd = AdDto(adGiven.id, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")

        assertEquals(expectedAd, adEntity)
    }

    @Test
    fun testDelete() {
        clearAds()

        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")

        val adGiven = createAd(requestAd)
        deleteAd(adGiven.id)

        val adsEntity = given().get("/api/ads")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(0, adsEntity.size)
    }

    @Test
    fun testGetAll() {
        clearAds()

        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")


        createAd(requestAd)
        createAd(requestAd)

        val adsEntity = given().get("/api/ads")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(2, adsEntity.size)
    }

    @Test
    fun testPublish() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, false, "")

        val adGiven = createAd(requestAd)

        given().contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads/${adGiven.id}/publish")
                .then()
                .statusCode(200)

        val adPublish = getAdById(adGiven.id)

        assertEquals(adGiven.id, adPublish.id)
        assertEquals(adGiven.userId, adPublish.userId)
        assertEquals(adGiven.name, adPublish.name)
        assertEquals(adGiven.description, adPublish.description)
        assertEquals(adGiven.hourPrice, adPublish.hourPrice)
        assertEquals(adGiven.latitude, adPublish.latitude)
        assertEquals(adGiven.longitude, adPublish.longitude)
        assertNotEquals(adGiven.state, adPublish.state)
    }

    @Test
    fun testUnpublish() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")


        val adGiven = createAd(requestAd)

        given().contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads/${adGiven.id}/unpublish")
                .then()
                .statusCode(200)

        val adUnpublish = getAdById(adGiven.id)

        assertEquals(adGiven.id, adUnpublish.id)
        assertEquals(adGiven.userId, adUnpublish.userId)
        assertEquals(adGiven.name, adUnpublish.name)
        assertEquals(adGiven.description, adUnpublish.description)
        assertEquals(adGiven.hourPrice, adUnpublish.hourPrice)
        assertEquals(adGiven.latitude, adUnpublish.latitude)
        assertEquals(adGiven.longitude, adUnpublish.longitude)
        assertNotEquals(adGiven.state, adUnpublish.state)

    }

    @Test
    fun testUpdate() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val requestAdUpdate = AdDto(null, "Testeur", "Gauthier Ad Update", "Description de test Update", 25.8f, -0.2569191f, 30.281220f, false, "")

        val adGiven = createAd(requestAd)

        given().contentType(ContentType.JSON)
                .body(Json.encodeToString(requestAdUpdate))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(200)

        val adUpdate = getAdById(adGiven.id)

        val expectedAd = AdDto(adGiven.id, "Testeur", "Gauthier Ad Update", "Description de test Update", 25.8f, -0.2569191f, 30.281220f, false, "")

        assertEquals(expectedAd, adUpdate)
    }

    @Test
    fun testGetAdDoesNotExist() {
        given().get("/api/ads/0")
                .then()
                .statusCode(404)
    }

    @Test
    fun testCreateWithBadValues() {
        val badRequestHourPrice = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", -56.3f, -0.2562456f, 30.295626f, true, "")
        given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestHourPrice))
                .post("/api/ads")
                .then()
                .statusCode(400)

        val badRequestLatitude = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -100.25624f, 30.295626f, true, "")
        given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLatitude))
                .post("/api/ads")
                .then()
                .statusCode(400)

        val badRequestLongitude = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 300.9562f, true, "")
        given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLongitude))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithBadValues() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestHourPrice = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", -56.3f, -0.2562456f, 30.295626f, true, "")
        given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestHourPrice))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(400)

        val badRequestLatitude = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -100.25624f, 30.295626f, true, "")
        given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLatitude))
                .post("/api/ads")
                .then()
                .statusCode(400)

        val badRequestLongitude = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 300.9562f, true, "")
        given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLongitude))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }



}
