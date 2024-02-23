package org.rncp.feedback.infra.api

import io.quarkus.security.Authenticated
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.*
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import jakarta.ws.rs.core.SecurityContext
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
    @Authenticated
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
    @Authenticated
    fun create(feedbackCreateOrUpdateDTO: FeedbackCreateOrUpdateDTO, @Context securityContext: SecurityContext): Response {
        val userUid = securityContext.userPrincipal.name
        val feedback = Feedback(null, feedbackCreateOrUpdateDTO.adId, userUid, feedbackCreateOrUpdateDTO.rating, feedbackCreateOrUpdateDTO.description, feedbackCreateOrUpdateDTO.date)

        feedbackCreateOrUpdateDTO.rating?.let { rating ->
            if (rating < 1 || rating > 5 || getAdbyIdUseCase.execute(feedbackCreateOrUpdateDTO.adId) == null) {
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
        }


        val createdFeedback = createUseCase.execute(feedback)
        return Response.status(Response.Status.CREATED).entity(FeedbackDTO.fromFeedback(createdFeedback)).build()
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/ad/{adId}")
    @Authenticated
    fun getListByAd(@PathParam("adId") adId: Int): List<FeedbackDTO> {
        val feedbacks = getListByAdUseCase.execute(adId)
        return feedbacks.map { FeedbackDTO.fromFeedback(it) }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    @Transactional
    @Authenticated
    fun update(@PathParam("id") feedbackId: Int, feedbackCreateOrUpdateDTO: FeedbackCreateOrUpdateDTO, @Context securityContext: SecurityContext): Response {
        val userUid = securityContext.userPrincipal.name
        val feedback = Feedback(feedbackId, feedbackCreateOrUpdateDTO.adId, userUid, feedbackCreateOrUpdateDTO.rating, feedbackCreateOrUpdateDTO.description, feedbackCreateOrUpdateDTO.date)
        feedbackCreateOrUpdateDTO.rating?.let { rating ->
            if (rating < 1 || rating > 5) {
                return Response.status(Response.Status.BAD_REQUEST).build()
            }
        }

        return updateUseCase.execute(feedbackId, feedback)
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    @Authenticated
    fun delete(@PathParam("id") feedbackId: Int): Response {
        deleteUseCase.execute(feedbackId)
        return Response.noContent().build()
    }

}