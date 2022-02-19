package com.globe

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.globe.ui.R
import com.globe.ui.databinding.ViewCountryInfoBinding

class CountryInfoView @kotlin.jvm.JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {

    private var binding = ViewCountryInfoBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        )
    }

    fun renderContent(name: String, flag: String?, code: String) {
        binding.heading.text =
            if (flag.isNullOrEmpty()) {
                context.getString(R.string.country_heading, name, code)
            } else {
                context.getString(R.string.country_heading_with_flag, flag, name, code)
            }
    }
}
