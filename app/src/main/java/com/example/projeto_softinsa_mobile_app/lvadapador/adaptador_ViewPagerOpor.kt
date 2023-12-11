package com.example.projeto_softinsa_mobile_app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class Adaptador_ViewPagerOpor (fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return oportunidadeFragment.newInstance(position)
    }

    override fun getCount(): Int {
        return 4
    }
    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Negócios"
            1 -> "Parcerias"
            2 -> "Investimentos"
            3 -> "Projetos"
            else -> null
        }
    }
    }
