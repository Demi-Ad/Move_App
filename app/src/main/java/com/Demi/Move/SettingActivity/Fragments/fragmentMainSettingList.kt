package com.Demi.Move.SettingActivity.Fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.Demi.Move.MoveApplication
import com.Demi.Move.R
import com.Demi.Move.SettingActivity.Fragments.Resource.CustomDialogDepth
import com.Demi.Move.Utils.Constant
import com.Demi.Move.databinding.FragmentMainSettingListBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragmentMainSettingList.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragmentMainSettingList : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentMainSettingListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainSettingListBinding.inflate(inflater, container, false)

        binding.searchDepth.setOnClickListener {
            val dialog = CustomDialogDepth()
            activity?.supportFragmentManager.let {
                dialog.show(it!!, "AA")
            }
            dialog.setOnClickListener(object : CustomDialogDepth.CustomDialogDepthListener {
                override fun onClick(value: Int) {
                    value.let {
                        MoveApplication.prefs.setInt(Constant.DEPTH, it)
                    }
                    dialog.dismiss()
                }
            })
        }

        binding.settingNotifyControlSwitchBtn.isChecked =
            MoveApplication.prefs.getBoolean(Constant.SHOW, true)

        binding.settingNotifyControlSwitchBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                MoveApplication.prefs.setBoolean(Constant.SHOW, true)
                Log.d("로그", "onCreateView: 체크")
            } else {
                MoveApplication.prefs.setBoolean(Constant.SHOW, false)
                Log.d("로그", "onCreateView: 체크해제")
            }
        }

        binding.settingExtension.setOnClickListener {
            it.findNavController()
                .navigate(R.id.action_fragmentMainSettingList_to_settingEditExtensionFragment)
        }

        binding.settingReset.setOnClickListener {
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + requireActivity().packageName)
            ).apply {
                addCategory(Intent.CATEGORY_DEFAULT)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.let { startActivity(it) }
        }

        binding.settingVersionText.text = requireContext().let {
            return@let it.packageManager.getPackageInfo(it.packageName,0).versionName
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragmentMainSettingList.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragmentMainSettingList().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}