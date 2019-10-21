package com.quin.assignment.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.quin.assignment.model.DailyActivity
import com.quin.assignment.repository.DailyActivityRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat

internal class StatisticsServiceTest {

    private val dailyActivityRepository: DailyActivityRepository = mock()
    private val statisticsService = StatisticsService(dailyActivityRepository)

    @Test
    fun getActivityStatistics_dataPresent_timeSeriesReturned() {
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
        val timeSeries = activityStatistics.timeSeries
        assertEquals(3, timeSeries.size, "number of statics is as expected")

        val date = timeSeries["date"]
        assertEquals(2, date?.size, "number of date entries is as expected")
        assertEquals(dailyActivityEntryOne.date, date?.get(0),"date is expected for entry one")
        assertEquals(dailyActivityEntryTwo.date, date?.get(1),"date is expected for entry two")

        val steps = timeSeries["steps"]
        assertEquals(2, steps?.size, "number of steps entries is as expected")
        assertEquals(dailyActivityEntryOne.steps, steps?.get(0),"steps is expected for entry one")
        assertEquals(dailyActivityEntryTwo.steps, steps?.get(1),"steps is expected for entry two")

        val calories = timeSeries["calories"]
        assertEquals(2, calories?.size, "number of calories entries is as expected")
        assertEquals(dailyActivityEntryOne.caloriesBurned, calories?.get(0),"calories is expected for entry one")
        assertEquals(dailyActivityEntryTwo.caloriesBurned, calories?.get(1),"calories is expected for entry two")
    }

    @Test
    fun getActivityStatistics_noLatestEntryFound_nullReturned() {
        //arrange
        doReturn(null).`when`(dailyActivityRepository).findFirstByOrderByDateDesc()

        //act
        statisticsService.getActivityStatistics()

        //assert
        verify(dailyActivityRepository, never()).findByDateBetween(any(), any())
    }
    @Test
    fun getActivityStatistics_dataPresent_weeklyStatisticsReturned() {
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
//        val weeklyStatistics = activityStatistics.weeklyStatistics
//        assertEquals(3, timeSeries.size, "number of statics is as expected")
//
//        val date = timeSeries["date"]
//        assertEquals(2, date?.size, "number of date entries is as expected")
//        assertEquals(dailyActivityEntryOne.date, date?.get(0),"date is expected for entry one")
//        assertEquals(dailyActivityEntryTwo.date, date?.get(1),"date is expected for entry two")
//
//        val steps = timeSeries["steps"]
//        assertEquals(2, steps?.size, "number of steps entries is as expected")
//        assertEquals(dailyActivityEntryOne.steps, steps?.get(0),"steps is expected for entry one")
//        assertEquals(dailyActivityEntryTwo.steps, steps?.get(1),"steps is expected for entry two")
//
//        val calories = timeSeries["calories"]
//        assertEquals(2, calories?.size, "number of calories entries is as expected")
//        assertEquals(dailyActivityEntryOne.caloriesBurned, calories?.get(0),"calories is expected for entry one")
//        assertEquals(dailyActivityEntryTwo.caloriesBurned, calories?.get(1),"calories is expected for entry two")
    }
}
