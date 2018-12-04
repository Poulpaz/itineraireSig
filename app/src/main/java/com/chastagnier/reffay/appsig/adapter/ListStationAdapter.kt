package com.chastagnier.reffay.appsig.adapter

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chastagnier.reffay.appsig.R
import com.chastagnier.reffay.appsig.model.GEO_POINT
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_station.view.*

class ListStationAdapter() : ListAdapter<GEO_POINT, ListStationAdapter.StationViewHolder>(DiffStationCallback()) {

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    val indexClickLike: PublishSubject<Int> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_station, parent, false)
        return StationViewHolder(view)
    }

    inner class StationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(station: GEO_POINT) {
            itemView.item_station_id.text = station.id.toString()
            itemView.item_station_latitude.text = station.latitude.toString()
            itemView.item_station_longitude.text = station.longitude.toString()
            itemView.item_station_name.text = station.nom
            itemView.item_station_partition.text = station.partition.toString()
            bindPositionClick(station.id)
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