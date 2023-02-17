package com.example.tachiyomi_clone.son_ui

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.tachiyomi_clone.R
import com.example.tachiyomi_clone.databinding.HomeFragmentBinding
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment: Fragment() {
    lateinit var binding: HomeFragmentBinding

    var index = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPagerAdapter = ViewPagerAdapter(requireContext())
        binding.vp2Banner.adapter = viewPagerAdapter

        var listItem = ArrayList<Item_Home>()

        var itemDetailHome1 = Item_Detail_Home(R.drawable.avt_detail_ryc)
        var itemDetailHome2 = Item_Detail_Home(R.drawable.avt_detail_ryc)
        var itemDetailHome3 = Item_Detail_Home(R.drawable.avt_detail_ryc)
        var itemDetailHome4 = Item_Detail_Home(R.drawable.avt_detail_ryc)

        var itemHome1 = Item_Home("hello", itemDetailHome1)
        var itemHome2 = Item_Home("hello", itemDetailHome2)
        var itemHome3 = Item_Home("hello", itemDetailHome3)
        var itemHome4 = Item_Home("hello", itemDetailHome4)

        listItem.add(itemHome1)
        listItem.add(itemHome2)
        listItem.add(itemHome3)
        listItem.add(itemHome4)






        var adapter = Item_Home_Adapter(requireContext(), listItem)

        binding.rycTrangChu.adapter = adapter


        binding.btnKhac.setOnClickListener(View.OnClickListener {
            binding.btnKhac.setBackgroundResource(R.drawable.son_bg_button)
            binding.btnThieuNhi.setBackgroundResource(R.drawable.son_bg_button_khac)
            binding.btnThieuNhi.setTextColor(resources.getColor(R.color.xam))
            binding.btnKhac.setTextColor(resources.getColor(R.color.black))
        })

        binding.btnThieuNhi.setOnClickListener(View.OnClickListener {
            binding.btnKhac.setBackgroundResource(R.drawable.son_bg_button_khac)
            binding.btnThieuNhi.setBackgroundResource(R.drawable.son_bg_button)
            binding.btnThieuNhi.setTextColor(resources.getColor(R.color.black))
            binding.btnKhac.setTextColor(resources.getColor(R.color.xam))
        })





    }
}