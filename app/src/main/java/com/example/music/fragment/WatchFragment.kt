package com.example.music.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.music.R
import com.example.music.adapter.VideoAdapter
import com.example.music.data.viewmodel.NavigationBottomBarViewModel
import com.example.music.utils.RetrofitClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class WatchFragment : Fragment() {
    private lateinit var videoRecyclerView: RecyclerView
    private lateinit var videoAdapter: VideoAdapter
    private lateinit var searchBar: SearchView
    private lateinit var bottomNav :BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_watch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoRecyclerView = view.findViewById(R.id.video_recycleView)
        videoRecyclerView.layoutManager = LinearLayoutManager(context)
        searchBar(view)
        bottomNav = requireActivity().findViewById(R.id.bottomNav)
        scrollListener(bottomNav)

    }

    private fun scrollListener(

        bottomNav: BottomNavigationView, ) {
        videoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0){

                    bottomNav.visibility = View.GONE
                   // Log.e("onScrolled", "onScrolled:down ", )
                }else if (dy < 0){
                   // Log.e("onScrolled", "onScrolled:up ", )

                    bottomNav.visibility = View.VISIBLE
                }

            }
        }
        )
    }

    private fun searchBar(view: View) {

        searchBar = view.findViewById(R.id.search_view)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    // Perform the search using the query
                    loadData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }


    @SuppressLint("CheckResult")
    fun loadData(search: String) {

        RetrofitClient
            .api
            .searchVideos(
                part = "snippet",
                query = search,
                apiKey = "your api key from youtube data V3",
                type = "video",
                maxResult = 30
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    videoAdapter = VideoAdapter(response.items)
                    videoRecyclerView.adapter = videoAdapter

                },
                { error ->
                    Log.e("Error", "${error.message}")
                }
            )

    }
}


