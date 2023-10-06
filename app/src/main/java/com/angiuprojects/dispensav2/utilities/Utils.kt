package com.angiuprojects.dispensav2.utilities

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.res.ResourcesCompat
import android.R
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.angiuprojects.dispensav2.adapters.StorageUnitRecyclerAdapter
import com.angiuprojects.dispensav2.entities.StorageItem
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.reflect.KFunction1
import kotlin.reflect.KFunction3
import kotlin.reflect.KFunction5
import kotlin.reflect.KMutableProperty1

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
        val adapter = ArrayAdapter(context, R.layout.simple_spinner_dropdown_item, suggestionList)
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

    fun <T> createSimpleOKPopUp(message: String, dialog: Dialog?, onClickOk: KFunction1<T, Unit>, invoker: T) {
        val popUpView = commonCodePopUp(dialog, com.angiuprojects.dispensav2.R.layout.pop_up_simple_ok)
        popUpView?.findViewById<TextView>(com.angiuprojects.dispensav2.R.id.text)?.text = message
        val okButton = popUpView?.findViewById<Button>(com.angiuprojects.dispensav2.R.id.ok_button)
        okButton?.setOnClickListener { onClickOk.invoke(invoker) }
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

    fun <T> filterItemList(itemList: MutableList<StorageItem>,
                           getFieldFunction: KMutableProperty1<StorageItem, T>,
                           property: T)
    : MutableList<StorageItem> {
        return itemList.filter { getFieldFunction.get(it) == property }.sortedBy { it.name }.toMutableList()
    }

    fun <A: Adapter<V>, V : ViewHolder> setRecyclerAdapter(recyclerView: RecyclerView,
                                                           context: Context,
                                                           adapter: A) {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
    }
}