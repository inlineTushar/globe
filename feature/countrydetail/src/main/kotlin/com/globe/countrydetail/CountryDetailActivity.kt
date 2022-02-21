package com.globe.countrydetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.globe.EXTRA_COUNTRY_ID
import com.globe.countrydetail.databinding.ActivityCountryDetailBinding

class CountryDetailActivity : AppCompatActivity() {

    private val countryId: String by lazy {
        intent.extras?.getString(EXTRA_COUNTRY_ID)
            ?: throw IllegalArgumentException("Intent without $EXTRA_COUNTRY_ID is not allowed")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCountryDetailBinding.inflate(layoutInflater).apply {
            setContentView(root)
            toolbar.setNavigationOnClickListener { finish() }
            supportFragmentManager.beginTransaction()
                .add(R.id.detail_controller, CountryDetailControllerFragment.newInstance(countryId))
                .commit()
        }
    }
}
