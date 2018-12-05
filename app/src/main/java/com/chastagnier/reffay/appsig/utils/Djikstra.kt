package com.chastagnier.reffay.appsig.utils

import android.support.v4.view.MotionEventCompat.getSource
import android.util.Half.toFloat
import com.chastagnier.reffay.appsig.model.GEO_ARC
import com.chastagnier.reffay.appsig.model.GEO_POINT
import com.chastagnier.reffay.appsig.model.Graph
import java.util.*


class Dijkstra(graph: Graph) {

    private val nodes: List<GEO_POINT>
    private val edges: List<GEO_ARC>
    var settledNodes: Set<GEO_POINT> = HashSet()
    var unSettledNodes: Set<GEO_POINT> = HashSet()
    var predecessors: Map<GEO_POINT, GEO_POINT> = HashMap()
    var distance: Map<GEO_POINT, Float> = HashMap()

    init {
        // create a copy of the array so that we can operate on this array
        this.nodes = graph.listPoint
        this.edges = graph.listArc
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
                distance.plus(target to (getShortestDistance(node) + getDistance(node, target)))
                predecessors.plus(target to node)
                unSettledNodes.plus(target)
            }
        }

    }

    private fun getDistance(node: GEO_POINT?, target: GEO_POINT): Float {
        for (edge in edges) {
            if (edge.deb.equals(node) && edge.fin.equals(target)) {
                return edge.temps
            }
        }
        throw RuntimeException("Should not happen")
    }

    private fun getNeighbors(node: GEO_POINT): List<GEO_POINT> {
        val neighbors = ArrayList<GEO_POINT>()
        for (edge in edges) {
            if (nodes.find { it.id == edge.deb } == node && !isSettled(nodes.find { it.id == edge.fin })) {
                val point = nodes.find { it.id == edge.fin }
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

    fun getPath(target: GEO_POINT): LinkedList<GEO_POINT>? {
        val path = LinkedList<GEO_POINT>()
        var step = target
        // check if a path exists
        if (predecessors[step] == null) {
            return null
        }
        path.add(step)
        while (predecessors[step] != null) {
            step = predecessors[step]!!
            path.add(step)
        }
        // Put it into the correct order
        Collections.reverse(path)
        return path
    }

}