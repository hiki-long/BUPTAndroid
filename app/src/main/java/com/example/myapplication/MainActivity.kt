package com.example.myapplication

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_todo, R.id.navigation_view, R.id.navigation_course, R.id.navigation_more))
//        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setSupportActionBar(toolbar)

        //tobar drawer open button
        toolbar.setNavigationOnClickListener{
            if(navView.selectedItemId==R.id.navigation_todo ) {
                if (mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mainDrawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    mainDrawerLayout.openDrawer(GravityCompat.START)
                }
            }
        }

        supportActionBar?.let{
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
        }

//        when bottom navigationView is seletected
//        注意！：日后支持滑动切换fragment的时候不知道此判断还是否有效，**潜在bug**
//        navView.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.navigation_todo->{
//
//                    supportActionBar?.let{
//                        it.setDisplayHomeAsUpEnabled(true)
//                        it.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24)
//                    }
//                    mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
//                    true
//                }
//                R.id.navigation_course->{
//                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
//                    mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//                    true
//                }
//                R.id.navigation_more->{
//                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
//                    mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//                    true
//                }
//                R.id.navigation_view->{
//                    supportActionBar?.setDisplayHomeAsUpEnabled(false)
//                    mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
//                    true
//                }
//                else->false
//            }
//        }
        val toggle =ActionBarDrawerToggle(this,mainDrawerLayout,R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        mainDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    //依附于MainActivity的fragment需要在onViewCreated调用needDrawer函数决定是否打开drawer。
    fun needDrawer(judge: Boolean){
        if(judge){
            mainDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        else{
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            mainDrawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }

    }
}