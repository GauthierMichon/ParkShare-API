package org.rncp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rncp.ad.infra.api.AdDto
import org.rncp.feedback.infra.api.FeedbackDTO
import org.rncp.reservation.infra.api.ReservationDTO
import org.rncp.reservation.infra.db.ReservationPostGreRepository
import java.time.LocalDateTime
import java.time.Month
import kotlin.random.Random

@QuarkusTest
class ReservationTest {

    @Inject
    lateinit var reservationPostGreRepository: ReservationPostGreRepository

    @Transactional
    fun clearReservations() {
        reservationPostGreRepository.deleteAll()
    }

    private fun createAd(requestAd: AdDto): AdDto {
        return RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(requestAd))
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .`as`(AdDto::class.java)
    }

    private fun createReservation(requestReservation: ReservationDTO): ReservationDTO {
        return RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(requestReservation))
                .post("/api/reservation")
                .then()
                .statusCode(201)
                .extract()
                .`as`(ReservationDTO::class.java)
    }

    private fun getReservationByAdId(adId: Int?): List<ReservationDTO> {
        return RestAssured.given().get("/api/reservation/ad/$adId")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)
    }

    private fun getReservationById(reservationId: Int?): ReservationDTO {
        return RestAssured.given().get("/api/reservation/$reservationId")
                .then()
                .statusCode(200)
                .extract()
                .`as`(ReservationDTO::class.java)
    }

    @Test
    fun testCreateAndGetById() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val reservationEntity = getReservationById(reservationGiven.id)

        val expectedReservation = ReservationDTO(reservationGiven.id, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)

        Assertions.assertEquals(expectedReservation, reservationEntity)
    }

    @Test
    fun testGetByStatus() {
        clearReservations()
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
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

            val requestReservation = ReservationDTO(null, adIdsList[randomAdIdIndex], adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), randomStatus)
            createReservation(requestReservation)
        }

        val reservationsStatus1 = RestAssured.given().get("/api/reservation/status/1")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        val reservationsStatus2 = RestAssured.given().get("/api/reservation/status/2")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        val reservationsStatus3 = RestAssured.given().get("/api/reservation/status/3")
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
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        RestAssured.given().delete("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(204)

    val reservations = getReservationByAdId(reservationGiven.adId)

        Assertions.assertEquals(0, reservations.size)
    }

    @Test
    fun testCancel() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        RestAssured.given()
                .contentType(ContentType.JSON)
                .post("/api/reservation/cancel/${reservationGiven.id}")
                .then()
                .statusCode(200)

        val reservationEntity = getReservationById(reservationGiven.id)

        val expectedReservation = ReservationDTO(reservationGiven.id, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 3)

        Assertions.assertEquals(expectedReservation, reservationEntity)
    }

    @Test
    fun testUpdate() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val requestReservationUpdate = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 21, 19, 42, 13), 2)
        val reservationGiven = createReservation(requestReservation)

        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(requestReservationUpdate))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(204)

        val reservationUpdate = getReservationById(reservationGiven.id)
        val expectedReservation = ReservationDTO(reservationGiven.id, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 21, 19, 42, 13), 2)

        Assertions.assertEquals(expectedReservation, reservationUpdate)
    }

    @Test
    fun testGetReservationDoesNotExist() {
        RestAssured.given().get("/api/reservation/0")
                .then()
                .statusCode(404)
    }

    @Test
    fun testCancelReservationDoesNotExist() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .post("/api/reservation/cancel/0")
                .then()
                .statusCode(404)
    }

    @Test
    fun testCreateWithEndDateBeforeBeginDate() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val badRequestEndDateBeforeBeginDate = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestEndDateBeforeBeginDate))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithBeginDatePassed() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val badRequestBeginDatePassed = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestBeginDatePassed))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithEndDatePassed() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val badRequestEndDatePassed = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestEndDatePassed))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidStatusId1() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val badRequestStatusId1 = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 0)
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestStatusId1))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidStatusId2() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val badRequestStatusId2 = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 6)
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestStatusId2))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidAdId() {
        val badRequestAdId = ReservationDTO(null, 0, "", LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAdId))
                .post("/api/reservation")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithEndDateBeforeBeginDate() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val badRequestEndDateBeforeBeginDate = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestEndDateBeforeBeginDate))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithBeginDatePassed() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val badRequestBeginDatePassed = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestBeginDatePassed))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithEndDatePassed() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val badRequestEndDatePassed = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 18, 19, 42, 13), 1)
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestEndDatePassed))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidStatusId1() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val badRequestStatusId1 = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 0)
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestStatusId1))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidStatusId2() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val badRequestStatusId2 = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2024, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2024, Month.SEPTEMBER, 20, 19, 42, 13), 6)
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestStatusId2))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(400)
    }
}