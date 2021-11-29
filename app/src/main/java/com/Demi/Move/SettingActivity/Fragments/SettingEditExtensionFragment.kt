package com.Demi.Move.SettingActivity.Fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.Demi.Move.Database.ExtensionData
import com.Demi.Move.R
import com.Demi.Move.SettingActivity.Fragments.Resource.CustomDialog
import com.Demi.Move.SettingActivity.Fragments.Resource.SettingEditExtensionAdapter
import com.Demi.Move.SettingActivity.Fragments.Resource.SettingEditFragmentViewModel
import com.Demi.Move.SettingActivity.SettingsActivity
import com.Demi.Move.databinding.FragmentSettingEditExtensionBinding
import com.Demi.Move.repository.PathDataRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingEditExtensionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingEditExtensionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentSettingEditExtensionBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: SettingEditFragmentViewModel


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
        _binding = FragmentSettingEditExtensionBinding.inflate(inflater, container, false)
        val actionBar = (activity as SettingsActivity).supportActionBar
        actionBar?.title = "Edit Extension"
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(SettingEditFragmentViewModel::class.java)
        val recyclerView = binding.settingEditExtensionRecyclerview
        val adapter = SettingEditExtensionAdapter(onDelete = {
            Log.d("로그", "onViewCreated:$it")
            viewModel.delete(it)

        })
        recyclerView.apply {
            Log.d("로그", "onCreateView: ")
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context)
            this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        viewModel.extensionView.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingEditExtensionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingEditExtensionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onDestroy() {
        (activity as SettingsActivity).supportActionBar?.title = "Setting"
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.setting_extension_edit_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setting_edit_extension_add_btn -> {
                Log.d("로그", "option Click")
                val dialog = CustomDialog()
                dialog.setOnClickListener(object : CustomDialog.CustomDialogListener {
                    override fun onClick(data: ExtensionData) {
                        CoroutineScope(Dispatchers.IO).launch {
                            val repository = PathDataRepository.getInstance(activity!!.application)
                            val dataList = repository.getTypeData(data.type)
                            if (data.extension in dataList)
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(activity, R.string.setting_dialog_albert_exists,Toast.LENGTH_SHORT).show()
                                }
                            else if (data.extension.isEmpty() || data.type.isEmpty()) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(activity,R.string.setting_dialog_albert_empty,Toast.LENGTH_SHORT).show()
                                }
                            }
                            else
                                withContext(Dispatchers.Main) {
                                    viewModel.insert(data)
                                    dialog.dismiss()
                                }
                        }
                    }
                })
                activity?.supportFragmentManager?.let {
                    dialog.show(it, "AA")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}