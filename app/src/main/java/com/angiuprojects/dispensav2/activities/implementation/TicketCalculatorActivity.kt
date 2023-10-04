package com.angiuprojects.dispensav2.activities.implementation

import android.os.Bundle
import android.view.View
import com.angiuprojects.dispensav2.R
import com.angiuprojects.dispensav2.activities.BaseActivity
import com.angiuprojects.dispensav2.databinding.ActivityCalculatorBinding
import com.angiuprojects.dispensav2.utilities.Constants
import com.angiuprojects.dispensav2.utilities.Utils
import java.math.RoundingMode
import java.text.DecimalFormat

class TicketCalculatorActivity : BaseActivity<ActivityCalculatorBinding>(ActivityCalculatorBinding::inflate) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setBackButtonListener(binding.header, R.string.ticket_calculator_txt, this)

        binding.calculateButton.setOnClickListener { onClickCalculate() }
    }

    private fun onClickCalculate() {

        // If Amount is Empty it Can't be Calculated
        if(binding.amountExpVal.editText == null || binding.amountExpVal.editText!!.text == null
            || binding.amountExpVal.editText!!.text.toString().isEmpty()) {
           Utils.singleton.openSnackBar(snackBarView, "Inserire Importo!")
            return
        }

        // Conversion Ticket and Expenses from String to Double
        val bpValueStr = binding.amountTicketsVal.selectedItem.toString().substring(0,4).replace(",", ".")
        val bpValue = bpValueStr.toDouble()

        val spendedAmount = binding.amountExpVal.editText!!.text.toString().toDouble()


        // Calculations
        val change: Double
        val missing: Double
        if(spendedAmount > bpValue * Constants.maxTicketInOneTransaction) {
            change = spendedAmount - bpValue * Constants.maxTicketInOneTransaction
            missing = 0.0
        }
        else {
            change = spendedAmount % bpValue
            missing = bpValue - change
        }

        // Format and Shows Results
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.HALF_UP

        binding.relativeLayout.visibility = View.VISIBLE
        binding.amountExcedingVal.text = buildString {
            append(df.format(change))
            append(" €")
        }
        binding.missingAmountVal.text = buildString {
            append(df.format(missing))
            append(" €")
        }
    }
}