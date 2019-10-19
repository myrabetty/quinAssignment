package com.quin.assignment.service

import com.opencsv.bean.CsvToBeanBuilder
import model.DailyActivity
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

/**
 * processes the file.
 **/
@Service
class FileService: Logging {
    fun process(file: File): Boolean {

        val reader = BufferedReader(FileReader(file.name))
        val csvBean = CsvToBeanBuilder<DailyActivity>(reader)
                .withType(DailyActivity::class.java)
                .withIgnoreLeadingWhiteSpace(true)
                .withSeparator(',')
                .build()

        val iterator:  Iterator<DailyActivity> = csvBean.iterator()

        while (iterator.hasNext()) {
            val dailyActivity = iterator.next()
        }
        return true
    }
}
