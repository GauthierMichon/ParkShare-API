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

    private fun getFeedbackById(adId: Int?): List<FeedbackDTO> {
        return RestAssured.given().get("/api/feedback/ad/$adId")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", FeedbackDTO::class.java)
    }

    @Test
    fun testCreateAndGetById() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, "-0.256245656", "30.29562656", true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 4, "Super", "19/09/2023")
        val feedbackGiven = createFeedback(requestFeedback)

        val feedbackEntity = getFeedbackById(feedbackGiven.adId).last

        val expectedFeedback = FeedbackDTO(feedbackGiven.id, adGiven.id!!, adGiven.userId, 4, "Super", "19/09/2023")

        Assertions.assertEquals(expectedFeedback, feedbackEntity)
    }

    @Test
    fun testDelete() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, "-0.256245656", "30.29562656", true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 4, "Super", "19/09/2023")
        val feedbackGiven = createFeedback(requestFeedback)

        RestAssured.given().delete("/api/feedback/${feedbackGiven.id}")
                .then()
                .statusCode(204)

        val feedbacks = getFeedbackById(feedbackGiven.adId)

        Assertions.assertEquals(0, feedbacks.size)
    }

    @Test
    fun testUpdate() {
        val requestAd = AdDto(null, "Testeur", "Gauthier Ad", "Description de test", 56.3f, "-0.256245656", "30.29562656", true, "")
        val adGiven = createAd(requestAd)

        val requestFeedback = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 4, "Super", "19/09/2023")
        val requestFeedbackUpdate = FeedbackDTO(null, adGiven.id!!, adGiven.userId, 2, "Pas ouf", "21/09/2023")
        val feedbackGiven = createFeedback(requestFeedback)

        RestAssured.given().contentType(ContentType.JSON)
                .body(Json.encodeToString(requestFeedbackUpdate))
                .put("/api/feedback/${feedbackGiven.id}")
                .then()
                .statusCode(200)

        val feedbackUpdate = getFeedbackById(feedbackGiven.id).last
        val expectedFeedback = FeedbackDTO(feedbackGiven.id, adGiven.id!!, adGiven.userId, 2, "Pas ouf", "21/09/2023")

        Assertions.assertEquals(expectedFeedback, feedbackUpdate)
    }
}