import com.chastagnier.reffay.appsig.model.GEO_POINT
import com.chastagnier.reffay.appsig.model.Graph
import java.util.*

/**
 * Breadth first traversal leverages a [Queue] (FIFO).
 */
fun breadthFirstTraversal(graph: Graph,
                              startNode: GEO_POINT,
                              maxDepth: Int = Int.MAX_VALUE): String {
    //
    // Setup.
    //

    // Mark all the vertices / nodes as not visited. And keep track of sequence
    // in which nodes are visited, for return value.
    class VisitedMap {
        val traversalList = mutableListOf<GEO_POINT>()

        val visitedMap = mutableMapOf<GEO_POINT, Boolean>().apply {
            for (node in graph.listPoint) this[node] = false
        }

        fun isNotVisited(node: GEO_POINT): Boolean = !visitedMap[node]!!

        fun markVisitedAndAddToTraversalList(node: GEO_POINT) {
            visitedMap[node] = true
            traversalList.add(node)
        }
    }

    val visitedMap = VisitedMap()

    // Keep track of the depth of each node, so that more than maxDepth nodes
    // aren't visited.
    val depthMap = mutableMapOf<GEO_POINT, Int>().apply {
        for (node in graph.listPoint) this[node] = Int.MAX_VALUE
    }

    // Create a queue for BFS.
    class Queue {
        val deck: Deque<GEO_POINT> = ArrayDeque<GEO_POINT>()
        fun add(node: GEO_POINT, depth: Int) {
            // Add to the tail of the queue.
            deck.add(node)
            // Record the depth of this node.
            depthMap[node] = depth
        }

        fun addAdjacentNodes(currentNode: GEO_POINT, depth: Int) {
            graph.listArc.forEach({
                if (it.id == currentNode.id) {
                    val point = graph.listPoint.find { _point ->
                        it.fin == _point.id
                    }
                    add(point!!, depth)
                }
            })
        }

        fun isNotEmpty() = deck.isNotEmpty()
        fun remove() = deck.remove()
    }

    val queue = Queue()

    //
    // Algorithm implementation.
    //

    // Initial step -> add the startNode to the queue.
    queue.add(startNode, /* depth= */0)

    // Traverse the graph
    while (queue.isNotEmpty()) {
        // Remove the item at the head of the queue.
        val currentNode = queue.remove()
        val currentDepth = depthMap[currentNode]!!

        if (currentDepth <= maxDepth) {
            if (visitedMap.isNotVisited(currentNode)) {
                // Mark the current node visited and add to traversal list.
                visitedMap.markVisitedAndAddToTraversalList(currentNode)
                // Add nodes in the adjacency map.
                queue.addAdjacentNodes(currentNode, /* depth= */currentDepth + 1)
            }
        }

    }

    return visitedMap.traversalList.toString()
}