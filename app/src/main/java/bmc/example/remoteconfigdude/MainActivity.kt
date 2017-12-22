package bmc.example.remoteconfigdude

import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings


class MainActivity : AppCompatActivity() {

    private lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        val configSettings = FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build()
        firebaseRemoteConfig.setConfigSettings(configSettings)

        firebaseRemoteConfig.setDefaults(R.xml.remote_config_params);

        firebaseRemoteConfig.activateFetched();
        applyConfig();
        firebaseRemoteConfig.fetch(0);
    }

    private fun applyConfig() {
        //Get the widget from XML layout
        val layout = findViewById<ConstraintLayout>(R.id.layout)
        val textview = findViewById<TextView>(R.id.textView)

        //Get the values form Firebase remote configuration
        val welcomeText = firebaseRemoteConfig.getString("welcome_text")
        val welcomeTextColor = firebaseRemoteConfig.getString("welcome_text_color")
        val layoutColor = firebaseRemoteConfig.getString("bg_color")

        // Pone a disposición de la app los valores, en este caso los valores por defecto en el XML
        layout.setBackgroundColor(Color.parseColor(layoutColor))
        textview.text = welcomeText
        // Crea solicitud para recuperar los valores desde el servidor Firebase
        textview.setTextColor(Color.parseColor(welcomeTextColor))
    }

    private fun updateConfg() {
        firebaseRemoteConfig.fetch(0).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                firebaseRemoteConfig.activateFetched()
                applyConfig()
            } else {
                // Fetch failed
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.getItemId()

        //noinspection SimplifiableIfStatement
        if (id == R.id.updateConfig) {
            updateConfg()
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
