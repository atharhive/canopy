package org.tensorflow.lite.examples.soundclassifier

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

object NarrationManager : TextToSpeech.OnInitListener {
  private var textToSpeech: TextToSpeech? = null
  private var ready = false
  private var lastSpecies = ""
  private var lastSpokenAt = 0L

  fun initialize(context: Context) {
    if (textToSpeech == null) {
      textToSpeech = TextToSpeech(context.applicationContext, this)
    }
  }

  override fun onInit(status: Int) {
    ready = status == TextToSpeech.SUCCESS
    if (ready) {
      textToSpeech?.language = Locale.getDefault()
    }
  }

  fun speak(context: Context, species: String, confidence: Float) {
    initialize(context)
    val now = System.currentTimeMillis()
    if (!ready || (species == lastSpecies && now - lastSpokenAt < 30_000L)) return
    lastSpecies = species
    lastSpokenAt = now
    val confidenceText = (confidence * 100).toInt()
    val fallback = "$species detected with $confidenceText percent confidence."
    val useGemma = androidx.preference.PreferenceManager
      .getDefaultSharedPreferences(context)
      .getBoolean("gemma_enabled", false)
    if (useGemma && GemmaManager.isDownloaded(context)) {
      GemmaManager.summarize(context, species, confidence) { summary ->
        textToSpeech?.speak(summary, TextToSpeech.QUEUE_FLUSH, null, "canopy-$now")
      }
    } else {
      textToSpeech?.speak(fallback, TextToSpeech.QUEUE_FLUSH, null, "canopy-$now")
    }
  }

  fun shutdown() {
    textToSpeech?.stop()
    textToSpeech?.shutdown()
    textToSpeech = null
    ready = false
    lastSpecies = ""
    lastSpokenAt = 0L
    GemmaManager.close()
  }
}
