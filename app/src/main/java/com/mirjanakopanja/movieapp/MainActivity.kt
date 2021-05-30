package com.mirjanakopanja.movieapp


import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mirjanakopanja.movieapp.databinding.MainActivityBinding
import com.mirjanakopanja.movieapp.ui.SettingsFragment
import com.mirjanakopanja.movieapp.ui.main.MainFragment
import com.mirjanakopanja.movieapp.ui.maps.MapsFragment
import com.mirjanakopanja.movieapp.ui.search.SearchFragment


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(findViewById(R.id.toolbar))
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Fresco.initialize(this)
        initNavigationMenu()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }

    private fun initNavigationMenu() {
        val navigationView: BottomNavigationView = findViewById(R.id.bottomNavView)
        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.itemIconTintList = null
        navigationView.selectedItemId = R.id.navigation_home
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.navigation_home -> {
                loadFragment(MainFragment.newInstance())
                return true
            }
            R.id.navigation_map -> {
                loadFragment(MapsFragment.newEmptyInstance())
                return true
            }
            R.id.navigation_settings -> {
                loadFragment(SettingsFragment.newInstance())
                return true
            }
            R.id.navigationSearch -> {
                loadFragment(SearchFragment.newInstance())
                return true
            }
        }
        false
        return true
    }


    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("")
                .commitAllowingStateLoss()
            return true
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}