package model

import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvDate
import com.opencsv.bean.CsvNumber
import java.util.Date
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

/**
 * Class for Daily activity model.
 */
@Entity
class DailyActivity(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)

        @CsvBindByName(column = DATE, required = true)
        @CsvDate("dd-mm-yyyy")
        @Column(name = DATE, nullable = false)
        var date: Date? = null,

        @CsvBindByName(column = CALORIES, required = true)
        @CsvNumber("#.###")
        @Column(name = CALORIES, nullable = false)
        var caloriesIntake: Int? = null,

        @CsvBindByName(column = STEPS, required = true)
        @CsvNumber(THOUSANDS)
        @Column(name = STEPS, nullable = false)
        var steps: Int? = null,

        @CsvBindByName(column = DISTANCE, required = true)
        @CsvNumber("#,##")
        @Column(name = DISTANCE, nullable = false)
        var distance: Float? = null,

        @CsvBindByName(column = "floors", required = true)
        @Column(name = FLOORS, nullable = false)
        var floors: Int? = null,

        @CsvBindByName(column = MINUTES_SITTING, required = true)
        @CsvNumber(THOUSANDS)
        @Column(name = MINUTES_SITTING, nullable = false)
        var minutesSitting: Int? = null,

        @CsvBindByName(column = MINUTES_SLOW_ACTIVITY, required = true)
        @Column(name = MINUTES_SLOW_ACTIVITY, nullable = false)
        var minutesSlowActivity: Int? = null,

        @CsvBindByName(column = MINUTES_MODERATE_ACTIVITY, required = true)
        @Column(name = MINUTES_MODERATE_ACTIVITY, nullable = false)
        var minutesModerateActivity: Int? = null,

        @CsvBindByName(column = MINUTES_INTENSE_ACTIVITY, required = true)
        @Column(name = MINUTES_INTENSE_ACTIVITY, nullable = false)
        var minutesIntenseActivity: Int? = null,

        @CsvBindByName(column = CALORIES_ACTIVITY, required = true)
        @CsvNumber(THOUSANDS)
        @Column(name = CALORIES_ACTIVITY, nullable = false)
        var caloriesBurned: Int? = null
) {

    companion object {
        const val DATE = "date"
        const val CALORIES = "Calories"
        const val STEPS = "Steps"
        const val DISTANCE = "Distance"
        const val FLOORS = "floors"
        const val MINUTES_SITTING = "Minutes_sitting"
        const val MINUTES_SLOW_ACTIVITY = "Minutes_of_slow_activity "
        const val MINUTES_MODERATE_ACTIVITY = "Minutes_of_moderate_activity"
        const val MINUTES_INTENSE_ACTIVITY = "Minutes_of_intense_activity"
        const val CALORIES_ACTIVITY = "Calories_Activity"
        const val THOUSANDS = "#.###"
    }
}