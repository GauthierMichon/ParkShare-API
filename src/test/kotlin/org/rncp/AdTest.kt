package org.rncp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.rncp.ad.infra.api.AdDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.rncp.ad.infra.api.AdCreateOrUpdateDTO
import org.rncp.feedback.infra.api.FeedbackCreateOrUpdateDTO
import org.rncp.reservation.infra.api.ReservationCreateOrUpdateDTO
import org.rncp.feedback.infra.api.FeedbackDTO
import org.rncp.reservation.infra.api.ReservationDTO
import org.rncp.reservation.infra.db.ReservationPostGreRepository
import org.rncp.user.infra.api.LoginDTO
import java.time.LocalDateTime
import java.time.Month

@QuarkusTest
class AdTest {
    @Inject
    lateinit var reservationPostGreRepository: ReservationPostGreRepository
    private var tokenJWT: String? = null
    @BeforeEach
    fun generateTokenJwt() {
        val response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Json.encodeToString(LoginDTO("hugobast33@gmail.com", "mypassword", true)))
                .post("/api/user/authentication")

        val token = response.then()
                .extract()
                .jsonPath()
                .getString("idToken")

        tokenJWT = token
    }

    @Transactional
    fun clearReservations() {
        reservationPostGreRepository.deleteAll()
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

    private fun createReservation(requestReservation: ReservationCreateOrUpdateDTO): ReservationDTO {
        return RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(requestReservation))
                .post("/api/reservation")
                .then()
                .statusCode(201)
                .extract()
                .`as`(ReservationDTO::class.java)
    }

    private fun createFeedback(requestFeedback: FeedbackCreateOrUpdateDTO): FeedbackDTO {
        return RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(requestFeedback))
                .post("/api/feedback")
                .then()
                .statusCode(201)
                .extract()
                .`as`(FeedbackDTO::class.java)
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
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)
        val adEntity = getAdById(adGiven.id)

        val expectedAd = AdDto(adGiven.id, "8yk8My4TXZZPNkXOhAIki4y1iP83", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")

        assertEquals(expectedAd, adEntity)
    }

    @Test
    fun testDelete() {
        clearAds()

        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")

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

        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")

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
    fun testGetAllWithPublishFilter() {
        clearAds()

        val requestAdPublished = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8666, 2.3722, true, "")
        val requestAdUnpublished = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8466, 2.3322, false, "")

        createAd(requestAdPublished)
        createAd(requestAdUnpublished)
        createAd(requestAdPublished)
        createAd(requestAdPublished)

        val adsEntity = given().auth().oauth2(tokenJWT).get("/api/ads?onlyPublish=true")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(3, adsEntity.size)
    }

    @Test
    fun testGetAllWithLocationFilter() {
        clearAds()

        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8666, 2.3722, true, "")
        val requestAd2 = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8466, 2.3322, true, "")
        val requestAd3 = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8566, 2.4889, true, "")
        val requestAd4 = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8566, 2.4888, true, "")

        createAd(requestAd)
        createAd(requestAd2)
        createAd(requestAd3)
        createAd(requestAd4)

        val adsEntity = given().auth().oauth2(tokenJWT).get("/api/ads?latitude=48.8566&longitude=2.3522&distance=10.0")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(3, adsEntity.size)
    }

    @Test
    fun testGetAllWithDateFilterTrue() {
        clearAds()

        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8666, 2.3722, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 2)
        createReservation(requestReservation)

        val adsEntity = given().auth().oauth2(tokenJWT).get("/api/ads?beginDate=2024-09-19T09:00:00&endDate=2024-09-19T10:00:00")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(1, adsEntity.size)
    }

    @Test
    fun testGetAllWithDateFilterFalse() {
        clearAds()

        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8666, 2.3722, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 2)
        createReservation(requestReservation)

        val adsEntity = given().auth().oauth2(tokenJWT).get("/api/ads?beginDate=2024-09-20T09:00:00&endDate=2024-09-21T10:00:00")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(0, adsEntity.size)
    }

    @Test
    fun testGetAllWithDateTrue2() {
        clearAds()

        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8666, 2.3722, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 3)
        val reservationGiven = createReservation(requestReservation)

        RestAssured.given()
                .auth().oauth2(tokenJWT)
                .contentType(ContentType.JSON)
                .post("/api/reservation/cancel/${reservationGiven.id}")
                .then()
                .statusCode(200)


        val adsEntity = given().auth().oauth2(tokenJWT).get("/api/ads?beginDate=2024-09-20T09:00:00&endDate=2024-09-21T10:00:00")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(1, adsEntity.size)
    }

    @Test
    fun testGetAllWithRatingFilterTrue() {
        clearAds()

        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8666, 2.3722, true, "")
        val adGiven = createAd(requestAd)

        val requestFeedbackRating5 = FeedbackCreateOrUpdateDTO(adGiven.id!!, 5, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        val requestFeedbackRating4 = FeedbackCreateOrUpdateDTO(adGiven.id!!, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))

        createFeedback(requestFeedbackRating5)
        createFeedback(requestFeedbackRating5)
        createFeedback(requestFeedbackRating5)
        createFeedback(requestFeedbackRating4)

        val adsEntity = given().auth().oauth2(tokenJWT).get("/api/ads?minRate=4.7")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(1, adsEntity.size)
    }

    @Test
    fun testGetAllWithRatingFilterFalse() {
        clearAds()

        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8666, 2.3722, true, "")
        val adGiven = createAd(requestAd)

        val requestFeedbackRating5 = FeedbackCreateOrUpdateDTO(adGiven.id!!, 5, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        val requestFeedbackRating4 = FeedbackCreateOrUpdateDTO(adGiven.id!!, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))

        createFeedback(requestFeedbackRating5)
        createFeedback(requestFeedbackRating5)
        createFeedback(requestFeedbackRating5)
        createFeedback(requestFeedbackRating4)

        val adsEntity = given().auth().oauth2(tokenJWT).get("/api/ads?minRate=4.8")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(0, adsEntity.size)
    }

    @Test
    fun testGetAllWithMaxHourPrice() {
        clearAds()

        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 48.8666, 2.3722, true, "")
        val requestAd2 = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 16.3, 48.8666, 2.3722, true, "")
        val requestAd3 = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 6.3, 48.8666, 2.3722, true, "")
        val requestAd4 = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 8.9, 48.8666, 2.3722, true, "")
        val requestAd5 = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 11.3, 48.8666, 2.3722, true, "")
        createAd(requestAd)
        createAd(requestAd2)
        createAd(requestAd3)
        createAd(requestAd4)
        createAd(requestAd5)

        val adsEntity = given().auth().oauth2(tokenJWT).get("/api/ads?maxHourPrice=15.2")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", AdDto::class.java)

        assertEquals(3, adsEntity.size)
    }

    @Test
    fun testPublish() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, false, "")

        val adGiven = createAd(requestAd)

        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads/${adGiven.id}/publish")
                .then()
                .statusCode(204)

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
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")


        val adGiven = createAd(requestAd)

        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads/${adGiven.id}/unpublish")
                .then()
                .statusCode(204)

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
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val requestAdUpdate = AdCreateOrUpdateDTO("Gauthier Ad Update", "Description de test Update", 25.8, -0.2569191, 30.281220, false, "")

        val adGiven = createAd(requestAd)

        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(requestAdUpdate))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(204)

        val adUpdate = getAdById(adGiven.id)

        val expectedAd = AdDto(adGiven.id, "8yk8My4TXZZPNkXOhAIki4y1iP83", "Gauthier Ad Update", "Description de test Update", 25.8, -0.2569191, 30.281220, false, "")

        assertEquals(expectedAd, adUpdate)
    }

    @Test
    fun testDeleteAdWithActiveReservation() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
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
    fun testUpdateAdDoesNotExist() {
        val requestAdUpdate = AdCreateOrUpdateDTO("Gauthier Ad Update", "Description de test Update", 25.8, -0.2569191, 30.281220, false, "")

        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(requestAdUpdate))
                .put("/api/ads/0")
                .then()
                .statusCode(404)
    }

    @Test
    fun testCreateWithNegativeHourPrice() {
        val badRequestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", -56.3, -0.2562456, 30.295626, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAd))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidLatitudeNegative() {
        val badRequestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -100.25624, 30.295626, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAd))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidLatitudePositive() {
        val badRequestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 100.25624, 30.295626, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAd))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidLongitudeNegative() {
        val badRequestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, -300.9562, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAd))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidLongitudePositive() {
        val badRequestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 300.9562, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAd))
                .post("/api/ads")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithNegativeHourPrice() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val badRequestHourPrice = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", -56.3, -0.2562456, 30.295626, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestHourPrice))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidLatitudeNegative() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val badRequestLatitudeNegative = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -100.25624, 30.295626, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLatitudeNegative))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidLatitudePositive() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val badRequestLatitudePositive = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, 100.25624, 30.295626, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLatitudePositive))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidLongitudeNegative() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val badRequestLongitudeNegative = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, -300.9562, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLongitudeNegative))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidLongitudePositive() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val badRequestLongitudePositive = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 300.9562, true, "")
        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestLongitudePositive))
                .put("/api/ads/${adGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testPublishAdDoesNotExist() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, false, "")

        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads/0/publish")
                .then()
                .statusCode(404)
    }

    @Test
    fun testUnpublishAdDoesNotExist() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, false, "")

        given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(requestAd)
                .post("/api/ads/0/unpublish")
                .then()
                .statusCode(404)
    }



}
