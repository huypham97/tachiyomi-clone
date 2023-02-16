package com.example.tachiyomi_clone.son_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.ActivitySonTrangChuBinding
import com.example.tachiyomi_clone.databinding.HomeFragmentBinding

class Son_TrangChu : AppCompatActivity() {
    lateinit var binding: ActivitySonTrangChuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_son_trang_chu)

        binding = ActivitySonTrangChuBinding.inflate(layoutInflater)

        val homeFragment = HomeFragment()

        setCurrentFragment(homeFragment)

        binding.bnTrangChu.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.bn_home->setCurrentFragment(homeFragment)
                R.id.bn_newstory->setCurrentFragment(homeFragment)
                R.id.bn_self->setCurrentFragment(homeFragment)

            }
            true
        }

    }

    fun setCurrentFragment (fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_trangchu, fragment)
            commit()
        }
    }
}