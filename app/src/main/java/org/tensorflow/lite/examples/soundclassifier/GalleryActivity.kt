package org.tensorflow.lite.examples.soundclassifier

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.EditText
import android.widget.Button
import android.view.View
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import java.text.DateFormat
import java.util.Date

class GalleryActivity : BaseActivity() {
    private lateinit var database: BirdDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        database = BirdDBHelper.getInstance(this)
        setContentView(ScrollView(this).apply {
            setBackgroundColor(ContextCompat.getColor(context, R.color.surface_light))
            addView(buildContent())
        })
    }

    private fun buildContent(): LinearLayout {
        val observations = database.getCollectedSpecies()
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(24, 32, 24, 32)
        }
        content.addView(TextView(this).apply {
            text = "Canopy collection"
            textSize = 28f
            setTextColor(ContextCompat.getColor(context, R.color.forest))
        })
        content.addView(TextView(this).apply {
            text = "Your detected birds, collected like field cards"
            textSize = 16f
            setPadding(0, 8, 0, 24)
            setTextColor(Color.DKGRAY)
        })
        if (observations.isEmpty()) {
            content.addView(TextView(this).apply {
                text = "No birds collected yet. Start a detection session to earn your first card."
                textSize = 18f
            })
        } else {
            observations.forEach { observation ->
                content.addView(card(observation))
            }
        }
        content.addView(TextView(this).apply {
            text = "\nGemma challenge"
            textSize = 22f
            setTextColor(ContextCompat.getColor(context, R.color.forest))
        })
        val challenge = TextView(this).apply {
            text = challengeText(observations)
            textSize = 17f
            setPadding(0, 8, 0, 0)
        }
        content.addView(challenge)
        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("gemma_enabled", false)) {
            GemmaManager.generateChallenge(this, observations.map { it.name }) { generated ->
                runOnUiThread { challenge.text = generated }
            }
        }
        addChallengeTools(content, observations, challenge)
        return content
    }

    private fun addChallengeTools(
        content: LinearLayout,
        observations: List<BirdObservation>,
        challenge: TextView
    ) {
        val modelReady = GemmaManager.isDownloaded(this)
        val modelStatus = TextView(this).apply {
            text = if (modelReady) {
                "Gemma is ready for offline challenges and questions."
            } else {
                "Gemma is optional. Download it to create custom challenges and chat offline."
            }
            textSize = 14f
            setPadding(0, 18, 0, 8)
        }
        content.addView(modelStatus)
        val download = Button(this).apply {
            text = if (modelReady) "Gemma downloaded" else "Download Gemma (~529 MB)"
            isEnabled = !modelReady
            setOnClickListener {
                isEnabled = false
                modelStatus.text = "Downloading Gemma..."
                GemmaManager.download(this@GalleryActivity, object : GemmaDownloadProgress {
                    override fun onProgress(progress: Int) {
                        runOnUiThread { modelStatus.text = "Downloading Gemma: $progress%" }
                    }
                }, object : GemmaDownloadComplete {
                    override fun onComplete(success: Boolean) {
                        runOnUiThread {
                            isEnabled = !success
                            modelStatus.text = if (success) {
                                PreferenceManager.getDefaultSharedPreferences(this@GalleryActivity)
                                    .edit().putBoolean("gemma_enabled", true).apply()
                                "Gemma is ready. Reopen this page to generate a custom challenge."
                            } else {
                                "Gemma download failed. Check your connection and try again."
                            }
                        }
                    }
                })
            }
        }
        content.addView(download)

        val question = EditText(this).apply {
            hint = "Ask about your collection or birding"
            setSingleLine(false)
            minLines = 2
            setPadding(12, 12, 12, 12)
        }
        content.addView(question)
        val answer = TextView(this).apply {
            textSize = 16f
            setPadding(0, 10, 0, 10)
        }
        content.addView(answer)
        content.addView(Button(this).apply {
            text = "Ask Gemma"
            isEnabled = modelReady
            setOnClickListener {
                answer.text = "Thinking offline..."
                GemmaManager.answerQuestion(
                    this@GalleryActivity,
                    observations.map { it.name },
                    question.text.toString()
                ) { result -> runOnUiThread { answer.text = result } }
            }
        })
    }

    private fun card(observation: BirdObservation): TextView {
        val count = database.getObservationCountForSpecies(observation.speciesId)
        return TextView(this).apply {
            val confidence = (observation.probability * 100).toInt()
            text = "🐦  ${observation.name}\n" +
                    "Best match: $confidence%  •  Heard $count time${if (count == 1) "" else "s"}\n" +
                    "Collected ${DateFormat.getDateInstance().format(Date(observation.millis))}"
            textSize = 18f
            gravity = Gravity.CENTER_VERTICAL
            setTextColor(Color.WHITE)
            setPadding(24, 24, 24, 24)
            setBackgroundColor(ContextCompat.getColor(context, R.color.forest))
            val params = LinearLayout.LayoutParams(-1, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 0, 0, 16)
            layoutParams = params
        }
    }

    private fun challengeText(observations: List<BirdObservation>): String {
        val challenges = listOf(
            "Find your first bird to unlock a challenge.",
            "Find one more species to complete your starter set.",
            "Discover a bird you have not collected yet.",
            "Try to hear two different species before lunch.",
            "Record a detection above 70% confidence.",
            "Visit a new outdoor spot and compare its bird sounds.",
            "Listen quietly for five minutes and identify the clearest call.",
            "Collect three observations on the same day.",
            "Find a species at sunrise or sunset.",
            "Improve a card by finding the same species with higher confidence.",
            "Collect a bird whose name starts with a different letter.",
            "Build a five-species collection.",
            "Revisit your favorite location and look for a new card."
        )
        val index = if (observations.isEmpty()) 0 else (observations.size % (challenges.size - 1)) + 1
        return challenges[index]
    }
}
