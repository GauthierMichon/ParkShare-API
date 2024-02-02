package org.rncp.firebase

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import java.io.IOException

@ApplicationScoped
class FirebaseInitializer {
    @Startup
    fun initializeFirebase() {
        try {
            val serviceAccount = object {}.javaClass.getResourceAsStream("/firebase/firebase-config.json")

            val options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()

            FirebaseApp.initializeApp(options)


        } catch (e: IOException) {
            println("Erreur d'initialisation firebase : ${e.message}")
            e.printStackTrace()
        }
    }
}