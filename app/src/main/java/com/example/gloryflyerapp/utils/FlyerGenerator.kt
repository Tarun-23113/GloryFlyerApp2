package com.example.gloryflyerapp.utils

import android.content.Context
import android.graphics.*
import android.view.View
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import com.example.gloryflyerapp.data.Event
import java.time.format.DateTimeFormatter
import android.graphics.Color as AndroidColor

object FlyerGenerator {
    fun generateFlyerBitmap(context: Context, event: Event, width: Int = 1080, height: Int = 1920): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        // Set background gradient
        val gradient = LinearGradient(
            0f, 0f, 0f, height.toFloat(),
            AndroidColor.rgb(33, 150, 243), // Primary blue
            AndroidColor.rgb(13, 71, 161), // Darker blue
            Shader.TileMode.CLAMP
        )
        val paint = Paint().apply {
            shader = gradient
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        
        // Reset paint for text
        paint.reset()
        paint.apply {
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        // Add decorative elements
        val decorPaint = Paint().apply {
            color = AndroidColor.argb(50, 255, 255, 255)
            style = Paint.Style.STROKE
            strokeWidth = 5f
        }
        canvas.drawCircle(width * 0.8f, height * 0.2f, 200f, decorPaint)
        canvas.drawCircle(width * 0.2f, height * 0.8f, 150f, decorPaint)

        // Draw event type badge
        val badgePaint = Paint().apply {
            color = AndroidColor.WHITE
            alpha = 50
            style = Paint.Style.FILL
        }
        val badgeRect = RectF(
            width * 0.2f,
            150f,
            width * 0.8f,
            250f
        )
        canvas.drawRoundRect(badgeRect, 25f, 25f, badgePaint)

        // Draw event type
        paint.apply {
            color = AndroidColor.WHITE
            textSize = 64f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        canvas.drawText(
            event.type.name.replace("_", " "),
            width / 2f,
            220f,
            paint
        )

        // Draw title with shadow
        paint.apply {
            textSize = 120f
            setShadowLayer(10f, 0f, 5f, AndroidColor.argb(100, 0, 0, 0))
        }
        canvas.drawText(
            event.title,
            width / 2f,
            450f,
            paint
        )
        paint.clearShadowLayer()

        // Draw description
        paint.apply {
            textSize = 72f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }
        
        // Word wrap description
        val descriptionLines = wrapText(event.description, paint, width - 200f)
        var yPos = 650f
        for (line in descriptionLines) {
            canvas.drawText(line, width / 2f, yPos, paint)
            yPos += 100f
        }

        // Draw date and time section with larger, more prominent design
        val dateTimeBg = Paint().apply {
            color = AndroidColor.WHITE
            alpha = 30
            style = Paint.Style.FILL
        }
        val dateTimeRect = RectF(
            width * 0.1f,
            yPos + 100f,
            width * 0.9f,
            yPos + 400f
        )
        canvas.drawRoundRect(dateTimeRect, 40f, 40f, dateTimeBg)

        // Add a subtle glow effect to the date/time box
        val glowPaint = Paint().apply {
            color = AndroidColor.WHITE
            alpha = 15
            style = Paint.Style.STROKE
            strokeWidth = 8f
            maskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.NORMAL)
        }
        canvas.drawRoundRect(dateTimeRect, 40f, 40f, glowPaint)

        paint.apply {
            color = AndroidColor.WHITE
            textSize = 90f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        
        // Date with larger text and better spacing
        canvas.drawText(
            event.date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")),
            width / 2f,
            yPos + 200f,
            paint
        )

        // Time with prominent display
        paint.apply {
            textSize = 100f
            typeface = Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD)
        }
        canvas.drawText(
            event.date.format(DateTimeFormatter.ofPattern("h:mm a")),
            width / 2f,
            yPos + 350f,
            paint
        )

        return bitmap
    }

    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val words = text.split(" ")
        val lines = mutableListOf<String>()
        var currentLine = StringBuilder()

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "${currentLine} $word"
            val measureWidth = paint.measureText(testLine)

            if (measureWidth <= maxWidth) {
                currentLine = StringBuilder(testLine)
            } else {
                if (currentLine.isNotEmpty()) {
                    lines.add(currentLine.toString())
                }
                currentLine = StringBuilder(word)
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine.toString())
        }

        return lines
    }

    fun saveToGallery(context: Context, bitmap: Bitmap, fileName: String): String {
        val imagesDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
        val image = java.io.File(imagesDir, "$fileName.png")
        image.outputStream().use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }
        return image.absolutePath
    }
} 