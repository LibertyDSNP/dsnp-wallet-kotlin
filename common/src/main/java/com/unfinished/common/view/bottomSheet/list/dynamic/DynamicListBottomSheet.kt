package com.unfinished.common.view.bottomSheet.list.dynamic

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.unfinished.common.R
import com.unfinished.common.utils.DialogExtensions
import com.unfinished.common.utils.WithContextExtensions

typealias ClickHandler<T> = (T) -> Unit

class ReferentialEqualityDiffCallBack<T> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return true
    }
}

abstract class BaseDynamicListBottomSheet(context: Context) :
    BottomSheetDialog(context, R.style.BottomSheetDialog),
    WithContextExtensions by WithContextExtensions(context),
    DialogExtensions {

    override val dialogInterface: DialogInterface
        get() = this

    protected val container: LinearLayout
        get() = findViewById(R.id.dynamicListSheetItemContainer)!!

    protected val headerView: View
        get() = findViewById(R.id.dynamicListSheetHeader)!!

    protected val recyclerView: RecyclerView
        get() = findViewById(R.id.dynamicListSheetContent)!!

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.bottom_sheet_dynamic_list)
        super.onCreate(savedInstanceState)
    }

    final override fun setTitle(title: CharSequence?) {
        findViewById<TextView>(R.id.dynamicListSheetTitle)?.text = title
    }

    final override fun setTitle(titleId: Int) {
        findViewById<TextView>(R.id.dynamicListSheetTitle)?.setText(titleId)
    }

//    fun setupRightAction(
//        @DrawableRes drawableRes: Int,
//        onClickListener: View.OnClickListener
//    ) {
//        dynamicListSheetRightAction.setImageResource(drawableRes)
//        dynamicListSheetRightAction.setVisible(true)
//        dynamicListSheetRightAction.setOnClickListener(onClickListener)
//    }
}

abstract class DynamicListBottomSheet<T>(
    context: Context,
    private val payload: Payload<T>,
    private val diffCallback: DiffUtil.ItemCallback<T>,
    private val onClicked: ClickHandler<T>,
    private val onCancel: (() -> Unit)? = null,
) : BaseDynamicListBottomSheet(context), DynamicListSheetAdapter.Handler<T> {

    open class Payload<out T>(val data: List<T>, val selected: T? = null)

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dynamicListSheetContent: RecyclerView = findViewById(R.id.dynamicListSheetContent)!!
        dynamicListSheetContent.setHasFixedSize(true)

        val adapter = DynamicListSheetAdapter(payload.selected, this, diffCallback, holderCreator())
        dynamicListSheetContent.adapter = adapter

        adapter.submitList(payload.data)

        setOnCancelListener { onCancel?.invoke() }
    }

    abstract fun holderCreator(): HolderCreator<T>

    override fun itemClicked(item: T) {
        onClicked(item)

        dismiss()
    }
}
