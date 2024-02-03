package com.angiuprojects.dispensav2.activities.implementation

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageButton
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityMainBinding
import com.angiuprojects.dispensav2.entities.Profile
import com.angiuprojects.dispensav2.enums.ProfileButtonStateEnum
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.ReadWriteJsonUtils
import com.angiuprojects.dispensav2.utilities.Utils

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Constants.profileSettings.profileList.forEach { p -> setColorProfileButton(p, false) }

        setOnClickListeners()

        ExpiringActivity.filterExpiringItems().forEach {
            val dialog = Dialog(this)
            Utils.singleton.createSimpleOKPopUp(it.name + " " + it.expirationDate?.let { it1 ->
                Utils.singleton.setPhrase(it1) }, dialog, this::onClickCloseDialog)
        }
    }

    private fun onClickCloseDialog(dialog: Dialog?) {
        dialog?.dismiss()
    }

    private fun setOnClickListeners() {
        /*
            START MAIN LIST BUTTONS
         */
        binding.storageButton.setOnClickListener { onClickOpenSelectedActivity(StorageActivity::class.java) }
        binding.shoppingListButton.setOnClickListener { onClickOpenSelectedActivity(ShoppingListActivity::class.java) }
        binding.expiringButton.setOnClickListener { onClickOpenSelectedActivity(ExpiringActivity::class.java) }
        binding.mealPlanButton.setOnClickListener { onClickOpenSelectedActivity(MealPlanActivity::class.java) }

        /*
            START LIST YELLOW BUTTONS
         */
        binding.addItemButton.setOnClickListener { onClickOpenSelectedActivity(AddItemActivity::class.java) }
        binding.calculatorButton.setOnClickListener { onClickOpenSelectedActivity(TicketCalculatorActivity::class.java) }
        binding.historyButton.setOnClickListener { onClickOpenSelectedActivity(HistoryActivity::class.java) }

        /*
            START LIST SELECTION BUTTONS
         */
        Constants.profileSettings.profileList.forEach { p -> getImageButtonFromProfile(p)?.setOnClickListener { setColorProfileButton(p, true) } }
    }

    private fun setColorProfileButton(profile: Profile, isClicked: Boolean) {

        val imageButton = getImageButtonFromProfile(profile) ?: return

        if(profile.state == ProfileButtonStateEnum.OFF) {
            if(isClicked) {
                profile.state = ProfileButtonStateEnum.ON
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.yellow, baseContext.theme))
            } else {
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.gray, baseContext.theme))
            }
        } else{
            if(isClicked) {
                profile.state = ProfileButtonStateEnum.OFF
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.gray, baseContext.theme))
            } else {
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.yellow, baseContext.theme))
            }
        }
        ReadWriteJsonUtils.singleton.write(this, ReadWriteJsonUtils.singleton.profileFileName, Constants.profileSettings)
    }

    private fun <T> onClickOpenSelectedActivity(clazz: Class<T>) {
        Utils.singleton.changeActivity(this, clazz)
    }

    private fun getImageButtonFromProfile(p: Profile) : ImageButton? {
        return when(p.image) {
            "BOY" -> binding.boyButton
            "GIRL" -> binding.girlButton
            "BOTH" -> binding.bothButton
            else -> null
        }
    }
}