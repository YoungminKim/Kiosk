package kr.co.releasetech.kiosk.view.activity.menuSetting

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kr.co.releasetech.kiosk.R
import kr.co.releasetech.kiosk.databinding.ActivityMenuSettingBinding
import kr.co.releasetech.kiosk.utils.WindowUtils.setImmersiveMode
import kr.co.releasetech.kiosk.view.ZoomOutPageTransformer
import kr.co.releasetech.kiosk.view.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import splitties.views.onClick


class MenuSettingActivity: BaseActivity<ActivityMenuSettingBinding>(R.layout.activity_menu_setting) {
    companion object{
        const val TAG = "AdminActivity"
    }


    val viewModel: MenuSettingViewModel by viewModel()
    private lateinit var tabTvs: Array<TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = viewModel
        binding.lifecycleOwner = this


        val adapter = MenuSettingPagerAdapter(this)

        with(binding){
            includeTitleV.title = getString(R.string.manager_menu_02)
            includeTitleV.backIv.onClick{
                finish()
            }

            tabTvs = arrayOf(tabTv01, tabTv02, tabTv03)
            tabTvs[0].isSelected = true
            for (i in tabTvs.indices){
                val tabTv = tabTvs[i]
                tabTv.text = resources.getStringArray(R.array.admin_arr)[i]
                tabTv.onClick{
                    for (j in tabTvs.indices){
                        vp.currentItem = i
                        if( i == j){
                            tabTvs[j].setTextColor(Color.WHITE)
                            tabTvs[j].isSelected = true
                        }else{
                            tabTvs[j].setTextColor(ContextCompat.getColor(this@MenuSettingActivity, R.color.default_textcolor))
                            tabTvs[j].isSelected = false
                        }

                    }
                }
            }

            vp.adapter = adapter
            vp.setPageTransformer(ZoomOutPageTransformer())
            vp.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    for (i in tabTvs.indices){
                        if( i == position){
                            tabTvs[i].setTextColor(Color.WHITE)
                            tabTvs[i].isSelected = true
                        }else{
                            tabTvs[i].setTextColor(ContextCompat.getColor(this@MenuSettingActivity, R.color.default_textcolor))
                            tabTvs[i].isSelected = false
                        }

                    }
                }
            })


     /*       TabLayoutMediator(tl, vp){ tab, position ->
                tab.text = resources.getStringArray(R.array.admin_arr)[position]
            }.attach()*/
        }

        viewModel.onClose.observe(this) {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()

        setImmersiveMode(window)
    }

}