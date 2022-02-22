package com.globe.countrylist

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.globe.countrylist.databinding.ViewCountryInfoBinding
import com.globe.load
import com.globe.ui.R

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

    fun renderContent(name: String, flagUnicode: String?, flagUrl: String?, code: String) {
        binding.apply {
            imageFlag.isVisible = true
            unicodeFlag.isVisible = true
            heading.text = context.getString(R.string.country_heading, name, code)
            unicodeFlag.text = flagUnicode
            imageFlag.load(
                context,
                flagUrl,
                onFailure = { imageFlag.visibility = View.INVISIBLE },
                onSuccess = { unicodeFlag.isVisible = false }
            )
        }
    }
}
