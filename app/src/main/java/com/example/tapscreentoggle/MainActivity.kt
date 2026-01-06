package com.example.tapscreentoggle

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private var isScreenAwake = false
    private lateinit var statusText: TextView
    private lateinit var iconPower: ImageView
    private lateinit var rootLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        iconPower = findViewById(R.id.iconPower)
        rootLayout = findViewById(R.id.rootLayout)

        updateUi(false) // Initial state

        rootLayout.setOnClickListener {
            toggleScreenState()
        }
    }

    private fun toggleScreenState() {
        isScreenAwake = !isScreenAwake
        if (isScreenAwake) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        updateUi(true)
    }

    private fun updateUi(animate: Boolean) {
        val colorActive = ContextCompat.getColor(this, R.color.neon_blue)
        val colorInactive = ContextCompat.getColor(this, R.color.icon_inactive)
        val textActive = ContextCompat.getColor(this, R.color.white)
        val textInactive = ContextCompat.getColor(this, R.color.text_dim)

        val targetColor = if (isScreenAwake) colorActive else colorInactive
        val targetTextColor = if (isScreenAwake) textActive else textInactive

        if (animate) {
            // Animate Icon Tint
            val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), iconPower.imageTintList?.defaultColor ?: colorInactive, targetColor)
            colorAnimation.duration = 300
            colorAnimation.addUpdateListener { animator ->
                iconPower.imageTintList = ColorStateList.valueOf(animator.animatedValue as Int)
            }
            colorAnimation.start()
            
            // Animate Scale
            iconPower.animate().scaleX(if (isScreenAwake) 1.1f else 1.0f).scaleY(if (isScreenAwake) 1.1f else 1.0f).setDuration(300).start()
        } else {
            iconPower.imageTintList = ColorStateList.valueOf(targetColor)
            iconPower.scaleX = if (isScreenAwake) 1.1f else 1.0f
            iconPower.scaleY = if (isScreenAwake) 1.1f else 1.0f
        }

        if (isScreenAwake) {
            statusText.text = "Screen Awake Profile"
            statusText.setTextColor(textActive)
            // Add a subtle glow effect (handled via shadow layer for simplicity)
            statusText.setShadowLayer(10f, 0f, 0f, colorActive)
        } else {
            statusText.text = "Normal Sleep Mode"
            statusText.setTextColor(textInactive)
            statusText.setShadowLayer(0f, 0f, 0f, 0)
        }
    }
}
