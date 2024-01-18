package org.rncp

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase
import jakarta.enterprise.context.ApplicationScoped
import org.rncp.Entity.User

@ApplicationScoped
class UserRepository : PanacheRepositoryBase<User, Int>
