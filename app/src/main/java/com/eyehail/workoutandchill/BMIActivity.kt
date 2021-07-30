package com.eyehail.workoutandchill


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_m_i)
        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar_bmi_activity))

        val actionbar = supportActionBar
        if(actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title="CALCULATE BMI"
        }
        findViewById<Toolbar>(R.id.toolbar_bmi_activity).setNavigationOnClickListener {
            onBackPressed()
        }
        findViewById<Button>(R.id.btnCalculateUnits).setOnClickListener {
            if (validateMetricUnits()) {
                val heightValue: Float = findViewById<AppCompatEditText>(R.id.etMetricUnitHeight).text.toString().toFloat() / 100
                val weightValue: Float = findViewById<AppCompatEditText>(R.id.etMetricUnitWeight).text.toString().toFloat()

                val bmi = weightValue / (heightValue * heightValue)
                displayBMIResult(bmi)

            } else {
                Toast.makeText(this@BMIActivity, "Please, enter valid value.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                        bmi,
                        30f
                ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        findViewById<TextView>(R.id.tvYourBMI).visibility = View.VISIBLE
        findViewById<TextView>(R.id.tvBMIValue).visibility = View.VISIBLE
        findViewById<TextView>(R.id.tvBMIType).visibility = View.VISIBLE
        findViewById<TextView>(R.id.tvBMIDescription).visibility = View.VISIBLE

        // This is used to round the result value to 2 decimal values after "."
        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        findViewById<TextView>(R.id.tvBMIValue).text = bmiValue // Value is set to TextView
        findViewById<TextView>(R.id.tvBMIType).text = bmiLabel // Label is set to TextView
        findViewById<TextView>(R.id.tvBMIDescription).text = bmiDescription // Description is set to TextView
    }

    private fun validateMetricUnits(): Boolean {
        var isValid = true
        if (findViewById<TextInputEditText>(R.id.etMetricUnitWeight).text.toString().isEmpty()) {
            isValid = false
        } else if (findViewById<TextInputEditText>(R.id.etMetricUnitHeight).text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }
}