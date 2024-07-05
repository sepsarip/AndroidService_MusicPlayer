package pnj.exam.androidservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private val audioResource: String = "https://dl185.filemate22.shop/?file=M3R4SUNiN3JsOHJ6WWRMaXN2NlJvOWlxVlZIOCtyZzNrTnd4eGg0c0NxUUhxSUlrMk9xdktNWUVKYWdjeFlPMkVOQkE5RENUWE1uQUZBcU1wTk1BVUhxRStkSjJsekRFL0pvaFY0ZDhSa1crbmUrdmt3NW1pQlA5STUyZk02RlplR051b1VVbWdRQ2UzS0tSNnpQKzlpK0wvbXVYZmc0bnYybFlaZmFlL0k4YmsyN09ZS3JOOTlZQXRUYVc4SjlkMC8yWTZWQ2hrZUp2NFlRZ0RoMHlZWlZiNXBuOTJ2UEgrQlpmazQ5QjNrLzM%3D"
    private var musicPlaying : Boolean = true
    private lateinit var serviceIntent : Intent
    lateinit var button: ImageView

    private lateinit var sharedPreferences: SharedPreferences
    private val buttonReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.getStringExtra("update")
            if (action=="changePlay"){
                button.setImageResource(R.drawable.play)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(buttonReceiver, IntentFilter("button.update"), RECEIVER_EXPORTED)
        }

        musicPlaying = true

        button = findViewById(R.id.btn)
        button.setImageResource(R.drawable.pause)
        serviceIntent = Intent(this, MyPlayService::class.java)
        playAudio()

        button.setOnClickListener {
            musicPlaying = if(!musicPlaying){
                playAudio()
                button.setImageResource(R.drawable.pause)
                true
            }else{
                stopPlayService()
                button.setImageResource(R.drawable.play)
                false
            }
        }

    }

    private fun stopPlayService(){
        try {
            stopService(serviceIntent)
        }catch (e: SecurityException){
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()

        }

    }


    private fun playAudio() {
        serviceIntent.putExtra("audiolink", audioResource)

        try {
            startService(serviceIntent)
        }
        catch (e : SecurityException) {
            Toast.makeText(this, "Error play : " + e.message,Toast.LENGTH_SHORT).show()
        }
    }




}