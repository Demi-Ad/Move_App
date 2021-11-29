/*
Author : Park Jong Seong
Created At : 2021-11-02 - 오후 10:25
Updated At :
Description : 에디트액티비티
*/
package com.Demi.Move.EditActivity

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.Demi.Move.Database.PathData
import com.Demi.Move.MainActivityResource.MainRecyclerViewInItemsAdapter
import com.Demi.Move.R
import com.Demi.Move.Utils.*
import com.Demi.Move.databinding.ActivityEditBinding

/**
 * 아이템 추가 / 삭제 엑티비티
 *
 * [editActionGroupInit] - 액션버튼 토글 구현
 *
 * [fromSafInit] - 옮길 폴더의 위치를 SAF 를 실행하여 얻은 URI를 절대경로로 변환
 *
 * [toSafInit] - 도착 폴더의 위피를 SAF 를 실행하여 얻은 URI 를 절대경로로 변환
 */

class EditActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityEditBinding.inflate(layoutInflater)
    }
    private lateinit var mainPath: String
    private lateinit var downloadPath: String

    private lateinit var getFromPathResult: ActivityResultLauncher<Intent>
    private lateinit var getToPathResult: ActivityResultLauncher<Intent>
    private val intentPathData: PathData? by lazy {
        intent.getParcelableExtra(Constant.INPUT_MODEL)
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[EditViewModel::class.java]
    }

    private val TAG = "로그"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.editToolbar)
        val adapter = MainRecyclerViewInItemsAdapter(ContextCompat.getColor(this, R.color.black))
        val recyclerView = binding.editRecycler
        recyclerView.apply {
            this.adapter = adapter
            this.layoutManager = GridLayoutManager(this@EditActivity, 3)
        }


        editActionGroupInit()
        fromSafInit()
        toSafInit()

        mainPath = Environment.getExternalStorageDirectory().absolutePath
        downloadPath = Environment.DIRECTORY_DOWNLOADS
        binding.pathFromTextview.setText(downloadPath)

        if (intent.extras?.get(Constant.TYPE) == Constant.NEW) {
            Log.d(TAG, "EditActivity - New: ")

        } else {
            Log.d(TAG, "EditActivity - EDIT:")

            when (intentPathData!!.actionType) {
                Constant.DEL ->
                    binding.pathFromTextview.setText(intentPathData!!.srcPath.toShotPath("$mainPath/"))
                else -> {
                    binding.pathFromTextview.setText(intentPathData!!.srcPath.toShotPath("$mainPath/"))
                    binding.pathToTextview.setText(intentPathData!!.dstPath.toShotPath("$mainPath/"))
                }
            }

            when (intentPathData!!.actionType) {
                Constant.MOVE ->
                    binding.btnMove.isChecked = true
                Constant.COPY ->
                    binding.btnCopy.isChecked = true
                Constant.DEL ->
                    binding.btnDelete.isChecked = true
            }

            when (intentPathData!!.type) {
                Constant.IMAGE ->
                    binding.btnImage.isChecked = true

                Constant.DOCS ->
                    binding.btnDocs.isChecked = true

                Constant.MUSIC ->
                    binding.btnMusic.isChecked = true

                Constant.VIDEO ->
                    binding.btnVideo.isChecked = true
                Constant.CUSTOM ->
                    binding.btnCustom.isChecked = true
            }
//            editBtnInit()
        }



        viewModel.extension.observe(this) {
            val data = ArrayList<String>()
            data.addAll(it)
            adapter.submitList(data)
            adapter.notifyDataSetChanged()
        }
    }

    private fun editActionGroupInit() {
        binding.editActionGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_delete -> {
                        if (isChecked) {
                            binding.toLinear.visibility = View.INVISIBLE
                            binding.pathToTextview.setText("")
                        }
                    }
                    else -> {
                        binding.toLinear.visibility = View.VISIBLE
                    }
                }
            } else {
                binding.toLinear.visibility = View.VISIBLE
            }
        }

        binding.editBtnGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_image -> {
                        viewModel.getExtensions(Constant.IMAGE)
                    }
                    R.id.btn_music -> {
                        viewModel.getExtensions(Constant.MUSIC)
                    }
                    R.id.btn_docs -> {
                        viewModel.getExtensions(Constant.DOCS)
                    }
                    R.id.btn_video -> {
                        viewModel.getExtensions(Constant.VIDEO)
                    }
                    R.id.btn_custom -> {
                        viewModel.getExtensions(Constant.CUSTOM)
                    }
                }
            } else {
                viewModel.clear()
            }
        }
    }

    private fun fromSafInit() {
        getFromPathResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val resultPath = it.data?.data?.path.toString().split(":")[1]
                    binding.pathFromTextview.setText(resultPath)
                }
            }
        binding.openFromSaf.setOnClickListener {
            getFromPathResult.launch(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
        }

        binding.btnRestore.setOnClickListener {
            binding.pathFromTextview.setText(downloadPath)
        }
    }

    private fun toSafInit() {
        getToPathResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val resultPath = it.data?.data?.path.toString().split(":")[1]
                    binding.pathToTextview.setText(resultPath)
                }
            }
        binding.openToSaf.setOnClickListener {
            getToPathResult.launch(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE))
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_add -> {
                if (binding.btnDelete.isChecked) {
                    binding.toLinear.visibility = View.INVISIBLE
                    binding.pathToTextview.setText("")
                }
                if (binding.editActionGroup.checkedButtonId > 0) {
                    if (binding.editBtnGroup.checkedButtonId > 0) {
                        if (binding.pathToTextview.text.toString() != binding.pathFromTextview.text.toString()) {
                            val intent = Intent()
                            val srcPath = "$mainPath/${binding.pathFromTextview.text}"

                            val type = when (binding.editBtnGroup.checkedButtonId) {
                                R.id.btn_image -> Constant.IMAGE
                                R.id.btn_docs -> Constant.DOCS
                                R.id.btn_music -> Constant.MUSIC
                                R.id.btn_video -> Constant.VIDEO
                                R.id.btn_custom -> Constant.CUSTOM
                                else -> ""
                            }

                            when (binding.editActionGroup.checkedButtonId) {
                                R.id.btn_delete -> {
                                    val pathData = PathData(
                                        id = if (intentPathData == null) null else intentPathData!!.id,
                                        srcPath = srcPath,
                                        dstPath = "",
                                        actionType = Constant.DEL,
                                        type = type
                                    )
                                    intent.putExtra(Constant.RETURN_MODEL, pathData)
                                    setResult(RESULT_OK, intent)
                                    finish()
                                }
                                else -> {
                                    if (binding.pathToTextview.text?.isEmpty() != true) {
                                        val dstPath = "$mainPath/${binding.pathToTextview.text}"
                                        val pathData = PathData(
                                            id = if (intentPathData == null) null else intentPathData!!.id,

                                            srcPath = srcPath,
                                            dstPath = dstPath,
                                            type = type,
                                            actionType = when (binding.editActionGroup.checkedButtonId) {
                                                R.id.btn_move -> Constant.MOVE
                                                else -> Constant.COPY
                                            }
                                        )
                                        intent.putExtra(Constant.RETURN_MODEL, pathData)
                                        setResult(RESULT_OK, intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            "Select From Folder!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                        } else
                            Toast.makeText(this, "Start and End are duplicated", Toast.LENGTH_SHORT)
                                .show()

                    } else
                        Toast.makeText(this, "Select Type Button!", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(this, "Select Action Button!", Toast.LENGTH_SHORT).show()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}
