package com.quin.assignment.service

import com.quin.assignment.model.ActivityStatistics
import com.quin.assignment.model.DailyActivity
import com.quin.assignment.repository.DailyActivityRepository
import org.apache.logging.log4j.kotlin.Logging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.Date
import java.util.Calendar
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * service for statistics generating and returning statistics.
 */
@Service
class StatisticsService @Autowired constructor(
        private val dailyActivityRepository: DailyActivityRepository
) : Logging {

    fun getActivityStatistics(): ActivityStatistics {
        val latest = dailyActivityRepository.findFirstByOrderByDateDesc()
        latest?.let {
            val date = latest.date
            val series = getTimeSeries(date!!)
            val statistics = getWeeklyStatistics(date!!)
            return ActivityStatistics(series, statistics)
        }
        return ActivityStatistics(emptyMap(), emptyMap())
    }

    private fun getTimeSeries(latest: Date): Map<String, List<Any?>> {
        val cal = Calendar.getInstance()
        cal.time = latest
        cal.add(Calendar.MONTH, -2)
        val twoMonthsBefore = cal.time
        logger.debug(latest)
        logger.debug(twoMonthsBefore)
        val activities = dailyActivityRepository.findByDateBetween(twoMonthsBefore, latest)
        return extractTimeSeries(activities)
    }

    private fun extractTimeSeries(activities: List<DailyActivity>): Map<String, List<Any?>> {
        val series = HashMap<String, List<Any?>>()
        series["date"] = activities.stream().map { a -> a.date }.collect(Collectors.toList())
        series["steps"] = activities.stream().map { a -> a.steps }.collect(Collectors.toList())
        series["calories"] = activities.stream().map { a -> a.caloriesBurned }.collect(Collectors.toList())
        return series
    }

    private fun getWeeklyStatistics(latest: Date): Map<String, List<Any>> {
        val caloriesBurned = ArrayList<Int>()
        val caloriesIntake = ArrayList<Int>()
        val numberOfDays = ArrayList<Int>()
        val finalDates = ArrayList<Date>()
        val initialDates = ArrayList<Date>()
        var maxDate = latest
        for (i in 1..12) {
            finalDates.add(maxDate)
            val cal = Calendar.getInstance()
            cal.time = maxDate
            cal.add(Calendar.WEEK_OF_YEAR, -1)
            cal.add(Calendar.DAY_OF_YEAR, 1)
            val minDate = cal.time
            initialDates.add(minDate)
            val activities = dailyActivityRepository.findByDateBetween(minDate, maxDate)
            caloriesBurned.add(activities.stream().mapToDouble { a -> (a?.caloriesBurned!!.toDouble()) }.average().orElse(0.0).toInt())
            caloriesIntake.add(activities.stream().mapToDouble { a -> (a?.caloriesIntake!!.toDouble())}.average().orElse(0.0).toInt())
            numberOfDays.add(activities.size)
            maxDate = minDate
        }

        val weeklyStats = HashMap<String, List<Any>>()
        weeklyStats["finalDate"] = finalDates
        weeklyStats["initialDate"] = initialDates
        weeklyStats["caloriesBurned"] = caloriesBurned
        weeklyStats["caloriesIntake"] = caloriesIntake
        weeklyStats["numberOfDays"] = numberOfDays
        return weeklyStats
    }
}