package com.quin.assignment.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.quin.assignment.model.DailyActivity
import com.quin.assignment.repository.DailyActivityRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.text.SimpleDateFormat

/**
 * test class for DailyActivityService class.
 */

internal class DailyActivityServiceTest {

    private val dailyActivityRepository: DailyActivityRepository = mock()
    private val dailyActivityService: DailyActivityService = DailyActivityService(dailyActivityRepository)

    @Test
    fun process_entryCorrect_entryProcessed() {

        //arrange
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        val numberOfRecords = 1
        val content = "Date,Calories,Steps,Distance,floors,Minutes_sitting,Minutes_of_slow_activity,Minutes_of_moderate_activity,Minutes_of_intense_activity,Calories_Activity\n" +
                "\"08-05-2015\",\"1.934\",\"905\",\"0,65\",\"0\",\"1.355\",\"46\",\"0\",\"0\",\"168\""

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

        //act
        dailyActivityService.process(content)

        //assert
        argumentCaptor<DailyActivity>().apply {
            verify(dailyActivityRepository).save(capture())
            assertEquals(numberOfRecords, allValues.size, "number of records is a expected")
            assertEquals(dailyActivityEntryOne.date, firstValue.date, "date is parsed as expected")
            assertEquals(dailyActivityEntryOne.caloriesIntake, firstValue.caloriesIntake, "calories intake is parsed as expected")
            assertEquals(dailyActivityEntryOne.steps, firstValue.steps, "steps is parsed as expected")
            assertEquals(dailyActivityEntryOne.floors, firstValue.floors, "floors is parsed as expected")
            assertEquals(dailyActivityEntryOne.distance, firstValue.distance, "distance is parsed as expected")
            assertEquals(dailyActivityEntryOne.minutesSitting, firstValue.minutesSitting, "minutes sitting is parsed as expected")
            assertEquals(dailyActivityEntryOne.minutesSlowActivity, firstValue.minutesSlowActivity, "minutes slow activity is parsed as expected")
            assertEquals(dailyActivityEntryOne.minutesIntenseActivity, firstValue.minutesIntenseActivity, "minutes intense activity is parsed as expected")
            assertEquals(dailyActivityEntryOne.minutesModerateActivity, firstValue.minutesModerateActivity, "minutes moderate activity is parsed as expected")
            assertEquals(dailyActivityEntryOne.caloriesBurned, firstValue.caloriesBurned, "calires burned is parsed as expected")
        }
    }

    @Test
    fun process_fileCorrect_fileProcessed() {

        //arrange
        val content = "Date,Calories,Steps,Distance,floors,Minutes_sitting,Minutes_of_slow_activity,Minutes_of_moderate_activity,Minutes_of_intense_activity,Calories_Activity\n" +
                "\"08-05-2015\",\"1.934\",\"905\",\"0,65\",\"0\",\"1.355\",\"46\",\"0\",\"0\",\"168\"\n" +
                "\"09-05-2015\",\"3.631\",\"18.925\",\"14,11\",\"4\",\"611\",\"316\",\"61\",\"60\",\"2.248\""

        //act
        dailyActivityService.process(content)

        //assert
        verify(dailyActivityRepository, times(2)).save(any<DailyActivity>())
    }

    @Test
    fun getData_dataReturned() {
        //arrange
        val pageNumber = 0
        val pageSize = 1000

        var page = PageImpl<DailyActivity>(listOf(DailyActivity.Builder().build(), DailyActivity.Builder().build()))
        doReturn(page)
                .`when`(dailyActivityRepository)
                .findAllByOrderByDateDesc(PageRequest.of(pageNumber, pageSize))

        //act
        val data = dailyActivityService.getData(pageNumber, pageSize)

        //assert
        argumentCaptor<PageRequest>().apply {
            verify(dailyActivityRepository).findAllByOrderByDateDesc(capture())
            assertEquals(pageNumber, firstValue.pageNumber, "page number is as expected")
            assertEquals(pageSize, firstValue.pageSize, "page size is as expected")
        }

        assertEquals(2, data.size, "size of data is as expected")
    }
}