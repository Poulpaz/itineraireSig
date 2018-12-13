package com.chastagnier.reffay.appsig.utils

import android.util.Log
import com.chastagnier.reffay.appsig.model.GEO_ARC
import com.chastagnier.reffay.appsig.model.GEO_POINT
import com.chastagnier.reffay.appsig.model.Graph
import com.chastagnier.reffay.appsig.model.Point
import kotlin.collections.ArrayList
import android.system.Os.poll
import java.util.*


class ParcoursEnLargeur(graph: Graph) {

    private var adj = ArrayList<LinkedList<Int>>() //liste d'adjacence
    private var points = ArrayList<GEO_POINT>()
    private var arcs = ArrayList<GEO_ARC>()

    init {
        this.points = ArrayList<GEO_POINT>(graph.listPoint)
        this.arcs = ArrayList<GEO_ARC>(graph.listArc)

        for (i in points.indices) {
            adj[i] = LinkedList<Int>()
        }
        for (i in arcs.indices) {
            adj[arcs[i].deb].add(arcs[i].fin)
        }
    }

    fun execute(s: GEO_POINT): List<Int> {
        val liste_parcouru = ArrayList<Int>()
        val visited = BooleanArray(points.size)
        val queue = LinkedList<Int>()
        visited[s.id] = true
        queue.add(s.id)
        while (queue.size !== 0) {
            val poll = queue.poll()
            liste_parcouru.add(poll)
            val i = adj[poll].listIterator()
            while (i.hasNext()) {
                val n = i.next()
                if (!visited[n]) {
                    visited[n] = true
                    queue.add(n)
                }
            }
        }
        return liste_parcouru
    }
}