package bmc.example.remoteconfigdude

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import bmc.example.remoteconfigdude.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainActivity : AppCompatActivity() {

    private lateinit var remoteConfig: FirebaseRemoteConfig

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        remoteConfig = Firebase.remoteConfig
    }

    override fun onResume() {
        super.onResume()
        getFirebaseRemoteConfig()
    }

    private fun getFirebaseRemoteConfig() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) {
                0 // Kept 0 for quick debug
            } else {
                60 * 60 // Change this based on your requirement
            }
        }

        remoteConfig.setConfigSettingsAsync(configSettings)
        //remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.setDefaultsAsync(DEFAULTS)
        remoteConfig.fetchAndActivate().addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val updated = task.result
                Log.d(TAG, "Config params updated: $updated")
                Log.d(TAG, "Fetch and activate succeeded")
            } else {
                Log.d(TAG, "Fetch failed")
            }
            applyConfig()
        }
    }

    private fun applyConfig() {
        binding.let {
            it.textView.visibility = View.VISIBLE
            it.textView.text = getWelcomeText()
            val welcomeTextColor = getWelcomeTextColor()
            val layoutColor = getBgColor()

            it.constraintLayout.setBackgroundColor(Color.parseColor(layoutColor))
            it.textView.setTextColor(Color.parseColor(welcomeTextColor))
        }
    }

    private fun getWelcomeText(): String = remoteConfig.getString(WELCOME_TEXT)

    private fun getWelcomeTextColor(): String = remoteConfig.getString(WELCOME_TEXT_COLOR)

    private fun getBgColor(): String = remoteConfig.getString(BG_COLOR)

    companion object {
        private const val TAG = "RemoteConfigUtils"

        private const val WELCOME_TEXT = "welcome_text"
        private const val WELCOME_TEXT_COLOR = "welcome_text_color"
        private const val BG_COLOR = "bg_color"

        private val DEFAULTS: HashMap<String, Any> =
                hashMapOf(
                        WELCOME_TEXT to "Incorrect data collection",
                        WELCOME_TEXT_COLOR to "#FFFFFF",
                        BG_COLOR to "#000000"
                )
    }
}
