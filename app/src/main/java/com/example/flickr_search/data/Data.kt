package com.example.flickr_search.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Data<T>(
    // the LiveData of paged lists for the UI to observe
    val pagedList: LiveData<PagedList<T>>,
    // represents the network request status to show to the user
    val networkState: LiveData<NetworkState>
)
