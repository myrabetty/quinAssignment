package com.quin.assignment.service

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
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

        var record: Array<String>?
        val reader = BufferedReader(FileReader(file.name))

        val parser = CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(false)
                .build()

        val csvReader = CSVReaderBuilder(reader)
                .withSkipLines(1)
                .withCSVParser(parser)
                .build()

        record = csvReader.readNext()
        while (record != null) {
            DailyActivity.mapper(record)
            record = csvReader.readNext()
        }

        csvReader.close()
        file.deleteOnExit()
        return true
    }
}
