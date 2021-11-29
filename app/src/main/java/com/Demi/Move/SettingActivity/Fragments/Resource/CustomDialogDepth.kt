package com.Demi.Move.SettingActivity.Fragments.Resource

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.Demi.Move.MoveApplication
import com.Demi.Move.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView

class CustomDialogDepth:DialogFragment() {
    lateinit var listener:CustomDialogDepthListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
    }

    fun setOnClickListener(listener: CustomDialogDepthListener) {
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.custom_dailog_depth,container,false)
        val btn = view.findViewById<MaterialButton>(R.id.setting_depth_btn)
        val textEdit = view.findViewById<TextInputEditText>(R.id.setting_depth_text)

        textEdit.setText(MoveApplication.prefs.getInt("searchDepth",0).toString())

        btn.setOnClickListener {
            if (textEdit.text?.isEmpty() == true)
            {
                listener.onClick(0)
            } else
                listener.onClick(textEdit.text.toString().toInt())
        }

        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("로그", "onDestroy: 다이얼로그")
    }

    interface CustomDialogDepthListener {
        fun onClick(value:Int)
    }

}