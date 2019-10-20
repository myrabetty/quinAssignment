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

    fun getActivityStatistics(): ActivityStatistics {
        val maybeLatest = dailyActivityRepository.findFirstByOrderByDateDesc().date
        val latest = maybeLatest ?: Date()
        val series = getTimeSeries(latest)
        val statistics = getWeeklyStatistics(latest)
        return ActivityStatistics(series, statistics)
    }

    private fun getTimeSeries(latest: Date): Map<String, List<Any?>> {
        val cal = Calendar.getInstance()
        cal.time = latest
        cal.add(Calendar.MONTH, -2)
        val twoMonthsBefore = cal.time
        logger.info(latest)
        logger.info(twoMonthsBefore)
        val activities = dailyActivityRepository.findByDateBetween(twoMonthsBefore, latest)
        val series = extractTimeSeries(activities)
        return series
    }

    private fun extractTimeSeries(activities: List<DailyActivity>): Map<String, List<Any?>> {
        val series = HashMap<String, List<Any?>>()
        series["date"] = activities.stream().map { a -> a.date }.collect(Collectors.toList())
        series["steps"] = activities.stream().map { a -> a.steps }.collect(Collectors.toList())
        series["calories"] = activities.stream().map { a -> a.caloriesBurned }.collect(Collectors.toList())
        return series
    }

    private fun getWeeklyStatistics(latest: Date): Map<String, List<Any>> {
        val calories = ArrayList<Double>()
        var dates = ArrayList<Date>()
        var maxDate = latest
        for (i in 1..12) {
            dates.add(maxDate)
            val cal = Calendar.getInstance()
            cal.time = maxDate
            cal.add(Calendar.WEEK_OF_YEAR, -1)
            val minDate = cal.time
            val activities = dailyActivityRepository.findByDateBetween(minDate, maxDate)
            calories.add(activities.stream().mapToDouble { a -> (a?.caloriesBurned ?: 0) * 1.0 }.average().orElse(0.0))
            maxDate = minDate
        }

        val weeklyStats = HashMap<String, List<Any>>()
        weeklyStats["finalDate"] = dates
        weeklyStats["calories"] = calories
        return weeklyStats
    }
}
