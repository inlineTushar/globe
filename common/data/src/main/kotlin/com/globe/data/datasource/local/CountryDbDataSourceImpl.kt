package com.globe.data.datasource.local

import com.globe.data.datasource.local.instrumentation.RealmManager
import com.globe.data.datasource.local.instrumentation.executeAndWrapRealmQuery
import com.globe.data.model.CountryDbModel
import com.globe.data.model.CountryModel
import com.globe.data.toCountryDbModel
import com.globe.data.toCountryModel
import io.realm.Case
import io.realm.Sort

class CountryDbDataSourceImpl(
    private val realmManager: RealmManager
) : CountryDbDataSource {

    override suspend fun insert(countryList: List<CountryModel>) {
        executeAndWrapRealmQuery(realmManager) { realm ->
            realm.executeTransaction {
                it.insertOrUpdate(countryList.map { countryModel -> countryModel.toCountryDbModel() })
            }
        }
    }

    override suspend fun getAllCountries(): List<CountryModel> =
        executeAndWrapRealmQuery(realmManager) { realm ->
            realm.where(CountryDbModel::class.java)
                .findAll()
                .sort(CountryDbModel::name.name, Sort.ASCENDING)
                .map { it.toCountryModel() }
        }

    override suspend fun getCountriesBySearch(keyword: String): List<CountryModel> =
        executeAndWrapRealmQuery(realmManager) { realm ->
            realm.where(CountryDbModel::class.java)
                .beginsWith(CountryDbModel::name.name, keyword, Case.INSENSITIVE)
                .findAll()
                .sort(CountryDbModel::name.name, Sort.ASCENDING)
                .map { it.toCountryModel() }
        }
}
