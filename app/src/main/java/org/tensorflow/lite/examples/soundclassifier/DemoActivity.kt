package org.tensorflow.lite.examples.soundclassifier

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import kotlin.math.PI
import kotlin.math.sin

class DemoActivity : BaseActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_demo)
    val status = findViewById<TextView>(R.id.demo_status)
    val result = findViewById<TextView>(R.id.demo_result)
    findViewById<Button>(R.id.play_demo).setOnClickListener {
      status.setText(R.string.demo_description)
      result.visibility = TextView.VISIBLE
      result.setText(R.string.demo_result_text)
      Thread {
        playSimulatedCall()
      }.start()
    }
  }

  private fun playSimulatedCall() {
    val sampleRate = 22050
    val durationMs = 1400
    val samples = ShortArray(sampleRate * durationMs / 1000)
    for (i in samples.indices) {
      val t = i.toDouble() / sampleRate
      val envelope = if (t < 0.05 || t > 1.25) 0.0 else 0.55
      val chirp = 640.0 + (t * 480.0)
      samples[i] = (sin(2.0 * PI * chirp * t) * envelope * Short.MAX_VALUE).toInt().toShort()
    }
    val track = AudioTrack(
      AudioManager.STREAM_MUSIC,
      sampleRate,
      AudioFormat.CHANNEL_OUT_MONO,
      AudioFormat.ENCODING_PCM_16BIT,
      samples.size * 2,
      AudioTrack.MODE_STATIC
    )
    track.write(samples, 0, samples.size)
    track.play()
    Thread.sleep(durationMs.toLong())
    track.release()
  }
}
