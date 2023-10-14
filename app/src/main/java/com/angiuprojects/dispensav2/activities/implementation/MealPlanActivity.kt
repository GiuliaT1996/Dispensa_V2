package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityMealPlanBinding
import com.angiuprojects.dispensav2.exceptions.StorageException
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.ReadWriteJsonUtils
import com.angiuprojects.dispensav2.utilities.Utils
import java.time.LocalDate
import java.time.ZoneId

class MealPlanActivity : BaseActivity<ActivityMealPlanBinding>(ActivityMealPlanBinding::inflate) {

    private lateinit var daysOfWeekMap : MutableMap<Long, Triple<String, TextView, EditText?>>
    private lateinit var startDate : LocalDate
    private var weeksOff : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        daysOfWeekMap = mutableMapOf(
            0L to Triple("Lunedì", binding.monday, binding.mondayMeal.editText),
            1L to Triple("Martedì", binding.tuesday, binding.tuesdayMeal.editText),
            2L to Triple("Mercoledì", binding.wednesday, binding.wednesdayMeal.editText),
            3L to Triple("Giovedì", binding.thursday, binding.thursdayMeal.editText),
            4L to Triple("Venerdì", binding.friday, binding.fridayMeal.editText),
            5L to Triple("Sabato", binding.saturday, binding.saturdayMeal.editText),
            6L to Triple("Domenica", binding.sunday, binding.sundayMeal.editText))

        setHeaderListener(binding.header, R.string.meal_plan_txt, this,
            isFilterPresent = false,
            isSearchPresent = false
        )

        startDate = LocalDate.now(ZoneId.of("Europe/Rome"))
        updateMap()
        updateMealPlans(0, isClosing = false)
        //standard view
        setCurrentWeekDays(isRight = false, isLeft = false)
        binding.left.setOnClickListener {
            if(checkIfRightOrLeft(isRight = false, isLeft = true)) {
                updateMealPlans(-1, isClosing = false)
                setCurrentWeekDays(isRight = false, isLeft = true)
            }
        }
        binding.right.setOnClickListener {
            if(checkIfRightOrLeft(isRight = true, isLeft = false)) {
                updateMealPlans(1, isClosing = false)
                setCurrentWeekDays(isRight = true, isLeft = false)
            }
        }

        binding.header.backButton.setOnClickListener {
            updateMealPlans(0, isClosing = true)
            finish()
        }
    }

    private fun checkIfRightOrLeft(isRight: Boolean, isLeft: Boolean) : Boolean {
        val snackBarView : View = findViewById(android.R.id.content)

        if(isRight && isLeft)
            throw StorageException("Non possono essere premuti contemporaneamente due pulsanti!")
        else if(weeksOff >= 2 && isRight || weeksOff <= -1 && isLeft) {
            Utils.singleton.openSnackBar(
                snackBarView,
                "Non è possibile salvare più di quattro settimane!"
            )
            return false
        }
        return true
    }

    private fun setCurrentWeekDays(isRight: Boolean, isLeft: Boolean)  {
        startDate = startDate.minusDays((startDate.dayOfWeek.value - 1).toLong())
        startDate =
            if(isRight) {
                weeksOff += 1
                startDate.plusDays(7)
            }
            else if(isLeft) {
                weeksOff -= 1
                startDate.minusDays(7)
            }
            else startDate.plusDays(0)

        var tempDate = startDate

        daysOfWeekMap.forEach {
            it.value.second.text = String.format("%s %s", it.value.first, tempDate.dayOfMonth)
            tempDate = tempDate.plusDays(1L)
        }
    }

    private fun updateMap() {
        var currentDate = LocalDate.now(ZoneId.of("Europe/Rome"))
        currentDate = currentDate.minusDays((startDate.dayOfWeek.value - 1).toLong())
        val startDate = currentDate.minusDays(7)

        val olderKeys = Constants.mealPlan.weekMap.filterKeys { startDate > it }.keys
        olderKeys.forEach { Constants.mealPlan.weekMap.remove(it) }

    }

    private fun updateMealPlans(isLeftOrRight: Int, isClosing: Boolean) {
        startDate = startDate.minusDays((startDate.dayOfWeek.value - 1).toLong())

        daysOfWeekMap.forEach { (daysToAdd, triple) ->
            if((isLeftOrRight != 0 || isClosing) && triple.third?.text.toString().isNotEmpty())
                Constants.mealPlan.weekMap[startDate.plusDays(daysToAdd)] = triple.third?.text.toString()
            val newMeal = if(Constants.mealPlan.weekMap[startDate.plusDays(daysToAdd + 7 * isLeftOrRight)] != null)
                Constants.mealPlan.weekMap[startDate.plusDays(daysToAdd + 7 * isLeftOrRight)]
                          else ""
            triple.third?.setText(newMeal)
        }

        ReadWriteJsonUtils.singleton.write(this, ReadWriteJsonUtils.singleton.mealPlanFileName, Constants.mealPlan)
    }
}