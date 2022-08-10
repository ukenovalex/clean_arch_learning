package ca.ukenov.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorInputName")
fun bindingErrorInputName(textInput: TextInputLayout, value: Boolean) {
    if (value) {
        textInput.error = "Name is require field"
    } else {
        textInput.error = ""
    }
}

@BindingAdapter("errorInputCount")
fun bindingErrorInputCount(textInput: TextInputLayout, value: Boolean){
    if (value) {
        textInput.error = "Count is must be more than zero"
    } else {
        textInput.error = ""
    }
}

@BindingAdapter("currentCountText")
fun bindingCurrentCountText(textInput: TextInputEditText, count: Int) {
    if (count > 0) {
        textInput.setText(count.toString())
    }
}