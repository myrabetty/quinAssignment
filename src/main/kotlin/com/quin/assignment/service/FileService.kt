package com.quin.assignment.service

import model.DailyActivity
import org.springframework.stereotype.Service
import org.apache.logging.log4j.kotlin.Logging
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * processes the file.
 **/
@Service
class FileService: Logging {
    fun process(file: File): Boolean {
        var fileReader: BufferedReader? = null

        var line: String?

        fileReader = BufferedReader(FileReader(file.name))

        // Read CSV header
        fileReader.readLine()

        // Read the file line by line starting from the second line
        line = fileReader.readLine()
        while (line != null) {
            val tokens = line.split(",")
            if (tokens.isNotEmpty()) {
                DailyActivity.mapper(tokens)
            }
    }

}
