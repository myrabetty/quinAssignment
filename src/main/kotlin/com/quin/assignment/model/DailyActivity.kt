package com.quin.assignment.model

import com.opencsv.bean.AbstractBeanField
import com.opencsv.bean.CsvBindByName
import com.opencsv.bean.CsvCustomBindByName
import com.opencsv.exceptions.CsvConstraintViolationException
import com.opencsv.exceptions.CsvDataTypeMismatchException
import java.text.SimpleDateFormat
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Temporal
import javax.persistence.TemporalType

/**
 * Class for Daily activity.
 */
@Entity
class DailyActivity constructor(

        @Id
        @CsvCustomBindByName(column = DATE, required = true, converter = CustomDateConverter::class)
        @Column(name = DATE, nullable = false, unique = true)
        @Temporal(TemporalType.DATE)
        var date: Date?,

        @CsvCustomBindByName(column = CALORIES, required = true, converter = CustomThousandsConverter::class)
        @Column(name = CALORIES, nullable = false)
        var caloriesIntake: Int?,

        @CsvCustomBindByName(column = STEPS, required = true, converter = CustomThousandsConverter::class)
        @Column(name = STEPS, nullable = false)
        var steps: Int?,

        @CsvCustomBindByName(column = DISTANCE, required = true, converter = CustomDecimalConverter::class)
        @Column(name = DISTANCE, nullable = false)
        var distance: Float?,

        @CsvBindByName(column = FLOORS, required = true)
        @Column(name = FLOORS, nullable = false)
        var floors: Int?,

        @CsvCustomBindByName(column = MINUTES_SITTING, required = true, converter = CustomThousandsConverter::class)
        @Column(name = MINUTES_SITTING, nullable = false)
        var minutesSitting: Int?,

        @CsvBindByName(column = MINUTES_SLOW_ACTIVITY, required = true)
        @Column(name = MINUTES_SLOW_ACTIVITY, nullable = false)
        var minutesSlowActivity: Int?,

        @CsvBindByName(column = MINUTES_MODERATE_ACTIVITY, required = true)
        @Column(name = MINUTES_MODERATE_ACTIVITY, nullable = false)
        var minutesModerateActivity: Int?,

        @CsvBindByName(column = MINUTES_INTENSE_ACTIVITY, required = true)
        @Column(name = MINUTES_INTENSE_ACTIVITY, nullable = false)
        var minutesIntenseActivity: Int?,

        @CsvCustomBindByName(column = CALORIES_ACTIVITY, required = true, converter = CustomThousandsConverter::class)
        @Column(name = CALORIES_ACTIVITY, nullable = false)
        var caloriesBurned: Int?
) {

    data class Builder(
            var date: Date? = null,
            var caloriesIntake: Int? = null,
            var steps: Int? = null,
            var distance: Float? = null,
            var floors: Int? = null,
            var minutesSitting: Int? = null,
            var minutesSlowActivity: Int? = null,
            var minutesModerateActivity: Int? = null,
            var minutesIntenseActivity: Int? = null,
            var caloriesBurned: Int? = null) {

        fun date(date: Date) = apply { this.date = date }
        fun caloriesIntake(caloriesIntake: Int) = apply { this.caloriesIntake = caloriesIntake }
        fun steps(steps: Int) = apply { this.steps = steps }
        fun distance(distance: Float) = apply { this.distance = distance }
        fun floors(floors: Int) = apply { this.floors = floors }
        fun minutesSitting(minutesSitting: Int) = apply { this.minutesSitting = minutesSitting }
        fun minutesSlowActivity(minutesSlowActivity: Int) = apply { this.minutesSlowActivity = minutesSlowActivity }
        fun minutesModerateActivity(minutesModerateActivity: Int) = apply { this.minutesModerateActivity = minutesModerateActivity }
        fun minutesIntenseActivity(minutesIntenseActivity: Int) = apply { this.minutesIntenseActivity = minutesIntenseActivity }
        fun caloriesBurned(caloriesBurned: Int) = apply { this.caloriesBurned = caloriesBurned }
        fun build() = DailyActivity(date, caloriesIntake, steps, distance, floors, minutesSitting, minutesSlowActivity, minutesModerateActivity, minutesIntenseActivity, caloriesBurned)

    }

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
    }
}

class CustomDateConverter : AbstractBeanField<Date>() {
    @Throws(CsvDataTypeMismatchException::class, CsvConstraintViolationException::class)
    override fun convert(date: String): Any {
        val formatter = SimpleDateFormat("dd-MM-yyyy")
        return formatter.parse(date)
    }
}

class CustomThousandsConverter : AbstractBeanField<Int>() {
    @Throws(CsvDataTypeMismatchException::class, CsvConstraintViolationException::class)
    override fun convert(number: String): Any {
        return number.replace(".", "").toInt()
    }
}

class CustomDecimalConverter : AbstractBeanField<Float>() {
    @Throws(CsvDataTypeMismatchException::class, CsvConstraintViolationException::class)
    override fun convert(number: String): Any {
        return number.replace(",", ".").toFloat()
    }
}