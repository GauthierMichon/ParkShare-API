package org.rncp

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.rncp.ad.infra.api.AdCreateOrUpdateDTO
import org.rncp.ad.infra.api.AdDto
import org.rncp.user.infra.api.LoginDTO
import org.rncp.image.infra.api.ImageCreateDTO
import org.rncp.image.infra.api.ImageDTO

@QuarkusTest
class ImageTest {
    companion object {
        @JvmStatic
        private var tokenJWT: String? = null

        @BeforeAll
        @JvmStatic
        fun setUp() {
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

    private fun createImage(requestImage: ImageCreateDTO): ImageDTO {
        return RestAssured.given().auth().oauth2(tokenJWT).contentType(ContentType.JSON)
                .body(Json.encodeToString(requestImage))
                .post("/api/image")
                .then()
                .statusCode(201)
                .extract()
                .`as`(ImageDTO::class.java)
    }

    private fun getImageByAdId(adId: Int?): List<ImageDTO> {
        return RestAssured.given().auth().oauth2(tokenJWT).get("/api/image/ad/$adId")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", ImageDTO::class.java)
    }

    private fun getImageById(imageId: Int?): ImageDTO {
        return RestAssured.given().auth().oauth2(tokenJWT).get("/api/image/$imageId")
                .then()
                .statusCode(200)
                .extract()
                .`as`(ImageDTO::class.java)
    }

    @Test
    fun testCreateAndGetById() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestImage = ImageCreateDTO(adGiven.id!!, byteArrayOf(12, 35, 67))
        val imageGiven = createImage(requestImage)

        val imageEntity = getImageById(imageGiven.id)

        val expectedImage = ImageDTO(imageGiven.id, adGiven.id!!, byteArrayOf(12, 35, 67))

        Assertions.assertEquals(expectedImage, imageEntity)
    }

    @Test
    fun testGetByAdId() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestImage = ImageCreateDTO(adGiven.id!!, byteArrayOf(12, 35, 67))
        createImage(requestImage)
        createImage(requestImage)
        createImage(requestImage)

        val images = getImageByAdId(adGiven.id)

        Assertions.assertEquals(3, images.size)
    }

    @Test
    fun testDelete() {
        val requestAd = AdCreateOrUpdateDTO("Gauthier Ad", "Description de test", 56.3, -0.2562456, 30.295626, true, "")
        val adGiven = createAd(requestAd)

        val requestImage = ImageCreateDTO(adGiven.id!!, byteArrayOf(12, 35, 67))
        val imageGiven = createImage(requestImage)

        RestAssured.given().auth().oauth2(tokenJWT).delete("/api/image/${imageGiven.id}")
                .then()
                .statusCode(204)

        val images = getImageByAdId(imageGiven.adId)

        Assertions.assertEquals(0, images.size)
    }

    @Test
    fun testGetImageDoesNotExist() {
        RestAssured.given().auth().oauth2(tokenJWT).get("/api/image/0")
                .then()
                .statusCode(404)
    }
}