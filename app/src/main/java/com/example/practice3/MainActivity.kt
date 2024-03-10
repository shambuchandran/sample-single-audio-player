package com.example.practice3

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager.BadTokenException
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var seek: SeekBar
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        seek = findViewById(R.id.sbcello)
        handler = Handler(Looper.getMainLooper())
        val pause = findViewById<FloatingActionButton>(R.id.pause)
        val play = findViewById<FloatingActionButton>(R.id.play)
        val stop = findViewById<FloatingActionButton>(R.id.stop)
        pause.setOnClickListener {
            mediaPlayer?.pause()
        }
        play.setOnClickListener {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.bach_cello)
                seekBar()
            }
            mediaPlayer?.start()
        }
        stop.setOnClickListener {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(runnable)
            seek.progress = 0
        }

    }

    private fun seekBar() {
        seek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
        val playtime = findViewById<TextView>(R.id.tvplay)
        val remaintime = findViewById<TextView>(R.id.tvdue)
        seek.max = mediaPlayer!!.duration
        runnable = Runnable {
            seek.progress = mediaPlayer!!.currentPosition
            handler.postDelayed(runnable, 1000)
            val play = mediaPlayer!!.currentPosition / 1000
            playtime.text = "$play sec"
            val duration = mediaPlayer!!.duration / 1000
            val durationremain = duration - play
            remaintime.text = "$durationremain sec"

        }
        handler.postDelayed(runnable, 1000)

    }
}