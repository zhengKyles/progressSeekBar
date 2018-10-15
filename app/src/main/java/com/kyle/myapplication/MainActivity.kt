package com.kyle.myapplication

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.kyle.gradientseekbar.MySeekBarLine
import com.kyle.progressSeekBar.R

class MainActivity : AppCompatActivity() {
    lateinit var seekBar:MySeekBarLine
    lateinit var tvProgress:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        seekBar=findViewById(R.id.seekBar)
        tvProgress=findViewById(R.id.tv_progress)
        seekBar.setOnProgressChangeListener {  progress ->
            tvProgress.text = progress.toString()
        }
    }
}
