package costaber.com.github.omniflow.util

import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class FileReader {
    @Throws(IOException::class)
    fun readFileFromResources(fileName: String?): String {
        val classLoader = javaClass.getClassLoader()
        val resourceFile = classLoader.getResource(fileName)
        if (resourceFile == null) {
            throw FileNotFoundException(fileName + " not found!")
        }
        val filePath = resourceFile.getPath()
        val fileContent = Files.readAllBytes(Paths.get(filePath))
        return String(fileContent, StandardCharsets.UTF_8)
    }
}
