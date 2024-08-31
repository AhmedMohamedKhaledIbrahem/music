package com.example.music

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.music.data.NavigationViewModel
import com.example.music.fragment.MusicTabFragment
import com.example.music.fragment.SongFragment
import com.example.music.fragment.WatchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class Music : AppCompatActivity() {
    private val songFragment = SongFragment()
    private val musicTaps = MusicTabFragment()
    private val watchFragment = WatchFragment()
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var  nViewModel  :NavigationViewModel

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.my_music_activity)
        val navDrawerLayout: DrawerLayout = findViewById(R.id.navDrawerLayout)
        val cardContainer: LinearLayout = findViewById(R.id.card_container)
        val navigationView: NavigationView = findViewById(R.id.navView)
        val toolbar: Toolbar = findViewById(R.id.topAppBar)
        nViewModel = ViewModelProvider(this)[NavigationViewModel::class.java]
        MusicUtilities.appComponent = this
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                0
            )
        }

        setSupportActionBar(toolbar)
        addFragment(musicTaps)
        navControl()
        navigationDrawerLayout(navDrawerLayout, toolbar)
        navigationDrawerView(navigationView)
        cardItemsStateObserveViewModel(cardContainer)


    }

    private fun cardItemsStateObserveViewModel(cardContainer: LinearLayout){
        nViewModel.showState.observe(this, Observer { show->
            Log.e("showObserve","$show")
            if (show){
                cardItems(cardContainer)
            }else{
                hiddenCardItems(cardContainer)

            }
        })
    }


    private fun cardItems(cardContainer: LinearLayout) {
            val card1 = cardContainer.getChildAt(0)
            card1.visibility = LinearLayout.VISIBLE
            card1.findViewById<ImageView>(R.id.card_icon)
                .setImageResource(R.drawable.baseline_favorite_card)
            card1.findViewById<TextView>(R.id.card_label).text = getString(R.string.favourites)
            card1.background = getDrawable(R.drawable.favourites_card_colors)
            card1.setOnClickListener {
                Toast.makeText(applicationContext, "lol", Toast.LENGTH_SHORT).show()
            }

            val card2 = cardContainer.getChildAt(1)
             card2.visibility = LinearLayout.VISIBLE
            card2.findViewById<ImageView>(R.id.card_icon)
                .setImageResource(R.drawable.baseline_library_music_24)
            card2.findViewById<TextView>(R.id.card_label).text = getString(R.string.playLists)
            card2.background = getDrawable(R.drawable.playlists_card_color)

            val card3 = cardContainer.getChildAt(2)
            card3.visibility = LinearLayout.VISIBLE
            card3.findViewById<ImageView>(R.id.card_icon)
                .setImageResource(R.drawable.baseline_access_time_filled_24)
            card3.findViewById<TextView>(R.id.card_label).text = getString(R.string.recent)
            card3.background = getDrawable(R.drawable.recent_card_color)


    }
    private fun hiddenCardItems(cardContainer: LinearLayout){
        for (i in 0 until cardContainer.childCount){
            cardContainer.getChildAt(i).visibility = LinearLayout.GONE
        }
    }


    private fun addFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, fragment)
        transaction.commit()


    }

    private fun replaceFragment(fragment: Fragment, test: String) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
            .commit()
        Toast.makeText(applicationContext, test, Toast.LENGTH_SHORT).show()
    }

    private fun navControl() {

        var buttonNav: BottomNavigationView = findViewById(R.id.bottomNav)
        buttonNav.setOnItemSelectedListener { item ->

            when (item.itemId) {
                R.id.myMusic -> {
                    replaceFragment(musicTaps, "MusicTapFragment")
                    nViewModel.stateVisibility(true)
                    Log.e("showStatView1","${nViewModel.showState.value}")
                    true
                }

                R.id.watch -> {
                    replaceFragment(watchFragment, "WatchFragment")
                    nViewModel.stateVisibility(false)
                    Log.e("showStatView2","${nViewModel.showState.value}")
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun navigationDrawerLayout(
        navDrawerLayout: DrawerLayout,
        toolbar: Toolbar
    ) {
        toggle =
            ActionBarDrawerToggle(this, navDrawerLayout, toolbar, R.string.open, R.string.close)
        navDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun navigationDrawerView(navigationView: NavigationView) {

        navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.downloads -> {


                    Toast.makeText(this, "hello download", Toast.LENGTH_SHORT).show()
                }

                R.id.settings -> {

                    Toast.makeText(this, "hello download", Toast.LENGTH_SHORT).show()
                }

                R.id.languages -> {

                    Toast.makeText(this, "hello download", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}