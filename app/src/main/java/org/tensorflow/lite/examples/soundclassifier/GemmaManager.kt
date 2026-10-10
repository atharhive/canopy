package org.tensorflow.lite.examples.soundclassifier

import android.content.Context
import java.io.File
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import com.google.mediapipe.tasks.genai.llminference.LlmInference

interface GemmaDownloadProgress {
  fun onProgress(progress: Int)
}

interface GemmaDownloadComplete {
  fun onComplete(success: Boolean)
}

object GemmaManager {
  private const val MODEL_FILE = "gemma3-1b-it-int4.task"
  private const val MODEL_URL =
    "https://huggingface.co/litert-community/Gemma3-1B-IT/resolve/main/gemma3-1b-it-int4.task?download=true"
  private const val MIN_MODEL_BYTES = 400L * 1024L * 1024L
  private val executor = Executors.newSingleThreadExecutor()
  private var inference: LlmInference? = null

  fun modelFile(context: Context): File = File(context.filesDir, MODEL_FILE)

  fun isDownloaded(context: Context): Boolean =
    modelFile(context).let { it.isFile && it.length() >= MIN_MODEL_BYTES }

  fun download(context: Context, onProgress: GemmaDownloadProgress, onComplete: GemmaDownloadComplete) {
    executor.execute {
      val target = modelFile(context)
      val temporary = File(context.filesDir, "$MODEL_FILE.download")
      try {
        val connection = URL(MODEL_URL).openConnection() as HttpURLConnection
        connection.connectTimeout = 15_000
        connection.readTimeout = 30_000
        connection.connect()
        if (connection.responseCode !in 200..299) {
          throw IllegalStateException("Gemma download failed with HTTP ${connection.responseCode}")
        }
        val total = connection.contentLengthLong
        connection.inputStream.use { input ->
          temporary.outputStream().use { output ->
            val buffer = ByteArray(1024 * 1024)
            var downloaded = 0L
            var read: Int
            while (input.read(buffer).also { read = it } != -1) {
              output.write(buffer, 0, read)
              downloaded += read
              if (total > 0) onProgress.onProgress(((downloaded * 100) / total).toInt())
            }
          }
        }
        if (temporary.length() < MIN_MODEL_BYTES) {
          throw IllegalStateException("Downloaded file is smaller than the expected Gemma model")
        }
        if (!temporary.renameTo(target)) throw IllegalStateException("Could not store Gemma model")
        onComplete.onComplete(true)
      } catch (error: Exception) {
        temporary.delete()
        onComplete.onComplete(false)
      }
    }
  }

  fun summarize(context: Context, species: String, confidence: Float, onResult: (String) -> Unit) {
    if (!isDownloaded(context)) return
    executor.execute {
      try {
        val model = inference ?: LlmInference.createFromOptions(
          context.applicationContext,
          LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelFile(context).absolutePath)
            .setMaxTopK(16)
            .build()
        ).also { inference = it }
        val prompt = "You are Canopy, an offline bird guide. " +
          "BirdNET detected $species with ${(confidence * 100).toInt()} percent confidence. " +
          "In one short sentence, explain what the listener just heard. " +
          "Do not name any species other than $species. Do not use emojis."
        val result = model.generateResponse(prompt).trim()
        if (result.isNotEmpty()) onResult(result)
      } catch (_: Exception) {
        // The caller retains the deterministic local fallback.
      }
    }
  }

  fun generateChallenge(context: Context, species: List<String>, onResult: (String) -> Unit) {
    if (!isDownloaded(context) || species.isEmpty()) return
    executor.execute {
      try {
        val model = inference ?: LlmInference.createFromOptions(
          context.applicationContext,
          LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelFile(context).absolutePath)
            .setMaxTopK(16)
            .build()
        ).also { inference = it }
        val prompt = "You are an offline birding coach. The user has collected: " +
          species.joinToString(", ") + ". Create one short, actionable birding challenge " +
          "using only these birds. Do not invent species. Return only the challenge."
        val result = model.generateResponse(prompt).trim()
        if (result.isNotEmpty()) onResult(result)
      } catch (_: Exception) {
      }
    }
  }

  fun answerQuestion(context: Context, species: List<String>, question: String, onResult: (String) -> Unit) {
    if (!isDownloaded(context) || question.isBlank()) return
    executor.execute {
      try {
        val model = inference ?: LlmInference.createFromOptions(
          context.applicationContext,
          LlmInference.LlmInferenceOptions.builder()
            .setModelPath(modelFile(context).absolutePath)
            .setMaxTopK(16)
            .build()
        ).also { inference = it }
        val prompt = "You are Canopy, an offline birding coach. The user's collected birds are " +
          species.joinToString(", ") + ". Answer this question briefly: $question. " +
          "Use only general birding knowledge and do not claim the user detected a species " +
          "unless it is in the collection. If uncertain, say so."
        val result = model.generateResponse(prompt).trim()
        if (result.isNotEmpty()) onResult(result)
      } catch (_: Exception) {
      }
    }
  }

  fun close() {
    inference?.close()
    inference = null
  }
}
