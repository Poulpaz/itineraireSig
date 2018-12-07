package com.chastagnier.reffay.appsig.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chastagnier.reffay.appsig.R
import com.chastagnier.reffay.appsig.model.GEO_ARC
import com.chastagnier.reffay.appsig.model.GEO_POINT
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_arc.view.*
import kotlinx.android.synthetic.main.item_point.view.*

class ListCalculAdapter() : ListAdapter<GEO_POINT, ListCalculAdapter.CalculViewHolder>(DiffStationCallback()) {

    override fun onBindViewHolder(holder: CalculViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    val indexClickLike: PublishSubject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalculViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_point, parent, false)
        return CalculViewHolder(view)
    }

    inner class CalculViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(point: GEO_POINT) {
            itemView.item_point_id.text = point.id.toString()
            itemView.item_point_latitude.text = point.latitude.toString()
            itemView.item_point_longitude.text = point.longitude.toString()
            itemView.item_point_name.text = point.nom
            itemView.item_point_partition.text = point.partition.toString()
            bindPositionClick(point.id)
        }

        private fun bindPositionClick(stationId: Int) {
            itemView.clicks()
                    .takeUntil(RxView.detaches(itemView))
                    .filter { adapterPosition != RecyclerView.NO_POSITION }
                    .subscribe { indexClickLike.onNext(stationId) }
        }
    }

    class DiffStationCallback : DiffUtil.ItemCallback<GEO_POINT>() {

        override fun areItemsTheSame(p0: GEO_POINT, p1: GEO_POINT): Boolean = p0.id == p1.id

        override fun areContentsTheSame(p0: GEO_POINT, p1: GEO_POINT): Boolean = p0.id == p1.id

    }

}