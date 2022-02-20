package com.globe.data.datasource.local

import com.globe.data.datasource.local.instrumentation.RealmManager
import com.globe.data.datasource.local.instrumentation.executeAndWrapRealmQuery
import com.globe.data.datasource.local.instrumentation.flowAllWrapRealm
import com.globe.data.model.CountryDbModel
import com.globe.data.model.CountryModel
import com.globe.data.toCountryDbModel
import com.globe.data.toCountryModel
import io.realm.Sort
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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
                .map { it.toCountryModel() }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeCountyList(): Flow<List<CountryModel>> {
        return flowAllWrapRealm(realmManager) { realm ->
            realm.where(CountryDbModel::class.java)
                .sort(CountryDbModel::name.name, Sort.ASCENDING)
        }.map { list: List<CountryDbModel> -> list.map { it.toCountryModel() } }
    }
}
