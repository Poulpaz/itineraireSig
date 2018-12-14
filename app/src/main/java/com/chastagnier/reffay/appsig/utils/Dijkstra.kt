package com.chastagnier.reffay.appsig.utils

import android.util.Log
import com.chastagnier.reffay.appsig.model.GEO_ARC
import com.chastagnier.reffay.appsig.model.GEO_POINT
import com.chastagnier.reffay.appsig.model.Graph
import java.util.*


class Dijkstra(graph: Graph) {

    private val listPoint: List<GEO_POINT>
    private val listArc: List<GEO_ARC>
    var settledNodes: Set<GEO_POINT> = HashSet()
    var unSettledNodes: Set<GEO_POINT> = HashSet()
    var predecessors: Map<GEO_POINT, GEO_POINT> = HashMap()
    var distance: Map<GEO_POINT, Float> = HashMap()

    init {
        this.listPoint = graph.listPoint
        this.listArc = graph.listArc
    }

    fun reset(){
        settledNodes = HashSet()
        unSettledNodes = HashSet()
        predecessors = HashMap()
        distance = HashMap()
    }

    fun getResult(source: GEO_POINT, target: GEO_POINT): LinkedList<GEO_POINT>? {
        execute(source)
        val path = LinkedList<GEO_POINT>()
        var etape = target
        if (predecessors[etape] == null) {
            return null
        }
        path.add(etape)
        while (predecessors[etape] != null) {
            etape = predecessors[etape]!!
            path.add(etape)
        }
        path.reverse()
        return path
    }

    fun execute(source: GEO_POINT) {
        distance += source to 0.0f
        unSettledNodes += source
        while (unSettledNodes.size > 0) {
            val node = getMinimum(unSettledNodes)
            node?.let {
                settledNodes += node
                unSettledNodes -= node
                findMinimalDistances(node)
            }
        }
    }

    private fun findMinimalDistances(node: GEO_POINT) {
        val adjacentNodes = getNeighbors(node)
        for (target in adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                distance += target to (getShortestDistance(node) + getDistance(node, target))
                predecessors += target to node
                unSettledNodes += target
            }
        }

    }

    private fun getDistance(node: GEO_POINT, target: GEO_POINT): Float {
        for (edge in listArc) {
            if (edge.deb == node.id && edge.fin == target.id) {
                return edge.distance
            }
        }
        throw RuntimeException("Should not happen")
    }

    private fun getNeighbors(node: GEO_POINT): List<GEO_POINT> {
        val neighbors = ArrayList<GEO_POINT>()
        for (edge in listArc) {
            val point = listPoint.find { it.id == edge.fin }
            if ( node.id == edge.deb && !isSettled(point)) {
                Log.d("TESTTPOINT", point.toString())
                if(point != null) neighbors.add(point)
            }
        }
        return neighbors
    }

    private fun getMinimum(vertexes: Set<GEO_POINT>): GEO_POINT? {
        var minimum: GEO_POINT? = null
        for (vertex in vertexes) {
            if (minimum == null) {
                minimum = vertex
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex
                }
            }
        }
        return minimum
    }

    private fun isSettled(vertex: GEO_POINT?): Boolean {
        return settledNodes.contains(vertex)
    }

    private fun getShortestDistance(destination: GEO_POINT?): Float {
        val d = distance[destination]
        return d ?: Float.MAX_VALUE
    }

}