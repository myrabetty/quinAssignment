package com.quin.assignment.service

import com.quin.assignment.model.DailyActivity
import com.quin.assignment.repository.DailyActivityRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.text.SimpleDateFormat

/**
 * test class for FileService class.
 */

internal class FileServiceTest {

    private val dailyActivityRepository: DailyActivityRepository = Mockito.mock(DailyActivityRepository::class.java)
    private val fileService: FileService = FileService(dailyActivityRepository)
    private val formatter = SimpleDateFormat("dd-mm-yyyy")
    private val captor = ArgumentCaptor.forClass(DailyActivity::class.java)

    @Test
    fun process_entryCorrect_entryProcessed() {

        //arrange
        val numberOfRecords = 1
        val content = "Date,Calories,Steps,Distance,floors,Minutes_sitting,Minutes_of_slow_activity,Minutes_of_moderate_activity,Minutes_of_intense_activity,Calories_Activity\n" +
                "\"08-05-2015\",\"1.934\",\"905\",\"0,65\",\"0\",\"1.355\",\"46\",\"0\",\"0\",\"168\""

        val dailyActivityEntryOne = DailyActivity()
        dailyActivityEntryOne.date = formatter.parse("08-05-2015")
        dailyActivityEntryOne.caloriesIntake = 1934
        dailyActivityEntryOne.floors = 0
        dailyActivityEntryOne.steps = 905
        dailyActivityEntryOne.distance = 0.65f
        dailyActivityEntryOne.minutesSitting = 1355
        dailyActivityEntryOne.minutesSlowActivity = 46
        dailyActivityEntryOne.minutesModerateActivity = 0
        dailyActivityEntryOne.minutesIntenseActivity = 0
        dailyActivityEntryOne.caloriesBurned = 168

        //act
        fileService.process(content)

        //assert
        verify(dailyActivityRepository).save(captor.capture())
        val result = captor.allValues
        assertEquals(numberOfRecords, result.size, "number of records is a expected")
        assertEquals(dailyActivityEntryOne.date, result[0].date, "date is parsed as expected")
        assertEquals(dailyActivityEntryOne.caloriesIntake!!, result[0].caloriesIntake, "calories intake is parsed as expected")
        assertEquals(dailyActivityEntryOne.steps!!, result[0].steps, "steps is parsed as expected")
        assertEquals(dailyActivityEntryOne.floors!!, result[0].floors, "floors is parsed as expected")
        assertEquals(dailyActivityEntryOne.distance!!, result[0].distance, "distance is parsed as expected")
        assertEquals(dailyActivityEntryOne.minutesSitting!!, result[0].minutesSitting, "minutes sitting is parsed as expected")
        assertEquals(dailyActivityEntryOne.minutesSlowActivity!!, result[0].minutesSlowActivity, "minutes slow activity is parsed as expected")
        assertEquals(dailyActivityEntryOne.minutesIntenseActivity!!, result[0].minutesIntenseActivity, "minutes intense activity is parsed as expected")
        assertEquals(dailyActivityEntryOne.minutesModerateActivity!!, result[0].minutesModerateActivity, "minutes moderate activity is parsed as expected")
        assertEquals(dailyActivityEntryOne.caloriesBurned!!, result[0].caloriesBurned, "calires burned is parsed as expected")
    }

    @Test
    fun process_fileCorrect_fileProcessed() {

        //arrange
        val content = "Date,Calories,Steps,Distance,floors,Minutes_sitting,Minutes_of_slow_activity,Minutes_of_moderate_activity,Minutes_of_intense_activity,Calories_Activity\n" +
                "\"08-05-2015\",\"1.934\",\"905\",\"0,65\",\"0\",\"1.355\",\"46\",\"0\",\"0\",\"168\"\n" +
                "\"09-05-2015\",\"3.631\",\"18.925\",\"14,11\",\"4\",\"611\",\"316\",\"61\",\"60\",\"2.248\""

        //act
        fileService.process(content)

        //assert
        verify(dailyActivityRepository, times(2)).save(any())
    }
}