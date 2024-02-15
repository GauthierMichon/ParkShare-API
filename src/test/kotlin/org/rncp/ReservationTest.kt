package org.rncp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.rncp.ad.infra.api.AdCreateOrUpdateDTO
import org.rncp.ad.infra.api.AdDto
import org.rncp.feedback.infra.api.FeedbackDTO
import org.rncp.reservation.infra.api.ReservationCreateOrUpdateDTO
import org.rncp.reservation.infra.api.ReservationDTO
import org.rncp.user.infra.api.LoginDTO
import java.time.LocalDateTime
import java.time.Month
import kotlin.random.Random

@QuarkusTest
class ReservationTest {

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

    private fun clearReservations() {
        val reservationsStatus1 = RestAssured.given().auth().oauth2(tokenJWT).get("/api/reservation/status/1")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        reservationsStatus1.forEach { ad ->
            RestAssured.given().auth().oauth2(tokenJWT).delete("/api/reservation/${ad.id}")
                    .then()
                    .statusCode(204)
        }

        val reservationsStatus2 = RestAssured.given().auth().oauth2(tokenJWT).get("/api/reservation/status/2")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        reservationsStatus2.forEach { ad ->
            RestAssured.given().auth().oauth2(tokenJWT).delete("/api/reservation/${ad.id}")
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

    private fun createAd(requestAd: AdCreateOrUpdateDTO): AdDto {
        return RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
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

    private fun getReservationByAdId(adId: Int?): List<ReservationDTO> {
        return RestAssured.given().auth().oauth2(tokenJWT).get("/api/reservation/ad/$adId")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)
    }

    private fun getReservationById(reservationId: Int?): ReservationDTO {
        return RestAssured.given().auth().oauth2(tokenJWT).get("/api/reservation/$reservationId")
                .then()
                .statusCode(200)
                .extract()
                .`as`(ReservationDTO::class.java)
    }

    @Test
    fun testCreateAndGetById() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val reservationEntity = getReservationById(reservationGiven.id)

        val expectedReservation = ReservationDTO(reservationGiven.id, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)

        Assertions.assertEquals(expectedReservation, reservationEntity)
    }

    @Test
    fun testGetByStatus() {
        clearReservations()
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)
        val adGiven2 = createAd(requestAd)
        val adGiven3 = createAd(requestAd)

        val adIdsList = mutableListOf<Int>(adGiven.id!!, adGiven2.id!!, adGiven3.id!!)
        var countStatus1 = 0
        var countStatus2 = 0
        var countStatus3 = 0
        for (i in 0..20) {
            val randomAdIdIndex = Random.nextInt(0, 3)
            val randomStatus = Random.nextInt(1, 4)
            when (randomStatus) {
                1 -> countStatus1++
                2 -> countStatus2++
                3 -> countStatus3++
            }

            val requestReservation = ReservationCreateOrUpdateDTO(adIdsList[randomAdIdIndex], LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), randomStatus)
            createReservation(requestReservation)
        }

        val reservationsStatus1 = RestAssured.given().auth().oauth2(tokenJWT).get("/api/reservation/status/1")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        val reservationsStatus2 = RestAssured.given().auth().oauth2(tokenJWT).get("/api/reservation/status/2")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        val reservationsStatus3 = RestAssured.given().auth().oauth2(tokenJWT).get("/api/reservation/status/3")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        Assertions.assertEquals(countStatus1, reservationsStatus1.size)
        Assertions.assertEquals(countStatus2, reservationsStatus2.size)
        Assertions.assertEquals(countStatus3, reservationsStatus3.size)
    }

    @Test
    fun testDelete() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        RestAssured.given().auth().oauth2(tokenJWT).delete("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(204)

    val reservations = getReservationByAdId(reservationGiven.adId)

        Assertions.assertEquals(0, reservations.size)
    }

    @Test
    fun testCancel() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        RestAssured.given()
                .auth().oauth2(tokenJWT)
                .contentType(ContentType.JSON)
                .post("/api/reservation/cancel/${reservationGiven.id}")
                .then()
                .statusCode(200)
                .extract()
                .`as`(ReservationDTO::class.java)

        val reservationEntity = getReservationById(reservationGiven.id)

        val expectedReservation = ReservationDTO(reservationGiven.id, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 3)

        Assertions.assertEquals(expectedReservation, reservationEntity)
    }

    @Test
    fun testUpdate() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val requestReservationUpdate = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 21, 19, 42, 13), 2)
        val reservationGiven = createReservation(requestReservation)

        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(requestReservationUpdate))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(200)

        val reservationUpdate = getReservationById(reservationGiven.id)
        val expectedReservation = ReservationDTO(reservationGiven.id, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 21, 19, 42, 13), 2)

        Assertions.assertEquals(expectedReservation, reservationUpdate)
    }

    @Test
    fun testGetReservationDoesNotExist() {
        RestAssured.given().auth().oauth2(tokenJWT).get("/api/reservation/0")
                .then()
                .statusCode(404)
    }

    @Test
    fun testCancelReservationDoesNotExist() {
        RestAssured.given()
                .auth().oauth2(tokenJWT)
                .contentType(ContentType.JSON)
                .post("/api/reservation/cancel/0")
                .then()
                .statusCode(404)
    }

    @Test
    fun testCreateWithEndDateBeforeBeginDate() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestEndDateBeforeBeginDate = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestEndDateBeforeBeginDate))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithBeginDatePassed() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestBeginDatePassed = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestBeginDatePassed))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithEndDatePassed() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestEndDatePassed = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestEndDatePassed))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidStatusId1() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestStatusId1 = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 0)
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestStatusId1))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidStatusId2() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestStatusId2 = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 6)
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestStatusId2))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidAdId() {
        val badRequestAdId = ReservationCreateOrUpdateDTO(0, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAdId))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithEndDateBeforeBeginDate() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val badRequestEndDateBeforeBeginDate = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestEndDateBeforeBeginDate))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithBeginDatePassed() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val badRequestBeginDatePassed = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestBeginDatePassed))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithEndDatePassed() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val badRequestEndDatePassed = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestEndDatePassed))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidStatusId1() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val badRequestStatusId1 = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 0)
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestStatusId1))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidStatusId2() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val badRequestStatusId2 = ReservationCreateOrUpdateDTO(adGiven.id!!, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 6)
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestStatusId2))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(400)
    }
}