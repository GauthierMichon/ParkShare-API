package org.rncp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rncp.ad.infra.api.AdDto
import org.rncp.reservation.infra.api.ReservationDTO
import java.time.LocalDateTime
import java.time.Month
import kotlin.random.Random

@QuarkusTest
class ReservationTest {

    private fun clearReservations() {
        val reservationsStatus1 = RestAssured.given().get("/api/reservation/status/1")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        reservationsStatus1.forEach { ad ->
            RestAssured.given().delete("/api/reservation/${ad.id}")
                    .then()
                    .statusCode(204)
        }

        val reservationsStatus2 = RestAssured.given().get("/api/reservation/status/2")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        reservationsStatus2.forEach { ad ->
            RestAssured.given().delete("/api/reservation/${ad.id}")
                    .then()
                    .statusCode(204)
        }

        val reservationsStatus3 = RestAssured.given().get("/api/reservation/status/3")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ReservationDTO::class.java)

        reservationsStatus3.forEach { ad ->
            RestAssured.given().delete("/api/reservation/${ad.id}")
                    .then()
                    .statusCode(204)
        }
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
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        val reservationEntity = getReservationById(reservationGiven.id)

        val expectedReservation = ReservationDTO(reservationGiven.id, adGiven.id!!, adGiven.userId, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 20, 19, 42, 13), 1)

        Assertions.assertEquals(expectedReservation, reservationEntity)
    }

    @Test
    fun testGetByStatus() {
        clearReservations()
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
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

            val requestReservation = ReservationDTO(null, adIdsList[randomAdIdIndex], adGiven.userId, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 20, 19, 42, 13), randomStatus)
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
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        RestAssured.given().delete("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(204)

    val reservations = getReservationByAdId(reservationGiven.adId)

        Assertions.assertEquals(0, reservations.size)
    }

    @Test
    fun testCancel() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val reservationGiven = createReservation(requestReservation)

        RestAssured.given()
                .contentType(ContentType.JSON)
                .post("/api/reservation/cancel/${reservationGiven.id}")
                .then()
                .statusCode(200)
                .extract()
                .`as`(ReservationDTO::class.java)

        val reservationEntity = getReservationById(reservationGiven.id)

        val expectedReservation = ReservationDTO(reservationGiven.id, adGiven.id!!, adGiven.userId, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 20, 19, 42, 13), 3)

        Assertions.assertEquals(expectedReservation, reservationEntity)
    }

    @Test
    fun testUpdate() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestReservation = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 20, 19, 42, 13), 1)
        val requestReservationUpdate = ReservationDTO(null, adGiven.id!!, adGiven.userId, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 21, 19, 42, 13), 2)
        val reservationGiven = createReservation(requestReservation)

        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(requestReservationUpdate))
                .put("/api/reservation/${reservationGiven.id}")
                .then()
                .statusCode(200)

        val reservationUpdate = getReservationById(reservationGiven.id)
        val expectedReservation = ReservationDTO(reservationGiven.id, adGiven.id!!, adGiven.userId, LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13), LocalDateTime.of(2023, Month.SEPTEMBER, 21, 19, 42, 13), 2)

        Assertions.assertEquals(expectedReservation, reservationUpdate)
    }
}