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
import java.util.stream.IntStream

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
        assertEquals(dailyActivityEntryOne.date, date?.get(0), "date is expected for entry one")
        assertEquals(dailyActivityEntryTwo.date, date?.get(1), "date is expected for entry two")

        val steps = timeSeries["steps"]
        assertEquals(2, steps?.size, "number of steps entries is as expected")
        assertEquals(dailyActivityEntryOne.steps, steps?.get(0), "steps is expected for entry one")
        assertEquals(dailyActivityEntryTwo.steps, steps?.get(1), "steps is expected for entry two")

        val calories = timeSeries["calories"]
        assertEquals(2, calories?.size, "number of calories entries is as expected")
        assertEquals(dailyActivityEntryOne.caloriesBurned, calories?.get(0), "calories is expected for entry one")
        assertEquals(dailyActivityEntryTwo.caloriesBurned, calories?.get(1), "calories is expected for entry two")
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
                .caloriesIntake(2000)
                .floors(0)
                .steps(905)
                .distance(0.65f)
                .minutesSitting(1355)
                .minutesSlowActivity(46)
                .minutesModerateActivity(0)
                .minutesIntenseActivity(0)
                .caloriesBurned(1000)
                .build()

        val dailyActivityEntryTwo = DailyActivity.Builder()
                .date(formatter.parse("07-05-2015"))
                .caloriesIntake(3000)
                .floors(0)
                .steps(905)
                .distance(0.65f)
                .minutesSitting(1355)
                .minutesSlowActivity(46)
                .minutesModerateActivity(0)
                .minutesIntenseActivity(0)
                .caloriesBurned(1500)
                .build()

        val endDate = formatter.parse("08-05-2015")
        val startDate = formatter.parse("01-05-2015")
        doReturn(dailyActivityEntryOne).`when`(dailyActivityRepository).findFirstByOrderByDateDesc()
        doReturn(listOf(dailyActivityEntryOne, dailyActivityEntryTwo)).`when`(dailyActivityRepository).findByDateBetween(startDate, endDate)

        //act
        val activityStatistics = statisticsService.getActivityStatistics()

        //assert
        var weeklyStatistics = activityStatistics.weeklyStatistics
        assertEquals(4, weeklyStatistics.size, "number of statics is as expected")

        val date = weeklyStatistics["finalDate"]
        assertEquals(12, date?.size, "number of week entries is as expected")
        assertEquals(endDate, date?.get(0), "date is expected for entry one")

        val caloriesBurned = weeklyStatistics["caloriesBurned"]
        assertEquals(12, caloriesBurned?.size, "number of weekly entries is as expected")
        assertEquals(1250, caloriesBurned?.get(0), "average burned calories is as expected")

        val caloriesIntake = weeklyStatistics["caloriesIntake"]
        assertEquals(12, caloriesIntake?.size, "number of weekly entries is as expected")
        assertEquals(2500, caloriesIntake?.get(0), "average intake calories is as expected")

        val numberOfDays = weeklyStatistics["numberOfDays"]
        assertEquals(12, numberOfDays?.size, "number of weekly entries is as expected")
        assertEquals(2, numberOfDays?.get(0), "number of days used for average is as expected")
    }

    @Test
    fun getActivityStatistics_nodDataPresent_emptyStatisticReturned() {
        //arrange
        val formatter = SimpleDateFormat("dd-MM-yyyy")

        val dates = listOf(formatter.parse("08-05-2015"),
                formatter.parse("01-05-2015"),
                formatter.parse("24-04-2015"),
                formatter.parse("17-04-2015"),
                formatter.parse("10-04-2015"),
                formatter.parse("03-04-2015"),
                formatter.parse("27-03-2015"),
                formatter.parse("20-03-2015"),
                formatter.parse("13-03-2015"),
                formatter.parse("06-03-2015"),
                formatter.parse("27-02-2015"),
                formatter.parse("20-02-2015")
        )

        val dailyActivityEntryOne = DailyActivity.Builder()
                .date(formatter.parse("08-05-2015"))
                .caloriesIntake(2000)
                .floors(0)
                .steps(905)
                .distance(0.65f)
                .minutesSitting(1355)
                .minutesSlowActivity(46)
                .minutesModerateActivity(0)
                .minutesIntenseActivity(0)
                .caloriesBurned(1000)
                .build()

        doReturn(dailyActivityEntryOne).`when`(dailyActivityRepository).findFirstByOrderByDateDesc()

        //act
        val activityStatistics = statisticsService.getActivityStatistics()

        //assert
        var weeklyStatistics = activityStatistics.weeklyStatistics
        assertEquals(4, weeklyStatistics.size, "number of statics is as expected")

        val date = weeklyStatistics["finalDate"]
        assertEquals(12, date?.size, "number of week entries is as expected")

        IntStream.range(0, 12).forEach { i ->
            assertEquals(dates[i], date?.get(i), "date $i is expected for entry one")
        }

        val caloriesBurned = weeklyStatistics["caloriesBurned"]
        assertEquals(12, caloriesBurned?.size, "number of weekly entries is as expected")
        caloriesBurned!!.stream().forEach { x ->
            assertEquals(0, x, "calories burned should be zero")
        }

        val caloriesIntake = weeklyStatistics["caloriesIntake"]
        assertEquals(12, caloriesIntake?.size, "number of weekly entries is as expected")
        caloriesIntake!!.stream().forEach { x ->
            assertEquals(0, x, "calories intake should be zero")
        }

        val numberOfDays = weeklyStatistics["numberOfDays"]
        assertEquals(12, numberOfDays?.size, "number of weekly entries is as expected")
        caloriesIntake!!.stream().forEach { x ->
            assertEquals(0, x, "number of days averaged on should be zero")
        }
    }

}
