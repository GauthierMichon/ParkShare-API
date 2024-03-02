package org.rncp.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import com.google.firebase.auth.UserRecord
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.rncp.user.domain.model.User
import org.rncp.user.domain.ports.out.UserRepository

@ApplicationScoped
class FirebaseService {

    @Inject
    private lateinit var userRepository: UserRepository

    class FirebaseAuthException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
    fun authenticate(email: String, password: String): String? {
//        try {
//            val authResult = FirebaseAuth.getInstance()
//
//            val existingUser = userRepository.getByIdUid(uid)
//
//            if (existingUser != null) {
//                return existingUser
//            } else {
//                throw FirebaseAuthException("L'utilisateur avec cet UID n'existe pas.")
//            }
//        } catch (e: FirebaseAuthException) {
//            throw FirebaseAuthException("Erreur d'authentification : ${e.message}", e)
//        }

        return ""
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