package org.rncp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.rncp.ad.infra.api.AdDto
import org.rncp.feedback.infra.api.FeedbackDTO
import java.time.LocalDateTime
import java.time.Month

@QuarkusTest
class FeedbackTest {
    private fun createAd(requestAd: AdDto): AdDto {
        return RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(requestAd))
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .`as`(AdDto::class.java)
    }

    private fun createFeedback(requestFeedback: FeedbackDTO): FeedbackDTO {
        return RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(requestFeedback))
                .post("/api/feedback")
                .then()
                .statusCode(201)
                .extract()
                .`as`(FeedbackDTO::class.java)
    }

    private fun getFeedbackByAdId(adId: Int?): List<FeedbackDTO> {
        return RestAssured.given().get("/api/feedback/ad/$adId")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", FeedbackDTO::class.java)
    }

    private fun getFeedbackById(feedbackId: Int?): FeedbackDTO {
        return RestAssured.given().get("/api/feedback/$feedbackId")
                .then()
                .statusCode(200)
                .extract()
                .`as`(FeedbackDTO::class.java)
    }

    @Test
    fun testCreateAndGetById() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        val feedbackGiven = createFeedback(requestFeedback)

        val feedbackEntity = getFeedbackById(feedbackGiven.id)

        val expectedFeedback = FeedbackDTO(feedbackGiven.id, adGiven.id!!, adGiven.userId, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))

        Assertions.assertEquals(expectedFeedback, feedbackEntity)
    }

    @Test
    fun testDelete() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        val feedbackGiven = createFeedback(requestFeedback)

        RestAssured.given().delete("/api/feedback/${feedbackGiven.id}")
                .then()
                .statusCode(204)

        val feedbacks = getFeedbackByAdId(feedbackGiven.adId)

        Assertions.assertEquals(0, feedbacks.size)
    }

    @Test
    fun testUpdate() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        val requestFeedbackUpdate = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 2, "Pas ouf", LocalDateTime.of(2023, Month.SEPTEMBER, 21, 11, 38, 43))
        val feedbackGiven = createFeedback(requestFeedback)

        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(requestFeedbackUpdate))
                .put("/api/feedback/${feedbackGiven.id}")
                .then()
                .statusCode(200)

        val feedbackUpdate = getFeedbackById(feedbackGiven.id)
        val expectedFeedback = FeedbackDTO(feedbackGiven.id, adGiven.id!!, adGiven.userId, 2, "Pas ouf", LocalDateTime.of(2023, Month.SEPTEMBER, 21, 11, 38, 43))

        Assertions.assertEquals(expectedFeedback, feedbackUpdate)
    }

    @Test
    fun testGetFeedbackDoesNotExist() {
        RestAssured.given().get("/api/feedback/0")
                .then()
                .statusCode(404)
    }

    @Test
    fun testCreateWithBadValues() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestRating1 = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 0, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestRating1))
                .post("/api/feedback")
                .then()
                .statusCode(400)

        val badRequestRating2 = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 7, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestRating1))
                .post("/api/feedback")
                .then()
                .statusCode(400)

        val badRequestAdId = FeedbackDTO(null, 0, adGiven.userId, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestRating1))
                .post("/api/feedback")
                .then()
                .statusCode(400)

    }
    @Test
    fun testUpdateWithBadValues() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        val feedbackGiven = createFeedback(requestFeedback)

        val badRequestRating1 = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 0, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestRating1))
                .put("/api/feedback/${feedbackGiven.id}")
                .then()
                .statusCode(400)

        val badRequestRating2 = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 7, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestRating1))
                .put("/api/feedback/${feedbackGiven.id}")
                .then()
                .statusCode(400)

    }
}