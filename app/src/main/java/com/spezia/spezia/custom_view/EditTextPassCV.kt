package com.spezia.spezia.custom_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.spezia.spezia.R

class EditTextPassCV : AppCompatEditText {

    private lateinit var enableBackground: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        hint = resources.getString(R.string.enter_pass)
        background = enableBackground
    }

    private fun init() {
        enableBackground =
            ContextCompat.getDrawable(context, R.drawable.bg_text) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                val passwordError = context.getString(R.string.password_invalid)
                error = if (s.length < 8) {
                    passwordError
                } else {
                    null
                }
            }

            override fun afterTextChanged(s: Editable) {}

        })
    }
}