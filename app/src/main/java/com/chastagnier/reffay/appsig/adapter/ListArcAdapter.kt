package com.chastagnier.reffay.appsig.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chastagnier.reffay.appsig.R
import com.chastagnier.reffay.appsig.model.GEO_ARC
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_arc.view.*
import kotlinx.android.synthetic.main.item_point.view.*

class ListArcAdapter() : ListAdapter<GEO_ARC, ListArcAdapter.ArcViewHolder>(DiffStationCallback()) {

    override fun onBindViewHolder(holder: ArcViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    val indexClickLike: PublishSubject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArcViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_arc, parent, false)
        return ArcViewHolder(view)
    }

    inner class ArcViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(arc: GEO_ARC) {
            itemView.item_arc_id.text = arc.id.toString()
            itemView.item_arc_deb.text = arc.deb.toString()
            itemView.item_arc_fin.text = arc.fin.toString()
            itemView.item_arc_temps.text = arc.temps.toString()
            itemView.item_arc_sens.text = arc.sens.toString()
            bindPositionClick(arc.id)
        }

        private fun bindPositionClick(stationId: Int) {
            itemView.clicks()
                    .takeUntil(RxView.detaches(itemView))
                    .filter { adapterPosition != RecyclerView.NO_POSITION }
                    .subscribe { indexClickLike.onNext(stationId) }
        }
    }

    class DiffStationCallback : DiffUtil.ItemCallback<GEO_ARC>() {

        override fun areItemsTheSame(p0: GEO_ARC, p1: GEO_ARC): Boolean = p0.id == p1.id

        override fun areContentsTheSame(p0: GEO_ARC, p1: GEO_ARC): Boolean = p0.id == p1.id

    }

}