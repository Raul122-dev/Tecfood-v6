package com.example.tecfoodv6.Utils.TabNavigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MainAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    private val fragmentList = ArrayList<Fragment>()
    private val stringList = ArrayList<String>()


    fun addFragment(fragment: Fragment, s: String) {
        fragmentList.add(fragment)
        stringList.add(s)
    }


    override fun getCount(): Int {
        return fragmentList.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return stringList[position]
    }
}