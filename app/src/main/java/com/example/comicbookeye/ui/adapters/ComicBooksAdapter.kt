package com.example.comicbookeye.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.comicbookeye.R
import com.example.comicbookeye.helpers.ComicBookStatusEnum
import com.example.comicbookeye.helpers.comicBookStatus
import com.example.comicbookeye.helpers.customToString
import com.example.comicbookeye.helpers.index
import com.example.comicbookeye.infrastructure.view_model.ComicBookViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ComicBooksAdapter(context: Context, viewModel: ComicBookViewModel) :
    RecyclerView.Adapter<ViewHolder>() {

    private val dataSet = ArrayList<ComicBookAdapterModel>()

    private var mContext: Context
    private var mViewModel: ComicBookViewModel

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(
        newList: List<ComicBookAdapterModel>,
    ) {
        dataSet.clear()
        dataSet.addAll(newList)
        notifyDataSetChanged()
    }

    class TitleTypeViewHolder(itemView: View) : ViewHolder(itemView) {
        var title: TextView

        init {
            title = itemView.findViewById(R.id.card_title_section_id)
        }
    }

    class ComicBookTypeViewHolder(itemView: View) : ViewHolder(itemView) {
        var content: CardView
        var title: TextView
        var status: TextView
        var description: TextView
        var image: ImageView
        var statusIcon: ImageView

        init {
            content = itemView.findViewById(R.id.card_content)
            title = itemView.findViewById(R.id.card_content_title)
            status = itemView.findViewById(R.id.card_content_status)
            description = itemView.findViewById(R.id.card_content_description)
            image = itemView.findViewById(R.id.card_content_image)
            statusIcon = itemView.findViewById(R.id.card_status_image)
        }
    }

    init {
        mContext = context
        mViewModel = viewModel
    }

    /*
    In base al viewType che setto nel metodo getItemViewType, vado a fare l'inflate della ui corretta.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View
        when (viewType) {
            ComicBookAdapterModel.TITLE -> {
                view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.card_title_section, parent, false)
                return TitleTypeViewHolder(view)
            }
            ComicBookAdapterModel.CONTENT -> {
                view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.card_content, parent, false)
                return ComicBookTypeViewHolder(view)
            }
            else -> {
                view =
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.card_unsupported_format, parent, false)
                return ComicBookTypeViewHolder(view)
            }
        }
    }

    /*
    Mi recupero il type dal dataset attraverso il position che mi è stato fornito dal metodo.
    In base al type faccio il binding degli elementi grafici.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model: ComicBookAdapterModel = dataSet[position]
        when (model.type) {
            ComicBookAdapterModel.TITLE -> {
                (holder as TitleTypeViewHolder).title.text = model.title
            }
            ComicBookAdapterModel.CONTENT -> {
                (holder as ComicBookTypeViewHolder).content.setOnClickListener {
                    val dialog = BottomSheetDialog(mContext)
                    val view = dialog.layoutInflater.inflate(R.layout.bottom_sheet, null)

                    val containerPresent: LinearLayout =
                        view.findViewById(R.id.linear_layout_container_comic_present)
                    val containerOrdered: LinearLayout =
                        view.findViewById(R.id.linear_layout_container_comic_ordered)
                    val containerAbsent: LinearLayout =
                        view.findViewById(R.id.linear_layout_container_comic_absent)
                    when (model.comicBook!!.status.comicBookStatus()) {
                        ComicBookStatusEnum.PRESENT -> {
                            containerPresent.visibility =
                                GONE
                        }
                        ComicBookStatusEnum.ORDERED -> {
                            containerOrdered.visibility =
                                GONE
                        }
                        ComicBookStatusEnum.ABSENT -> {
                            containerAbsent.visibility =
                                GONE
                        }
                        else -> {}
                    }

                    containerPresent.setOnClickListener {
                        mViewModel.updateStatus(
                            ComicBookStatusEnum.PRESENT.index(),
                            model.comicBook!!.id
                        )
                        dialog.dismiss()
                    }
                    containerOrdered.setOnClickListener {
                        mViewModel.updateStatus(
                            ComicBookStatusEnum.ORDERED.index(),
                            model.comicBook!!.id
                        )
                        dialog.dismiss()
                    }
                    containerAbsent.setOnClickListener {
                        mViewModel.updateStatus(
                            ComicBookStatusEnum.ABSENT.index(),
                            model.comicBook!!.id
                        )
                        dialog.dismiss()
                    }

                    dialog.setCancelable(true)
                    dialog.setContentView(view)
                    dialog.show()
                }
                holder.title.text = model.comicBook!!.title
                holder.status.text = model.comicBook!!.status.comicBookStatus().customToString()
                holder.description.text = model.comicBook!!.description

                val resource = when (model.comicBook!!.status.comicBookStatus()) {
                    ComicBookStatusEnum.PRESENT -> R.drawable.present_dot_circle
                    ComicBookStatusEnum.ORDERED -> R.drawable.ordered_dot_circle
                    ComicBookStatusEnum.ABSENT -> R.drawable.absent_dot_circle
                    ComicBookStatusEnum.NOT_DEFINED -> R.drawable.unknown_dot_circle
                }

                holder.statusIcon.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        mContext.resources,
                        resource,
                        null
                    )
                )
                Glide.with(mContext).load(model.comicBook!!.image).into(holder.image)
            }
        }
    }

    /*
     Mi permette di scegliere quale view utilizzare.
     */
    override fun getItemViewType(position: Int): Int {
        return when (dataSet[position].type) {
            0 -> ComicBookAdapterModel.TITLE
            1 -> ComicBookAdapterModel.CONTENT
            else -> -1
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}