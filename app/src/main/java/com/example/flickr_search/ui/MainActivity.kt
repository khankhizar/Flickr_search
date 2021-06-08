package com.example.flickr_search.ui

import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.flickr_search.R
import com.example.flickr_search.data.*
import com.example.flickr_search.data.NetworkState.Companion.error
import com.example.flickr_search.extension.injectViewModel
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.no_result_layout.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector() = dispatchingAndroidInjector
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var searchVM: SearchVM
    val adapter = SearchAdapter()


    private var oldQuery: String = "kittens"
    private var newQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchVM = injectViewModel(viewModelFactory)

        val config = resources.configuration
        rvPhotos.layoutManager = GridLayoutManager(this,3,getOrientation(config),false)

        rvPhotos.adapter = adapter

        etText.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && etText.text.toString().isNotEmpty()) {
                newQuery = etText.text.toString()
                etText.text?.clear()

                searchPhotos(newQuery)
            }
            true
        }

    }

    override fun onResume() {
        super.onResume()

        if (searchVM.oldQuery.isNotEmpty())
            oldQuery = searchVM.oldQuery

        searchPhotos()
    }


    private fun searchPhotos(query: String = oldQuery) {
        dismissKeyboard()
        val data  =searchVM.search(query)



        data?.pagedList?.observe(this, Observer {

            adapter.submitList(it)

        })

        data?.networkState?.observe(this, Observer {

            when (it.status) {
                Status.RUNNING -> {
                }
                Status.SUCCESS -> {
                    show(LIST)
                    oldQuery = newQuery
                }
                Status.FAILED -> {
                    show(ERROR)
                    searchVM.oldQuery  = ""
                    oldQuery = ""
                }
                Status.NO_DATA -> {
                    if (adapter.itemCount  == 0 || oldQuery != newQuery )
                        show(NO_DATA)

                }
            }
        })


    }


    @RecyclerView.Orientation
    private fun getOrientation(config: Configuration): Int {
        return when (config.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                LinearLayoutManager.VERTICAL
            }
            Configuration.ORIENTATION_LANDSCAPE -> {
                LinearLayoutManager.HORIZONTAL
            }
            else -> {
                throw AssertionError("This should not be the case.")
            }
        }
    }

    private fun dismissKeyboard() {
        etText.clearFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etText.windowToken, 0)
    }


    private fun show(what:Int = LIST) {

        when (what) {
            LIST -> {
                rvPhotos.visibility = View.VISIBLE
                llNoResults.visibility = View.GONE
            }

            ERROR -> {
                setDetails(R.string.error,R.string.try_later)
                llNoResults.visibility = View.VISIBLE
                rvPhotos.visibility = View.GONE
            }

            NO_DATA -> {
                setDetails()
                llNoResults.visibility = View.VISIBLE
                rvPhotos.visibility = View.GONE
            }
        }
    }

    private fun setDetails(@StringRes title:Int = R.string.no_results,
                           @StringRes message: Int = R.string.try_new) {
        ivTitle.text = resources.getString(title)
        ivMessage.text = resources.getString(message)
    }

    fun setTestViewModel(viewModel: SearchVM) {
        searchVM = viewModel
    }


}