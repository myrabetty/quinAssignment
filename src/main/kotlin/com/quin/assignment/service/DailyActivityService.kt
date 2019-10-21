package com.quin.assignment.service

import com.opencsv.bean.CsvToBeanBuilder
import com.quin.assignment.model.ActivityStatistics
import com.quin.assignment.model.DailyActivity
import com.quin.assignment.repository.DailyActivityRepository
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.IOException
import java.io.StringReader
import java.util.*
import java.util.stream.Collectors
import javax.transaction.Transactional
import java.util.Calendar
import kotlin.collections.HashMap


/**
 * Service to process the file.
 **/
@Service
class DailyActivityService @Autowired constructor(
        private val dailyActivityRepository: DailyActivityRepository
) : Logging {

    fun process(content: String): Boolean {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(StringReader(content))

            val csvBean = CsvToBeanBuilder<DailyActivity>(reader)
                    .withType(DailyActivity::class.java)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withSeparator(',')
                    .build()

            val iterator: Iterator<DailyActivity> = csvBean.iterator()

            while (iterator.hasNext()) {
                val dailyActivity = iterator.next()
                dailyActivityRepository.save(dailyActivity)
            }
            return true
        } catch (e: Exception) {
            logger.error("file processing failed")
            e.printStackTrace()
            return false
        } finally {
            try {
                reader!!.close()
            } catch (e: IOException) {
                logger.error("Closing fileReader/csvParser Error!")
                e.printStackTrace()
            }
        }
    }

    @Transactional
    fun getData(page: Int, limit: Int): List<DailyActivity> {
        val pageRequest = PageRequest.of(page, limit)

        return dailyActivityRepository.findAllByOrderByDateDesc(pageRequest).get().collect(Collectors.toList())
    }


}
