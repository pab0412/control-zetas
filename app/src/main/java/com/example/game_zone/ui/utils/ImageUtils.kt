package com.example.game_zone.ui.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    fun saveImageToInternalStorage(context: Context, imageUri: Uri): String? {
        return try {
            // Leer la imagen desde URI
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // Crear directorio si no existe
            val directory = File(context.filesDir, "profile_images")
            if (!directory.exists()) {
                directory.mkdirs()
            }

            // Crear nombre único para el archivo
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "profile_$timestamp.jpg"
            val file = File(directory, fileName)

            // Comprimir y guardar
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            outputStream.flush()
            outputStream.close()

            // Retornar la ruta completa
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Elimina la imagen de perfil anterior para no acumular archivos
     */
    fun deleteOldProfileImage(imagePath: String?) {
        if (imagePath != null) {
            try {
                val file = File(imagePath)
                if (file.exists()) {
                    file.delete()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * Obtiene un URI para la cámara
     */
    fun createImageFile(context: Context): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(null)
        return File.createTempFile("JPEG_${timestamp}_", ".jpg", storageDir)
    }
}
