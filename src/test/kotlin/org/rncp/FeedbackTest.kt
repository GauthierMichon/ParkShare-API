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
import org.rncp.feedback.infra.api.FeedbackCreateOrUpdateDTO
import org.rncp.feedback.infra.api.FeedbackDTO
import org.rncp.user.infra.api.LoginDTO
import java.time.LocalDateTime
import java.time.Month

@QuarkusTest
class FeedbackTest {
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

    private fun createAd(requestAd: AdCreateOrUpdateDTO): AdDto {
        return RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(requestAd))
                .post("/api/ads")
                .then()
                .statusCode(201)
                .extract()
                .`as`(AdDto::class.java)
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

    private fun getFeedbackByAdId(adId: Int?): List<FeedbackDTO> {
        return RestAssured.given().auth().oauth2(tokenJWT).get("/api/feedback/ad/$adId")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", FeedbackDTO::class.java)
    }

    private fun getFeedbackById(feedbackId: Int?): FeedbackDTO {
        return RestAssured.given().auth().oauth2(tokenJWT).get("/api/feedback/$feedbackId")
                .then()
                .statusCode(200)
                .extract()
                .`as`(FeedbackDTO::class.java)
    }

    @Test
    fun testCreateAndGetById() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackCreateOrUpdateDTO(adGiven.id!!, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        val feedbackGiven = createFeedback(requestFeedback)

        val feedbackEntity = getFeedbackById(feedbackGiven.id)

        val expectedFeedback = FeedbackDTO(feedbackGiven.id, adGiven.id!!, adGiven.userId, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))

        Assertions.assertEquals(expectedFeedback, feedbackEntity)
    }

    @Test
    fun testDelete() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackCreateOrUpdateDTO(adGiven.id!!, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        val feedbackGiven = createFeedback(requestFeedback)

        RestAssured.given().auth().oauth2(tokenJWT).delete("/api/feedback/${feedbackGiven.id}")
                .then()
                .statusCode(204)

        val feedbacks = getFeedbackByAdId(feedbackGiven.adId)

        Assertions.assertEquals(0, feedbacks.size)
    }

    @Test
    fun testUpdate() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackCreateOrUpdateDTO(adGiven.id!!, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        val requestFeedbackUpdate = FeedbackCreateOrUpdateDTO(adGiven.id!!, 2, "Pas ouf", LocalDateTime.of(2023, Month.SEPTEMBER, 21, 11, 38, 43))
        val feedbackGiven = createFeedback(requestFeedback)

        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(requestFeedbackUpdate))
                .put("/api/feedback/${feedbackGiven.id}")
                .then()
                .statusCode(204)

        val feedbackUpdate = getFeedbackById(feedbackGiven.id)
        val expectedFeedback = FeedbackDTO(feedbackGiven.id, adGiven.id!!, adGiven.userId, 2, "Pas ouf", LocalDateTime.of(2023, Month.SEPTEMBER, 21, 11, 38, 43))

        Assertions.assertEquals(expectedFeedback, feedbackUpdate)
    }

    @Test
    fun testGetFeedbackDoesNotExist() {
        RestAssured.given().auth().oauth2(tokenJWT).get("/api/feedback/0")
                .then()
                .statusCode(404)
    }

    @Test
    fun testCreateWithInvalidRating1() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestRating1 = FeedbackCreateOrUpdateDTO(adGiven.id!!, 0, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestRating1))
                .post("/api/feedback")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidRating2() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val badRequestRating2 = FeedbackCreateOrUpdateDTO(adGiven.id!!, 7, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestRating2))
                .post("/api/feedback")
                .then()
                .statusCode(400)
    }

    @Test
    fun testCreateWithInvalidAdId() {
        val badRequestAdId = FeedbackCreateOrUpdateDTO(0, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestAdId))
                .post("/api/feedback")
                .then()
                .statusCode(400)
    }
    @Test
    fun testUpdateWithInvalidRating1() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackCreateOrUpdateDTO(adGiven.id!!, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        val feedbackGiven = createFeedback(requestFeedback)

        val badRequestRating1 = FeedbackCreateOrUpdateDTO(adGiven.id!!, 0, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestRating1))
                .put("/api/feedback/${feedbackGiven.id}")
                .then()
                .statusCode(400)
    }

    @Test
    fun testUpdateWithInvalidRating2() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3f, -0.2562456f, 30.295626f, true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackCreateOrUpdateDTO(adGiven.id!!, 4, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        val feedbackGiven = createFeedback(requestFeedback)

        val badRequestRating2 = FeedbackCreateOrUpdateDTO(adGiven.id!!, 7, "Super", LocalDateTime.of(2023, Month.SEPTEMBER, 19, 19, 42, 13))
        RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(badRequestRating2))
                .put("/api/feedback/${feedbackGiven.id}")
                .then()
                .statusCode(400)
    }
}