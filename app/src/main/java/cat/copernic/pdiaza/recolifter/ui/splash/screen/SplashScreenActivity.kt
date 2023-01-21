package cat.copernic.pdiaza.recolifter.ui.splash.screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import cat.copernic.pdiaza.recolifter.databaseManager.FirebaseSets
import cat.copernic.pdiaza.recolifter.ui.flux.MainActivity
import cat.copernic.pdiaza.recolifter.ui.login.register.PreLoginActivity
import cat.copernic.pdiaza.recolifter.models.Language
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*

/**
 * Clase encargada de mostrar el splashScreen
 */
class SplashScreenActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        //Iniciar SplashScreen
        installSplashScreen()
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        checkUserLogined()
        Language.instance.checkDeviceLanguage(Locale.getDefault().language)

        val set = FirebaseSets()
        set.addFirebaseCat()
        set.addFirebaseEs()
        set.addFirebaseEn()
    }

    /**
     * Verificar si el usuario esta logeado.
     */
    private fun checkUserLogined() {
        var changeActivity: Intent

        if (auth.currentUser == null) {
            changeActivity = Intent(this, PreLoginActivity::class.java)

        } else {
            changeActivity = Intent(this, MainActivity::class.java)
        }
        startActivity(changeActivity)
        finish()
    }
}