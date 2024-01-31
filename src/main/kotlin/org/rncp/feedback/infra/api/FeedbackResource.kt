package org.rncp.feedback.infra.api

import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.rncp.feedback.domain.model.Feedback
import org.rncp.feedback.domain.ports.`in`.CreateUseCase
import org.rncp.feedback.domain.ports.`in`.DeleteUseCase
import org.rncp.feedback.domain.ports.`in`.GetListByAdUseCase
import org.rncp.feedback.domain.ports.`in`.UpdateUseCase


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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    fun create(feedbackDTO: FeedbackDTO): Response {
        val feedback = Feedback(null, feedbackDTO.adId, feedbackDTO.userId, feedbackDTO.rating, feedbackDTO.description, feedbackDTO.date)
        createUseCase.execute(feedback)
        return Response.status(Response.Status.CREATED).entity(FeedbackDTO.fromFeedback(feedback)).build()
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