package org.rncp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.rncp.ad.infra.api.AdDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.rncp.ad.infra.api.AdCreateOrUpdateDTO
import org.rncp.reservation.infra.api.ReservationCreateOrUpdateDTO
import org.rncp.reservation.infra.api.ReservationDTO
import org.rncp.user.infra.api.LoginDTO
import java.time.LocalDateTime
import java.time.Month

@QuarkusTest
class AdTest {
    private var tokenJWT: String? = null

    @BeforeEach
    fun generateTokenJwt() {
        val response = given()
                .contentType(ContentType.JSON)
                .body(Json.encodeToString(LoginDTO("hugobast33@gmail.com", "mypassword", true)))
                .post("/api/user/authentication")

        val token = response.then()
                .extract()
                .jsonPath()
                .getString("idToken")

        tokenJWT = token
    }

    private fun clearReservations() {
        val reservationsStatus1 = given().auth().oauth2(tokenJWT).get("/api/reservation/status/1")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        reservationsStatus1.forEach { ad ->
            given().auth().oauth2(tokenJWT).delete("/api/reservation/${ad.id}")
                    .then()
                    .statusCode(204)
        }

        val reservationsStatus2 = given().auth().oauth2(tokenJWT).get("/api/reservation/status/2")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        reservationsStatus2.forEach { ad ->
            given().auth().oauth2(tokenJWT).delete("/api/reservation/${ad.id}")
                    .then()
                    .statusCode(204)
        }

        val reservationsStatus3 = RestAssured.given().auth().oauth2(tokenJWT).get("/api/reservation/status/3")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        reservationsStatus3.forEach { ad ->
            RestAssured.given().auth().oauth2(tokenJWT).delete("/api/reservation/${ad.id}")
                    .then()
                    .statusCode(204)
        }
    }

    private fun clearAds() {
        clearReservations()
        val ads = given().auth().oauth2(tokenJWT).get("/api/ads")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        ads.forEach { ad ->
            given().auth().oauth2(tokenJWT).delete("/api/ads/${ad.id}")
                    .then()
                    .statusCode(204)
        }
    }

    private fun createAd(requestAd: AdCreateOrUpdateDTO): AdDto {
        return given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(requestAd))
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .`as`(AdDto::class.java)
    }

    private fun getAdById(adId: Int?): AdDto {
        return given().auth().oauth2(tokenJWT).get("/api/ads/$adId")
                .then()
                .statusCode(200)
                .extract()
                .`as`(AdDto::class.java)
    }

    private fun deleteAd(adId: Int?) {
        given().auth().oauth2(tokenJWT).delete("/api/ads/$adId")
                .then()
                .statusCode(204)
    }

    @Test
    fun testCreateAndGetById() {
        clearAds()
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)
        val adEntity = getAdById(adGiven.id)

        val expectedAd = AdDto(adGiven.id, "xau0If5kFubYSQBOG5y6I6vpMK73", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")

        assertEquals(expectedAd, adEntity)
    }

    @Test
    fun testDelete() {
        clearAds()

        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")

        val adGiven = createAd(requestAd)
        deleteAd(adGiven.id)

        val adsEntity = given().auth().oauth2(tokenJWT).get("/api/ads")
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

        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")


        createAd(requestAd)
        createAd(requestAd)

        val adsEntity = given().auth().oauth2(tokenJWT).get("/api/ads")
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
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, false, "")

        val adGiven = createAd(requestAd)

        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
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
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")


        val adGiven = createAd(requestAd)

        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
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
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val requestAdUpdate = AdCreateOrUpdateDTO("Gauthier Ad Update", "Description de test Update", 25.8f, -0.2569191f, 30.281220f, false, "")

        val adGiven = createAd(requestAd)

        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(requestAdUpdate))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(200)

        val adUpdate = getAdById(adGiven.id)

        val expectedAd = AdDto(adGiven.id, "xau0If5kFubYSQBOG5y6I6vpMK73", "Gauthier Ad Update", "Description de test Update", 25.8f, -0.2569191f, 30.281220f, false, "")

        assertEquals(expectedAd, adUpdate)
    }

    @Test
    fun testDeleteAdWithActiveReservation() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(requestReservation))
                .post("/api/reservation")
                .then()
                .statusCode(201)
                .extract()
                .`as`(ReservationDTO::class.java)

        given().auth().oauth2(tokenJWT).delete("/api/ads/${adGiven.id!!}")
                .then()
                .statusCode(409)
    }

    @Test
    fun testGetAdDoesNotExist() {
        given().auth().oauth2(tokenJWT).get("/api/ads/0")
                .then()
                .statusCode(404)
    }

    @Test
    fun testCreateWithNegativeHourPrice() {
        val badRequestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", -56.3f, -0.2562456f, 30.295626f, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAd))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidLatitudeNegative() {
        val badRequestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -100.25624f, 30.295626f, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAd))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidLatitudePositive() {
        val badRequestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, 100.25624f, 30.295626f, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAd))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidLongitudeNegative() {
        val badRequestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, -300.9562f, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAd))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidLongitudePositive() {
        val badRequestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 300.9562f, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAd))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithNegativeHourPrice() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestHourPrice = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", -56.3f, -0.2562456f, 30.295626f, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestHourPrice))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidLatitudeNegative() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestLatitudeNegative = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -100.25624f, 30.295626f, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLatitudeNegative))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidLatitudePositive() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestLatitudePositive = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, 100.25624f, 30.295626f, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLatitudePositive))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidLongitudeNegative() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestLongitudeNegative = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, -300.9562f, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLongitudeNegative))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidLongitudePositive() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestLongitudePositive = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 300.9562f, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLongitudePositive))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(400)
    }


}
