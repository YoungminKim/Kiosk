package kr.co.releasetech.kiosk.view.activity.menuSetting

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kr.co.releasetech.kiosk.view.fragment.category.CategoryFragment
import kr.co.releasetech.kiosk.view.fragment.goods.GoodsFragment
import kr.co.releasetech.kiosk.view.fragment.option.OptionFragment

class MenuSettingPagerAdapter(fa: FragmentActivity): FragmentStateAdapter(fa) {

    private val fragments = arrayOf(CategoryFragment(), GoodsFragment(), OptionFragment())

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}