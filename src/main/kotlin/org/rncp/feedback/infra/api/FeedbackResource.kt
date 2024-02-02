package org.rncp.feedback.infra.api

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.rncp.ad.domain.ports.`in`.GetAdByIdUseCase
import org.rncp.ad.infra.api.AdDto
import org.rncp.feedback.domain.model.Feedback
import org.rncp.feedback.domain.ports.`in`.*
import org.rncp.reservation.infra.api.ReservationDTO


@Path("/api/feedback")
class FeedbackResource {
    @Inject
    lateinit var createUseCase: CreateUseCase

    @Inject
    lateinit var getListByAdUseCase: GetListByAdUseCase

    @Inject
    lateinit var updateUseCase: UpdateUseCase

    @Inject
    lateinit var deleteUseCase: DeleteUseCase

    @Inject
    lateinit var getOneUseCase: GetByIdUseCase

    @Inject
    lateinit var getAdbyIdUseCase: GetAdByIdUseCase

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getById(@PathParam("id") adId: Int): Response {
        val feedback = getOneUseCase.execute(adId)

        return if (feedback != null) {
            return Response.ok(FeedbackDTO.fromFeedback(feedback)).build()
        } else {
            Response.status(Response.Status.NOT_FOUND).build()
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun create(feedbackDTO: FeedbackDTO): Response {
        val feedback = Feedback(null, feedbackDTO.adId, feedbackDTO.userId, feedbackDTO.rating, feedbackDTO.description, feedbackDTO.date)

        feedbackDTO.rating?.let { rating ->
            if (rating < 1 || rating > 5 || getAdbyIdUseCase.execute(feedbackDTO.adId) == null) {
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
        }


        val createdFeedback = createUseCase.execute(feedback)
        return Response.status(Response.Status.CREATED).entity(FeedbackDTO.fromFeedback(createdFeedback)).build()
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ad/{adId}")
    fun getListByAd(@PathParam("adId") adId: Int): List<FeedbackDTO> {
        val feedbacks = getListByAdUseCase.execute(adId)
        return feedbacks.map { FeedbackDTO.fromFeedback(it) }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    fun update(@PathParam("id") feedbackId: Int, feedbackDTO: FeedbackDTO): Response {
        val feedback = Feedback(null, feedbackDTO.adId, feedbackDTO.userId, feedbackDTO.rating, feedbackDTO.description, feedbackDTO.date)
        feedbackDTO.rating?.let { rating ->
            if (rating < 1 || rating > 5) {
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
        }

        updateUseCase.execute(feedbackId, feedback)
        return Response.ok(FeedbackDTO.fromFeedback(feedback)).build()
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    fun delete(@PathParam("id") feedbackId: Int): Response {
        deleteUseCase.execute(feedbackId)
        return Response.noContent().build()
    }

}