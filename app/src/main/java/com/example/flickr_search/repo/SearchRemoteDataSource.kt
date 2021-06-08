package com.example.flickr_search.repo

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.example.flickr_search.data.BaseDataSource
import com.example.flickr_search.data.FlickrApi
import com.example.flickr_search.data.Photo
import com.example.flickr_search.data.SearchResult
import com.example.flickr_search.data.Result
import javax.inject.Inject

//@OpenForTesting
class SearchRemoteDataSource @Inject constructor(val service: FlickrApi) : BaseDataSource() {

    val map = HashMap<String,String>()

    init {
        map["method"] = "flickr.photos.search"
        map["api_key"] = "3e7cc266ae2b0e0d78e279ce8e361736"
        map["format"] = "json"

    }

    fun search(perPage:Int,query: String,page:Int) : Result<SearchResult> {

        map["text"] = query
        return getResult { service.searchPhotos(perPage,page,map) }
    }

}