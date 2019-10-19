package model

import java.util.Date
import java.text.SimpleDateFormat

class DailyActivity(
        var date: Date = Date(),
        var caloriesIntake: Int = -1,
        var steps: Int = -1,
        var distance: Float? = null,
        var floors: Int = -1,
        var minutesSitting: Int = -1,
        var minutesModerateActivity: Int = -1,
        var minutesIntenseActivity: Int = -1,
        var caloriesBurned: Int = -1
        ) {


    companion object  {
        var formatter = SimpleDateFormat("dd-MMM-yyyy")
        fun mapper(tokens: Array<String>?) : DailyActivity {
            var dailyActivity = DailyActivity()
            dailyActivity.date = formatter.parse(tokens?.get(0))
            dailyActivity.caloriesIntake = Integer.parseInt(tokens?.get(1))
            dailyActivity.steps =  Integer.parseInt(tokens?.get(2))
            return dailyActivity;
        }

        fun parseThousands(){

        }

    }
}