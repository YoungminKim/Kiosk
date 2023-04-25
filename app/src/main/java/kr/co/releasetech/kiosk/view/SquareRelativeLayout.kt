package kr.co.releasetech.kiosk.view

import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout

/**

 * @author:young
 * @CreatedDate : 2021-04-19 오후 7:44
 * @PackageName : kr.co.unboxer.view
 * @ClassName: SquareRelativeLayout
 * @Description:

 */
class SquareRelativeLayout: RelativeLayout {

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context,  attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec)
    }

}