package model

import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvDate
import com.opencsv.bean.CsvNumber
import java.util.Date

/**
 * Class for Daily activity model.
 */
class DailyActivity(
        @CsvBindByName(column = "Date", required = true)
        @CsvDate("dd-mm-yyyy")
        var date: Date? = null,
        @CsvBindByName(column = "Calories", required = true)
        @CsvNumber("#.###")
        var caloriesIntake: Int? =null,
        @CsvBindByName(column = "Steps", required = true)
        @CsvNumber("#.###")
        var steps: Int? = null,
        @CsvBindByName(column = "Distance", required = true)
        @CsvNumber("#,##")
        var distance: Float? = null,
        @CsvBindByName(column = "floors", required = true)
        var floors: Int? = null,
        @CsvBindByName(column = "Minutes_sitting", required = true)
        @CsvNumber("#.###")
        var minutesSitting: Int? = null,
        @CsvBindByName(column = "Minutes_of_slow_activity", required = true)
        var minutesSlowActivity: Int? = null,
        @CsvBindByName(column = "Minutes_of_moderate_activity", required = true)
        var minutesModerateActivity: Int? = null,
        @CsvBindByName(column = "Minutes_of_intense_activity", required = true)
        var minutesIntenseActivity: Int? = null,
        @CsvBindByName(column = "Calories_Activity", required = true)
        @CsvNumber("#.###")
        var caloriesBurned: Int? = null
        )