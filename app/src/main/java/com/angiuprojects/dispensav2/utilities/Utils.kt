package com.angiuprojects.dispensav2.utilities

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.entities.StorageItem
import com.angiuprojects.dispensav2.enums.ProfileButtonStateEnum
import com.google.android.material.snackbar.Snackbar
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.reflect.*

class Utils {

    companion object {
        lateinit var singleton : Utils

        fun initializeUtils() {
            singleton = Utils()
        }
    }

    fun <T> changeActivity(context : Context, clazz: Class<T>) {
        val intent = Intent(context, clazz)
        context.startActivity(intent)
    }

    fun openSnackBar(snackBarView: View, text: String) {
        Snackbar.make(
            snackBarView, text,
            Snackbar.LENGTH_LONG
        ).show()
    }

    fun createNewDropDown(
        spinner: AutoCompleteTextView,
        context: Context,
        drawable: Int,
        suggestionList: MutableList<String>
    ) {
        val adapter = ArrayAdapter(context, R.layout.spinner_dropdown_item_view, suggestionList)
        spinner.setAdapter(adapter)
        spinner.setDropDownBackgroundDrawable(
            ResourcesCompat.getDrawable(
                context.resources,
                drawable,
                context.theme
            )
        )
    }

    fun convertStringToDate(data: String?): Date? {
        return if (data != null) {
            try {
                Constants.DATE_TIME_FORMATTER.parse(data)
            } catch (e: Exception) {
                Log.e(Constants.STORAGE_LOGGER, "Il formato della data non è corretto")
                null
            }
        } else null
    }

    fun convertDateToString(date: Date?) : String {
        return if (date != null) {
            try {
                Constants.DATE_TIME_FORMATTER.format(date)
            } catch (e: Exception) {
                Log.e(Constants.STORAGE_LOGGER, "Il formato della data non è corretto")
                ""
            }
        } else ""
    }

    fun createSimpleOKPopUp(message: String, dialog: Dialog?, onClickOk: KFunction1<Dialog?, Unit>) {
        val popUpView = commonCodePopUp(dialog, com.angiuprojects.dispensav2.R.layout.pop_up_simple_ok)
        popUpView?.findViewById<TextView>(com.angiuprojects.dispensav2.R.id.text)?.text = message
        val okButton = popUpView?.findViewById<Button>(com.angiuprojects.dispensav2.R.id.ok_button)
        okButton?.setOnClickListener { onClickOk.invoke(dialog) }
    }

    //delete expiration date // Method version impossible to generalize - checks exp date
    fun <T : Adapter<V>, V> createYesNoPopUp(message: String, dialog: Dialog?,
                                              onClickDeleteExpDate: KFunction5<T, StorageItem, Int, V, Boolean, Unit>,
                                              invoker: T, s: StorageItem, num: Int, holder: V) {
        val popUpView = commonCodePopUp(dialog, com.angiuprojects.dispensav2.R.layout.pop_up_yes_no)
        popUpView?.findViewById<TextView>(com.angiuprojects.dispensav2.R.id.text)?.text = message
        val yesButton = popUpView?.findViewById<Button>(com.angiuprojects.dispensav2.R.id.yes_button)
        yesButton?.setOnClickListener { onClickDeleteExpDate.invoke(invoker, s, num, holder, true) }
        val noButton = popUpView?.findViewById<Button>(com.angiuprojects.dispensav2.R.id.no_button)
        noButton?.setOnClickListener { onClickDeleteExpDate.invoke(invoker, s, num, holder, false) }
    }

    //delete item
    fun <T : Adapter<V>, V> createYesNoPopUp(message: String, dialog: Dialog?,
                                             onClickYes : KFunction3<T, StorageItem, V, Unit>,
                                             invoker: T, s: StorageItem, holder: V) {
        val popUpView = commonCodePopUp(dialog, com.angiuprojects.dispensav2.R.layout.pop_up_yes_no)
        popUpView?.findViewById<TextView>(com.angiuprojects.dispensav2.R.id.text)?.text = message
        val yesButton = popUpView?.findViewById<Button>(com.angiuprojects.dispensav2.R.id.yes_button)
        yesButton?.setOnClickListener { onClickYes.invoke(invoker, s, holder) }
        val noButton = popUpView?.findViewById<Button>(com.angiuprojects.dispensav2.R.id.no_button)
        noButton?.setOnClickListener { dialog?.dismiss() }
    }

