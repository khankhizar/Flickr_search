package com.example.flickr_search.repo

import android.util.Log
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import com.example.flickr_search.data.Data
import com.example.flickr_search.data.Photo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
//@OpenForTesting
class SearchRepo @Inject constructor(private val searchRemoteDataSource: SearchRemoteDataSource) {

    fun searchPhoto(query: String): Data<Photo> {
        Log.e("SearchRepo"," $query ")
        val dataSourceFactory = SearchDataSourceFactory(searchRemoteDataSource,
            query
        )



        val networkState = Transformations.switchMap(dataSourceFactory.liveData) {
            it.networkState
        }

        return Data(
            LivePagedListBuilder(dataSourceFactory,
                SearchDataSourceFactory.pagedListConfig())
                .build(),networkState)

    }


}