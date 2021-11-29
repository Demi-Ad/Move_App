package com.Demi.Move.IntroActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.viewpager2.widget.ViewPager2
import com.Demi.Move.IntroActivity.Fragments.IntroFragmentFirst
import com.Demi.Move.IntroActivity.Fragments.IntroFragmentSecond
import com.Demi.Move.IntroActivity.Fragments.IntroFragmentThird
import com.Demi.Move.MainActivity
import com.Demi.Move.MoveApplication
import com.Demi.Move.R
import com.Demi.Move.Utils.Constant
import com.Demi.Move.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityIntroBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.introSkipText.setOnClickListener {
            activityFinish()
        }
        val adapter = IntroViewPagerAdapter(this).apply {
            fragmentList = listOf(IntroFragmentFirst(),IntroFragmentSecond(),IntroFragmentThird())
        }


        binding.introNextText.setOnClickListener {
            binding.introView.setCurrentItem(binding.introView.currentItem + 1,true)
        }

        binding.introStartText.setOnClickListener {
            activityFinish()
        }

        binding.introView.apply {
            this.adapter = adapter
            this.isUserInputEnabled = false
            this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.imageView11.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@IntroActivity,
                            R.drawable.shape_circle_white
                        )
                    )
                    binding.imageView12.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@IntroActivity,
                            R.drawable.shape_circle_white
                        )
                    )
                    binding.imageView13.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@IntroActivity,
                            R.drawable.shape_circle_white
                        )
                    )
                    when (position) {
                        0 -> binding.imageView11.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@IntroActivity,
                                R.drawable.shape_circle_blue
                            )
                        )
                        1 -> binding.imageView12.setImageDrawable(
                            AppCompatResources.getDrawable(
                                this@IntroActivity,
                                R.drawable.shape_circle_blue
                            )
                        )
                        2 -> {
                            binding.imageView13.setImageDrawable(
                                AppCompatResources.getDrawable(
                                    this@IntroActivity,
                                    R.drawable.shape_circle_blue
                                )
                            )
                            binding.introStartText.visibility = View.VISIBLE
                            binding.introNextText.visibility = View.GONE

                        }
                    }
                }
            })
        }
    }

    private fun activityFinish() {
        MoveApplication.prefs.setBoolean(Constant.FIRST, true)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}