    private fun commonCodePopUp(dialog: Dialog?, resouce: Int): View? {
        if(dialog == null) {
            Log.e(Constants.STORAGE_LOGGER, "Errore! Dialog non popolato!")
            return null
        }
        val popUpView: View = dialog.layoutInflater.inflate(resouce, null)
        dialog.setContentView(popUpView)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        return popUpView
    }

    fun <B : ViewBinding> getPopUpBinding(dialog: Dialog?, bindingFactory: (LayoutInflater) -> B): B? {
        if(dialog == null) {
            Log.e(Constants.STORAGE_LOGGER, "Errore! Dialog non popolato!")
            return null
        }
        val binding = bindingFactory(dialog.layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        return binding
    }

    fun <A: Adapter<V>, V : ViewHolder> setRecyclerAdapter(recyclerView: RecyclerView,
                                                           context: Context,
                                                           adapter: A) {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }

    fun setPhrase(date: Date): String {
        val days: Long = getDaysFromExpiration(date)
        val phrase: String = if (days == -1L) {
            "è scaduto da " + days * -1 + " giorno."
        } else if (days < -1) {
            "è scaduto da " + days * -1 + " giorni."
        } else if (days == 1L) {
            "scade domani!"
        } else if (days > 1) {
            "scade tra $days giorni."
        } else {
            "scade oggi!"
        }
        return phrase
    }

    private fun getDaysFromExpiration(date: Date): Long {
        val localDate: LocalDate = convertDateToLocalDate(date) ?: return 0
        return getDiffDays(localDate, LocalDate.now())
    }

    private fun convertDateToLocalDate(date: Date?): LocalDate? {
        return if (date != null) {
            try {
                LocalDateTime.ofInstant(date.toInstant(), ZoneId.of("Europe/Rome")).toLocalDate()
            } catch (e: Exception) {
                Log.e(Constants.STORAGE_LOGGER, "Il formato della data non è corretto")
                null
            }
        } else null
    }

    private fun getDiffDays(startDate: LocalDate, endDate: LocalDate): Long {
        return if (startDate.isBefore(endDate)) getDiffDaysFromStartToEnd(
            startDate,
            endDate
        ) * -1 else if (startDate.isAfter(endDate)) {
            getDiffDaysFromStartToEnd(
                endDate,
                startDate
            )
        } else 0
    }

    private fun getDiffDaysFromStartToEnd(startDate: LocalDate, endDate: LocalDate): Long {
        return endDate.dayOfYear + (endDate.year - startDate.year) * 365L - startDate.dayOfYear
    }

    fun getHoursFromDate(date: Date) : Long {
        return ChronoUnit.HOURS.between(date.toInstant(), Instant.now())
    }

    fun refreshProfileList() {
        Constants.itemMapFilteredByProfile = mutableMapOf()
        Constants.profileSettings.profileMap.forEach { (profileEnum, profileButtonStateEnum) ->
            if(profileButtonStateEnum == ProfileButtonStateEnum.ON) {
                val filteredMap = Constants.itemMap.filter { it.value.profile.trim() == profileEnum.formattedName }
                Constants.itemMapFilteredByProfile.putAll(filteredMap)
            }
        }
    }

    fun expand(expandButton: ImageButton, recyclerView: RecyclerView) {
        if(recyclerView.visibility == View.VISIBLE) {
            expandButton.rotation = -90F
            recyclerView.visibility = View.GONE
        } else {
            expandButton.rotation = 0F
            recyclerView.visibility = View.VISIBLE
        }
    }

    fun filterOptionalMandatoryList(isOptional: Boolean, itemMap: MutableMap<String, StorageItem>) : MutableList<StorageItem> {
        return itemMap
            .filter { (isOptional && it.value.quantity == 0 && it.value.trigger < 0)
                    || (!isOptional && it.value.quantity <= it.value.trigger) }.values.toMutableList()
    }
}