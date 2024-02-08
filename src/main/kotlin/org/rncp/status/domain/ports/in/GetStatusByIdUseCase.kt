package org.rncp.status.domain.ports.`in`

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.status.domain.model.Status
import org.rncp.status.domain.ports.out.StatusRepository

@ApplicationScoped
class GetStatusByIdUseCase {

    @Inject
    private lateinit var statusRepository: StatusRepository

    fun execute(id: Int): Status? {
        return statusRepository.getById(id)
    }
}