/*
Author : Park Jong Seong
Created At : 2021-10-31 - 오후 8:49
Updated At :
Description : 메인 액티비티
*/

package com.Demi.Move

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.Demi.Move.Database.PathData
import com.Demi.Move.Database.ViewPathData
import com.Demi.Move.EditActivity.EditActivity
import com.Demi.Move.IntroActivity.IntroActivity
import com.Demi.Move.MainActivityResource.MainRecyclerViewAdapter
import com.Demi.Move.MainActivityResource.MainViewModel
import com.Demi.Move.SettingActivity.SettingsActivity
import com.Demi.Move.Utils.*
import com.Demi.Move.databinding.ActivityMainBinding
import com.Demi.Move.repository.PathDataRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import java.io.File


class MainActivity : AppCompatActivity() {
    val TAG: String = "로그 MainActivity"
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var activityResultNewType: ActivityResultLauncher<Intent>
    private lateinit var activityResultEditType: ActivityResultLauncher<Intent>

    private lateinit var viewModel: MainViewModel

    private val notificationManager: NotificationManager by lazy {
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        // 앱 최초 실행 시 작업
        MoveApplication.prefs.getBoolean(Constant.FIRST, false).let {
            if (!it) {
                Log.d(TAG, "MainActivity: First ")
                MoveApplication.prefs.setBoolean(Constant.SHOW, true)
                MoveApplication.prefs.setInt(Constant.DEPTH,0)
                createNotifyChannel()
                startActivity(Intent(this,IntroActivity::class.java))
                finish()
            } else {
                if (!Environment.isExternalStorageManager()) {
                    externalStorageGetPermission()
                }
            }
        }

        viewModel =
            ViewModelProvider(this, defaultViewModelProviderFactory)[MainViewModel::class.java]



        activityResultNewType =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val pathData: PathData? =
                        it.data?.getParcelableExtra(Constant.RETURN_MODEL)
                    if (pathData != null) {
                        Log.d(TAG, "MainActivity - onCreate: $pathData")

                        viewModel.insert(pathData)
                    }
                }
            }
        activityResultEditType =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val pathData: PathData? = it.data?.getParcelableExtra(Constant.RETURN_MODEL)
                    if (pathData != null) {
                        Log.d(TAG, "MainActivity - onCreate: $pathData")
                        viewModel.update(pathData)
                    }
                }
            }

        addBtnClick()
        recyclerInit()
    }


    /**
     * 사용자에게 [Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION] 권한을 요청합니다
     *
     * 메니페스트에서 "android.permission.MANAGE_EXTERNAL_STORAGE" 권한 선언이 필요합니다
     *
     * 권한 거부시 앱이 실행 되지 않으므로 반드시 필요하단걸 어필해야합니다
     *
     */
    private fun externalStorageGetPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.request_permission_title))
        builder.setMessage(R.string.request_permission_description)

        val listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> {
                    Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                        addCategory("android.intent.category.DEFAULT")
                        data = Uri.parse(
                            String.format(
                                "package:%s",
                                applicationContext.packageName
                            )
                        )
                        startActivity(this)
                    }
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    finish()
                }
            }
        }
        builder.setPositiveButton(R.string.request_yes, listener)
        builder.setNegativeButton(R.string.request_no, listener)
        builder.show()
    }


    /**
     * 버튼 클릭시 "EditActivity" 로 인텐트 전달
     *
     * EditActivity 에서 Type 에 따라 처리 변경
     *
     * @return [activityResultNewType]
     *
     */
    private fun addBtnClick() {
        binding.addBtn.setOnClickListener {
            Intent(this, EditActivity::class.java).apply {
                putExtra(Constant.TYPE, Constant.NEW)
                activityResultNewType.launch(this)
            }
        }
    }

    private fun createNotifyChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel =
            NotificationChannel(NotifyValue.CHANNEL.ID, NotifyValue.CHANNEL.NAME, importance)
        channel.description = NotifyValue.CHANNEL.DESCRIPTION
        notificationManager.createNotificationChannel(channel)
    }


    private fun recyclerInit() {
        val recyclerview = binding.mainRecyclerView
        val adapter = MainRecyclerViewAdapter(
            this,
            onDelete = { viewModel.delete(it) },
            onStart = { viewPathData ->
                MoveApplication.prefs.getBoolean(Constant.SHOW, true).let {
                    when (it) {
                        true -> {
                            val builder = AlertDialog.Builder(this).apply {
                                setTitle(R.string.work_albert_title)
                                setMessage(R.string.work_albert_description)
                                val listener = DialogInterface.OnClickListener { _, position ->
                                    when (position) {
                                        DialogInterface.BUTTON_POSITIVE ->
                                            recyclerOnStarInit(viewPathData)
                                    }
                                }
                                setPositiveButton(R.string.request_yes, listener)
                                setNegativeButton(R.string.request_no,listener)
                            }
                            builder.show()
                        }
                        false -> recyclerOnStarInit(viewPathData)
                    }
                }
            },
            onClick = {
                val intent = Intent(this, EditActivity::class.java).apply {
                    putExtra(Constant.TYPE, Constant.EDIT)
                    putExtra(Constant.INPUT_MODEL, it)
                }
                activityResultEditType.launch(intent)
            }
        )

        recyclerview.apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        viewModel.viewAllData.observe(this) {
            adapter.submitList(it)
            adapter.notifyDataSetChanged()
        }
    }

    /**
     * @param viewPathData Onclick 이벤트 데이터 리턴
     * @see MoveWorker
     *
     */
    private fun recyclerOnStarInit(viewPathData: ViewPathData) {
        CoroutineScope(Dispatchers.Default).launch {
            val workManager = WorkManager.getInstance(this@MainActivity)
            val requestWorkBuilder = OneTimeWorkRequestBuilder<MoveWorker>()
            val filesPath =
                withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
                    File(viewPathData.pathData.srcPath)
                        .walkForPreference()
                        .filter { file -> file.extension in viewPathData.extensionList }
                        .map { it.toPath().toString() }
                        .toList()
                        .toTypedArray()
                } //SrcPath에 파일명을 어레이로 변환

            if (filesPath.isEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(this@MainActivity, R.string.toast_no_files, Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                val filesData = mapOf(Constant.FILE_PATH to filesPath)
                val dstPath =
                    mapOf(Constant.DST_PATH to viewPathData.pathData.dstPath)
                val action =
                    mapOf(Constant.ACTION to viewPathData.pathData.actionType)
                val data: Data =
                    Data.Builder().putAll(filesData).putAll(dstPath).putAll(action).build()
                val requestWork = requestWorkBuilder.setInputData(data).build()

                workManager.enqueueUniqueWork(
                    "Work",
                    ExistingWorkPolicy.APPEND,
                    requestWork
                )

                CoroutineScope(Dispatchers.Main).launch {
                    workManager.getWorkInfoByIdLiveData(requestWork.id)
                        .observe(this@MainActivity) {
                            if (it.state == WorkInfo.State.SUCCEEDED) {
                                Log.d(TAG, "WorkObserver: success ")
                                val notification = NotificationCompat
                                    .Builder(this@MainActivity, NotifyValue.CHANNEL.ID)
                                    .setContentText(getString(R.string.work_complete))
                                    .setSmallIcon(R.drawable.ic_stat_noti)
                                    .setContentTitle(getString(R.string.app_name))
                                    .build()
                                notificationManager.notify(101, notification)
                            }
                        }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.option_setting ->
                startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}
