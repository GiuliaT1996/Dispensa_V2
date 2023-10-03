package com.angiuprojects.dispensav2.activities.implementation

import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageButton
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityMainBinding
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.ProfileButtonStateEnum
import com.angiuprojects.dispensav2.utilities.ProfileEnum
import com.angiuprojects.dispensav2.utilities.Utils

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorProfileButton(binding.boyButton, ProfileEnum.ANTONIO, false)
        setColorProfileButton(binding.girlButton, ProfileEnum.GIULIA, false)
        setColorProfileButton(binding.bothButton, ProfileEnum.COMUNI, false)

        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        /*
            START MAIN LIST BUTTONS
         */
        binding.storageButton.setOnClickListener { onClickOpenSelectedActivity(StorageActivity::class.java) }
        binding.shoppingListButton.setOnClickListener { onClickOpenSelectedActivity(ShoppingListActivity::class.java) }
        binding.expiringButton.setOnClickListener { onClickOpenSelectedActivity(ExpiringActivity::class.java) }

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

    private fun setColorProfileButton(imageButton: ImageButton, profile: ProfileEnum, isClicked: Boolean) {

        if(Constants.getInstance().getProfileMap()[profile]?.equals(ProfileButtonStateEnum.OFF) == true) {
            if(isClicked) {
                Constants.getInstance().getProfileMap()[profile] = ProfileButtonStateEnum.ON
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.yellow, baseContext.theme))
            } else {
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.gray, baseContext.theme))
            }
        } else{
            if(isClicked) {
                Constants.getInstance().getProfileMap()[profile] = ProfileButtonStateEnum.OFF
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.gray, baseContext.theme))
            } else {
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.yellow, baseContext.theme))
            }
        }
    }

    private fun <T> onClickOpenSelectedActivity(clazz: Class<T>) {
        Utils.getInstance().changeActivity(this, clazz)
    }
}