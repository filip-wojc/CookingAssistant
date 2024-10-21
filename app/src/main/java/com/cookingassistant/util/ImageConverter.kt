package com.cookingassistant.util
import java.io.File
import java.io.FileInputStream
import java.io.IOException


class ImageConverter() {
    fun convertImageToByteArray(imagePath: String):ByteArray?{
        try
        {
            val file = File(imagePath)

            if (file.exists()) {
                println("Image found: ${file.absolutePath}")
                val inputStream = FileInputStream(file)
                return inputStream.readBytes() // reads the file into a ByteArray
            } else {
                println("Image not found: $imagePath")
                return null
                }
        } catch (e: IOException)
        {
            e.printStackTrace()
            return null
        }
    }
}