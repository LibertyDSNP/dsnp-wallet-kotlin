package io.novafoundation.nova.common.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.res.getStringOrThrow
import androidx.lifecycle.LifecycleOwner
import io.novafoundation.nova.common.R
import io.novafoundation.nova.common.utils.WithContextExtensions
import io.novafoundation.nova.common.utils.setTextOrHide
import io.novafoundation.nova.common.utils.setVisible
import io.novafoundation.nova.common.utils.updatePadding
import io.novafoundation.nova.common.utils.useAttributes

class AdvertisementCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), WithContextExtensions by WithContextExtensions(context) {

    private val view: View = View.inflate(context, R.layout.view_advertisement_card, this)
    private val advertisementCardButton: PrimaryButton = view.findViewById(R.id.advertisementCardButton)
    private val advertisementCardImage: ImageView = view.findViewById(R.id.advertisementCardImage)
    private val advertisementCardLearnMoreArrow: ImageView = view.findViewById(R.id.advertisementCardLearnMoreArrow)
    private val advertisementCardLearnMoreContent: TextView = view.findViewById(R.id.advertisementCardLearnMoreContent)
    private val advertisementCardLearnMoreGroup: Group = view.findViewById(R.id.advertisementCardLearnMoreGroup)
    private val advertisementCardSubTitle: TextView = view.findViewById(R.id.advertisementCardSubTitle)
    private val advertisementCardTitle: TextView = view.findViewById(R.id.advertisementCardTitle)

    val action: PrimaryButton
        get() = advertisementCardButton

    init {
        View.inflate(context, R.layout.view_advertisement_card, this)

        attrs?.let(::applyAttrs)

        updatePadding(bottom = 16.dp)
    }

    fun setupAction(lifecycleOwner: LifecycleOwner, onClicked: (View) -> Unit) {
        action.prepareForProgress(lifecycleOwner)
        action.setOnClickListener(onClicked)
    }

    fun setOnLearnMoreClickedListener(onClicked: (View) -> Unit) {
        advertisementCardLearnMoreArrow.setOnClickListener(onClicked)
        advertisementCardLearnMoreContent.setOnClickListener(onClicked)
    }

    private fun applyAttrs(attributeSet: AttributeSet) = context.useAttributes(attributeSet, R.styleable.AdvertisementCard) {
        val actionLabel = it.getString(R.styleable.AdvertisementCard_action)
        action.setTextOrHide(actionLabel)

        val learnMore = it.getString(R.styleable.AdvertisementCard_learnMore)
        advertisementCardLearnMoreGroup.setVisible(learnMore != null)
        advertisementCardLearnMoreContent.text = learnMore

        val title = it.getStringOrThrow(R.styleable.AdvertisementCard_title)
        advertisementCardTitle.text = title

        val subtitle = it.getStringOrThrow(R.styleable.AdvertisementCard_subtitle)
        advertisementCardSubTitle.text = subtitle

        val image = it.getDrawable(R.styleable.AdvertisementCard_image)
        advertisementCardImage.setImageDrawable(image)
    }
}
