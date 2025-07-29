package costaber.com.github.omniflow.util

import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.StandardCharsets

class FileReader {
    @Throws(IOException::class)
    fun readFileFromResources(fileName: String?): String {
        val classLoader = javaClass.classLoader
        val resourceFile = classLoader.getResourceAsStream(fileName)
        if (resourceFile == null) {
            throw FileNotFoundException("$fileName not found!")
        }
        return String(resourceFile.readAllBytes(), StandardCharsets.UTF_8)
    }
}
