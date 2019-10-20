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
 * Class for Daily activity com.quin.assignment.model.
 */
@Entity
data class DailyActivity(

        @Id
        @CsvCustomBindByName(column = DATE, required = true, converter = CustomDateConverter::class)
        @Column(name = DATE, nullable = false, unique = true)
        @Temporal(TemporalType.DATE)
        var date: Date? = null,

        @CsvCustomBindByName(column = CALORIES, required = true, converter = CustomThousandsConverter::class)
        @Column(name = CALORIES, nullable = false)
        var caloriesIntake: Int? = null,

        @CsvCustomBindByName(column = STEPS, required = true, converter = CustomThousandsConverter::class)
        @Column(name = STEPS, nullable = false)
        var steps: Int? = null,

        @CsvCustomBindByName(column = DISTANCE, required = true, converter = CustomDecimalConverter::class)
        @Column(name = DISTANCE, nullable = false)
        var distance: Float? = null,

        @CsvBindByName(column = FLOORS, required = true)
        @Column(name = FLOORS, nullable = false)
        var floors: Int? = null,

        @CsvCustomBindByName(column = MINUTES_SITTING, required = true, converter = CustomThousandsConverter::class)
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

        @CsvCustomBindByName(column = CALORIES_ACTIVITY, required = true, converter = CustomThousandsConverter::class)
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
    }
}

class CustomDateConverter : AbstractBeanField<Date>() {
    @Throws(CsvDataTypeMismatchException::class, CsvConstraintViolationException::class)
    override fun convert(date: String): Any {
        val formatter = SimpleDateFormat("dd-mm-yyyy")
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