package com.example.myapplication

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var toolbar:androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar=findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_todo, R.id.navigation_view, R.id.navigation_course, R.id.navigation_more))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ordinarylist,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.editlist->{

            }
            R.id.deletelist->{

            }
            R.id.sort->{
                val myItems=listOf("清单","创建日期","截至日期","执行事件","重要级")
                MaterialDialog(this).show{
                    cornerRadius(16f)
                    listItemsSingleChoice(items = myItems){ dialog, index, text ->
                        when(index){
                            0->{
                                Toast.makeText(this.context,"you clicked 0", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    positiveButton(R.string.confirm)
                }
            }
            R.id.showCompleted->{

            }
        }
        return true;
    }
}