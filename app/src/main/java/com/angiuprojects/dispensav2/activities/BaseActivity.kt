package com.angiuprojects.dispensav2.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.implementation.MainActivity
import com.angiuprojects.dispensav2.databinding.HeaderLayoutBinding
import com.angiuprojects.dispensav2.utilities.Utils

abstract class BaseActivity<B : ViewBinding> (val bindingFactory: (LayoutInflater) -> B) : AppCompatActivity() {

    lateinit var binding: B
    lateinit var snackBarView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
        window.decorView.setBackgroundColor(resources.getColor(R.color.light_blue, baseContext.theme))
        snackBarView = findViewById(android.R.id.content)
    }

    open fun setHeaderListener(header: HeaderLayoutBinding, activityName: Int?, context: Context,
                               isFilterPresent: Boolean, isSearchPresent: Boolean) {
        if (activityName != null)
            header.activityName.setText(activityName)
        else
            header.activityName.visibility = View.GONE

        if(!isFilterPresent) header.filterButton.visibility = View.GONE
        if(!isSearchPresent) header.searchButton.visibility = View.GONE

        header.filterButton.setOnClickListener { setFilterListener(header) }
        header.searchButton.setOnClickListener { setSearchListener(header) }
        header.closeButton.setOnClickListener { setCloseListener(header) }
        header.backButton.setOnClickListener { finish() }
    }

    open fun setFilterListener(header: HeaderLayoutBinding) {
        header.filterButton.visibility = View.GONE
        header.searchButton.visibility = View.GONE

        header.closeButton.visibility = View.VISIBLE
        header.filterDropdown.visibility = View.VISIBLE
    }

    open fun setSearchListener(header: HeaderLayoutBinding) {
        header.filterButton.visibility = View.GONE
        header.searchButton.visibility = View.GONE

        header.closeButton.visibility = View.VISIBLE
        header.searchInput.visibility = View.VISIBLE
    }

    open fun setCloseListener(header: HeaderLayoutBinding) {
        header.filterButton.visibility = View.VISIBLE
        header.searchButton.visibility = View.VISIBLE

        header.closeButton.visibility = View.GONE
        header.filterDropdown.visibility = View.GONE
        header.searchInput.visibility = View.GONE
    }
}