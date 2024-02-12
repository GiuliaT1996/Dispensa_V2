package com.angiuprojects.dispensav2.activities.implementation

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.ImageButton
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityMainBinding
import com.angiuprojects.dispensav2.enums.ProfileButtonStateEnum
import com.angiuprojects.dispensav2.enums.ProfileEnum
import com.angiuprojects.dispensav2.queries.Queries
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.ReadWriteJsonUtils
import com.angiuprojects.dispensav2.utilities.Utils
import kotlinx.coroutines.runBlocking

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Constants.appIsStarting = false

        setColorProfileButton(binding.boyButton, ProfileEnum.ANTONIO, false)
        setColorProfileButton(binding.girlButton, ProfileEnum.GIULIA, false)
        setColorProfileButton(binding.bothButton, ProfileEnum.COMUNI, false)

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

        binding.refreshButton.setOnClickListener { onClickRefresh() }

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
        binding.boyButton.setOnClickListener { setColorProfileButton(binding.boyButton, ProfileEnum.ANTONIO, true) }
        binding.girlButton.setOnClickListener { setColorProfileButton(binding.girlButton, ProfileEnum.GIULIA, true) }
        binding.bothButton.setOnClickListener { setColorProfileButton(binding.bothButton, ProfileEnum.COMUNI, true) }
    }

    private fun onClickRefresh() = runBlocking {
        Constants.appIsStarting = true
        Queries.singleton.getStorageItems()
        showLoadingGif()
    }

    private fun showLoadingGif() {
        val dialog = Dialog(this)
        Utils.singleton.createLoadingPopUp(dialog)
        object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() { dialog.dismiss() }
        }.start()
        Constants.appIsStarting = false
    }

    private fun setColorProfileButton(imageButton: ImageButton, profile: ProfileEnum, isClicked: Boolean) {
        if(Constants.profileSettings.profileMap[profile]?.equals(ProfileButtonStateEnum.OFF) == true) {
            if(isClicked) {
                Constants.profileSettings.profileMap[profile] = ProfileButtonStateEnum.ON
                ReadWriteJsonUtils.singleton.write(this, ReadWriteJsonUtils.singleton.profileFileName, Constants.profileSettings)
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.yellow, baseContext.theme))
            } else {
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.gray, baseContext.theme))
            }
        } else{
            if(isClicked) {
                Constants.profileSettings.profileMap[profile] = ProfileButtonStateEnum.OFF
                ReadWriteJsonUtils.singleton.write(this, ReadWriteJsonUtils.singleton.profileFileName, Constants.profileSettings)
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.gray, baseContext.theme))
            } else {
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.yellow, baseContext.theme))
            }
        }

        Utils.singleton.refreshProfileList()
    }

    private fun <T> onClickOpenSelectedActivity(clazz: Class<T>) {
        Utils.singleton.changeActivity(this, clazz)
    }
}