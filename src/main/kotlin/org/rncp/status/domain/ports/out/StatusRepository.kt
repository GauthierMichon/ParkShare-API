package org.rncp.status.domain.ports.out

import org.rncp.status.domain.model.Status

interface StatusRepository {
    fun getById(id: Int): Status?
}