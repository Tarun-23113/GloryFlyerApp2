package com.example.gloryflyerapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.gloryflyerapp.R
import com.example.gloryflyerapp.data.Event
import com.example.gloryflyerapp.data.EventType
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter
import android.net.Uri

object FlyerGenerator {
    fun generateFlyerBitmap(context: Context, event: Event): Bitmap {
        // Create a bitmap with a white background
        val width = 1080
        val height = 1920
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        
        // Draw background
        canvas.drawColor(Color.WHITE)
        
        // Draw event type icon
        val iconResId = when (event.type) {
            EventType.BIRTHDAY -> R.drawable.ic_birthday
            EventType.WEDDING -> R.drawable.ic_wedding
            EventType.ANNIVERSARY -> R.drawable.ic_anniversary
            EventType.OTHER -> R.drawable.ic_event
        }
        
        val icon = ContextCompat.getDrawable(context, iconResId)
        icon?.setBounds(width / 4, height / 8, width * 3 / 4, height / 4)
        icon?.draw(canvas)
        
        // Draw event name
        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 72f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(event.name, width / 2f, height / 3f, titlePaint)
        
        // Draw date
        val datePaint = Paint().apply {
            color = Color.GRAY
            textSize = 48f
            textAlign = Paint.Align.CENTER
        }
        val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a")
        canvas.drawText(event.date.format(dateFormatter), width / 2f, height / 2.5f, datePaint)
        
        // Draw description if available
        if (event.description.isNotBlank()) {
            val descPaint = Paint().apply {
                color = Color.DKGRAY
                textSize = 36f
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(event.description, width / 2f, height / 2f, descPaint)
        }
        
        // Draw app name
        val appNamePaint = Paint().apply {
            color = Color.GRAY
            textSize = 24f
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Created with Glory Flyer", width / 2f, height * 0.95f, appNamePaint)
        
        return bitmap
    }

    fun generateFlyer(context: Context, event: Event): Uri {
        // Generate the bitmap
        val bitmap = generateFlyerBitmap(context, event)

        // Save the bitmap to a file
        val file = File(context.cacheDir, "flyer_${event.id}.png")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        }

        // Return the URI for the saved file
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }
} 