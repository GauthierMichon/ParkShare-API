package org.rncp.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.google.firebase.auth.UserRecord
import com.google.firebase.auth.UserRecord.UpdateRequest
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.user.domain.model.User
import org.rncp.user.domain.ports.out.UserRepository

@ApplicationScoped
class FirebaseService {

    @Inject
    private lateinit var userRepository: UserRepository

    class FirebaseAuthException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

    fun updatePwd(uid: String, pwd: String) {
        val request = UpdateRequest(uid).setPassword(pwd)
        FirebaseAuth.getInstance().updateUser(request)
    }

    fun refreshToken(uid: String): String? {
        return try {
            val customClaims = mapOf<String, Any>()
            FirebaseAuth.getInstance().createCustomToken(uid, customClaims)
        } catch (e: FirebaseAuthException) {
            println("Erreur lors de la génération du token: ${e.message}")
            null
        }
    }

    fun updateEmail(uid: String, email: String) {
        val user = FirebaseAuth.getInstance().getUser(uid)

        if (user.email != email) {
            val request = UpdateRequest(uid).setEmail(email)
            FirebaseAuth.getInstance().updateUser(request)
        }
    }

    fun deleteUser(uid: String) {
        FirebaseAuth.getInstance().deleteUser(uid)
    }


    fun registerUserInFirebase(userData: User, password: String): String? {
        return try {
            val userRecord = FirebaseAuth.getInstance().createUser(
                    UserRecord.CreateRequest()
                            .setEmail(userData.email)
                            .setPassword(password)
                            .setDisplayName(userData.firstname)
            )

            userRecord.uid
        } catch (e: FirebaseAuthException) {
            null
        }
    }
}