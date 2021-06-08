package com.example.flickr_search.ui

import androidx.lifecycle.ViewModel
import com.example.flickr_search.data.Data
import com.example.flickr_search.data.Photo
import com.example.flickr_search.repo.SearchRepo
import javax.inject.Inject

class SearchVM @Inject constructor(val searchRepo: SearchRepo) : ViewModel() {

    var data : Data<Photo>? = null



    var oldQuery: String  = ""

    fun search(query:String) : Data<Photo>? {
        if(data == null || oldQuery != query)
            data = searchRepo.searchPhoto(query)

        oldQuery = query
        return data
    }
}