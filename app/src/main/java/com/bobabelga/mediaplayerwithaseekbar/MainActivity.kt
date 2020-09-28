package com.bobabelga.mediaplayerwithaseekbar

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mp : MediaPlayer? = null
    private var uri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Intent.ACTION_VIEW == intent.action) {
            uri = intent.data
            if (mp==null){
                Glide.with(this).load(R.drawable.gif).into(imageView)
                mp = MediaPlayer.create(this,uri)
                mp?.start()
                initialiseSeekbar()
            }
        }

        play.setOnClickListener {
            Glide.with(this).load(R.drawable.gif).into(imageView)
            if (mp==null){
                if (uri != null) mp = MediaPlayer.create(this,uri)
                else mp = MediaPlayer.create(this,R.raw.song)
            }
            // -- Change voice --
            // mp?.playbackParams = PlaybackParams().setPitch(1f)
            mp?.start()
            initialiseSeekbar()

        }
        pause.setOnClickListener {
            if (mp!=null){
                imageView.setImageDrawable(getDrawable(R.drawable.png))
                mp?.pause()
            }
        }
        stop.setOnClickListener {
            if (mp!=null){
                imageView.setImageDrawable(getDrawable(R.drawable.png))
                stopMediaPlayer()
            }
        }
        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) mp?.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }

    private fun initialiseSeekbar(){
        slider.max = mp!!.duration
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    slider.progress = mp!!.currentPosition
                    if (!mp!!.isPlaying && slider.progress == mp!!.duration) {
                        imageView.setImageDrawable(getDrawable(R.drawable.png))
                        stopMediaPlayer()
                    }
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    slider.progress = 0
                }
            }

        }, 0)
    }

    private fun stopMediaPlayer() {
        mp?.stop()
        mp?.reset()
        mp?.release()
        mp = null
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
    }

    override fun onStop() {
        super.onStop()
        stopMediaPlayer()
    }


}