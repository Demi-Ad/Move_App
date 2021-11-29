package com.Demi.Move.SettingActivity.Fragments.Resource

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.DialogFragment
import com.Demi.Move.Database.ExtensionData
import com.Demi.Move.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialTextInputPicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class CustomDialog : DialogFragment() {
    lateinit var listener: CustomDialogListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    fun setOnClickListener(listener : CustomDialogListener) {
        this.listener = listener
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.custom_dialog, container, false)
        val items = listOf("Image", "Music", "Video","Docs","Custom")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        view.findViewById<AutoCompleteTextView>(R.id.autoComplete_text).setAdapter(adapter)
        view.findViewById<MaterialButton>(R.id.setting_edit_extension_dialog_add_btn).setOnClickListener {
            val type = view.findViewById<AutoCompleteTextView>(R.id.autoComplete_text).text.toString()
            val extension = view.findViewById<TextInputEditText>(R.id.setting_edit_extension_add_textInput).text.toString()
            listener.onClick(ExtensionData(type = type, extension = extension))
        }
        return view
    }

    interface CustomDialogListener {
        fun onClick(data: ExtensionData)
    }
}