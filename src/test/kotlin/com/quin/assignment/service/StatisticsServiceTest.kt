package com.quin.assignment.service

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.quin.assignment.model.DailyActivity
import com.quin.assignment.repository.DailyActivityRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat

internal class StatisticsServiceTest {

    private val dailyActivityRepository: DailyActivityRepository = mock()
    private val statisticsService = StatisticsService(dailyActivityRepository)
    @Test
    fun getActivityStatistics_timeSeriesReturned() {
        //arrange
        val formatter = SimpleDateFormat("dd-MM-yyyy")

        val dailyActivityEntryOne = DailyActivity.Builder()
                .date(formatter.parse("08-05-2015"))
                .caloriesIntake(1934)
                .floors(0)
                .steps(905)
                .distance(0.65f)
                .minutesSitting(1355)
                .minutesSlowActivity(46)
                .minutesModerateActivity(0)
                .minutesIntenseActivity(0)
                .caloriesBurned(168)
                .build()

        val dailyActivityEntryTwo = DailyActivity.Builder()
                .date(formatter.parse("07-05-2015"))
                .caloriesIntake(1934)
                .floors(0)
                .steps(905)
                .distance(0.65f)
                .minutesSitting(1355)
                .minutesSlowActivity(46)
                .minutesModerateActivity(0)
                .minutesIntenseActivity(0)
                .caloriesBurned(168)
                .build()

        val endDate = formatter.parse("08-05-2015")
        val startDate = formatter.parse("08-03-2015")
        doReturn(dailyActivityEntryOne).`when`(dailyActivityRepository).findFirstByOrderByDateDesc()
        doReturn(listOf(dailyActivityEntryOne, dailyActivityEntryTwo)).`when`(dailyActivityRepository).findByDateBetween(startDate, endDate)

        //act
        val activityStatistics = statisticsService.getActivityStatistics()

        //assert
//        val timeSeries = activityStatistics.timeSeries
//        Assertions.assertEquals(2, timeSeries.size, "number of entries is as expected")
//        timeSeries.get("date")

    }

}
