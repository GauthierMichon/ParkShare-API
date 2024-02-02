package org.rncp.user.infra.db

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import org.rncp.user.domain.model.User
import org.rncp.user.domain.ports.out.UserRepository

@ApplicationScoped
class UserPostGreRepository: PanacheRepositoryBase<UserDAO, Int>, UserRepository {
    override fun getByIdUid(uid: String): User {
        return find("uid", uid).firstResult<UserDAO>().toUser()
    }

    override fun create(user: User): User {
        val userDAO = UserDAO(null, user.uid!!, user.firstname, user.lastname, user.email, user.roleId)
        persist(userDAO)
        return userDAO.toUser()
    }
}