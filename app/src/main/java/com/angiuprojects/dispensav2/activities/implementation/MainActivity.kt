package com.angiuprojects.dispensav2.activities.implementation

import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityMainBinding
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.enums.ProfileButtonStateEnum
import com.angiuprojects.dispensav2.enums.ProfileEnum
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.ReadWriteJsonUtils
import com.angiuprojects.dispensav2.utilities.Utils

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setColorProfileButton(binding.boyButton, ProfileEnum.ANTONIO, false)
        setColorProfileButton(binding.girlButton, ProfileEnum.GIULIA, false)
        setColorProfileButton(binding.bothButton, ProfileEnum.COMUNI, false)

        Constants.itemMap.forEach { s -> Log.i(Constants.STORAGE_LOGGER, s.toString() + "\n") }
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

        val filteredMap = Utils.singleton.filterItemMap(Constants.itemMap,StorageItem::profile, profile.formattedName)

        if(Constants.profileSettings.profileMap[profile]?.equals(ProfileButtonStateEnum.OFF) == true) {
            if(isClicked) {
                Constants.profileSettings.profileMap[profile] = ProfileButtonStateEnum.ON
                ReadWriteJsonUtils.singleton.write(this)
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.yellow, baseContext.theme))
                Constants.itemMapFilteredByProfile.putAll(filteredMap)
            } else {
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.gray, baseContext.theme))
            }
        } else{
            if(isClicked) {
                Constants.profileSettings.profileMap[profile] = ProfileButtonStateEnum.OFF
                ReadWriteJsonUtils.singleton.write(this)
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.gray, baseContext.theme))
                Constants.itemMapFilteredByProfile.entries.removeIf{ it.value.profile == profile.formattedName }
            } else {
                imageButton.backgroundTintList =
                    ColorStateList.valueOf(resources.getColor(R.color.yellow, baseContext.theme))
                Constants.itemMapFilteredByProfile.putAll(filteredMap)
            }
        }
    }

    private fun <T> onClickOpenSelectedActivity(clazz: Class<T>) {
        Utils.singleton.changeActivity(this, clazz)
    }
}