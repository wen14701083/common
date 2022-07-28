package com.xhd.base.widget

import android.content.Context
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import com.xhd.base.R

/**
 * 6位数方块验证码
 * Create by wk on 2021/8/19
 */
class VerificationCodeView : FrameLayout {

    companion object {

        private const val MAX_LENGTH = 6
    }

    private val sb = StringBuffer()
    private var completeListener: InputCompleteListener? = null
    private val mTextArray: Array<TextView?> = arrayOfNulls(MAX_LENGTH)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attr: AttributeSet?) : this(context, attr, 0)
    constructor(
        context: Context,
        attr: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attr, defStyleAttr)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.verification_code_view, null)
        addView(view)
        val tv1: TextView = view.findViewById(R.id.tv_1)
        val tv2: TextView = view.findViewById(R.id.tv_2)
        val tv3: TextView = view.findViewById(R.id.tv_3)
        val tv4: TextView = view.findViewById(R.id.tv_4)
        val tv5: TextView = view.findViewById(R.id.tv_5)
        val tv6: TextView = view.findViewById(R.id.tv_6)
        val etCode: EditText = view.findViewById(R.id.et_code)
        etCode.isCursorVisible = false

        mTextArray[0] = tv1
        mTextArray[1] = tv2
        mTextArray[2] = tv3
        mTextArray[3] = tv4
        mTextArray[4] = tv5
        mTextArray[5] = tv6

        etCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable) {
                val str = editable.toString()
                if (TextUtils.isEmpty(str)) {
                    return
                }

                if (sb.length < MAX_LENGTH) {
                    sb.append(str)
                    if (sb.length == MAX_LENGTH && completeListener != null) {
                        completeListener!!.onComplete(sb.toString())
                    }
                }
                etCode.setText("")
                setTextViewValue()
            }
        })

        etCode.setOnKeyListener(object : OnKeyListener {
            override fun onKey(view: View, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN) {
                    if (sb.isEmpty()) {
                        return true
                    }
                    sb.deleteCharAt(sb.length - 1)
                    setTextViewValue()
                    return true
                }
                return false
            }
        })
    }

    private fun setTextViewValue() {
        val length = sb.length
        for (i in mTextArray.indices) {
            if (i < length) {
                mTextArray[i]?.text = sb[i].toString()
            } else {
                mTextArray[i]?.text = ""
            }
        }
    }

    fun setCompleteListener(completeListener: InputCompleteListener) {
        this.completeListener = completeListener
    }

    interface InputCompleteListener {

        fun onComplete(result: String)
    }
}
