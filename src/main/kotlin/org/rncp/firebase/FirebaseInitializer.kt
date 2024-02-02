package org.rncp.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import java.io.FileInputStream
import java.io.IOException

@ApplicationScoped
class FirebaseInitializer {
    @Startup
    fun initializeFirebase() {
        try {
            val serviceAccount = FileInputStream("firebase/firebase-config.json")

            val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()

            FirebaseApp.initializeApp(options)

            println("Firebase initialisé avec succès.")

        } catch (e: IOException) {
            // Gère les erreurs d'initialisation
            println("Erreur lors de l'initialisation de Firebase : ${e.message}")
            e.printStackTrace()
        }
    }
}