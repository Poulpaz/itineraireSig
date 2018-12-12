package com.chastagnier.reffay.appsig.utils

import java.util.*

class Graphe(private val ident: Int, val info: Int) {
    private val adjacents: MutableList<*>
    var couleur: Int = 0
    var distance: Int = 0
    var pere: Graphe? = null
        private set

    init {
        adjacents = LinkedList()
        pere = null
        couleur = 0 //blanc
        distance = -1
    }

    fun getAdjacents(): List<*> {
        return adjacents
    }

    fun addAdjacent(g: Graphe) {
        adjacents.add(g)
    }

    private fun reset() {
        couleur = 0 // blanc;
        pere = null
        distance = -1
    }

    fun visiterProfondeur() {
        print(this)
        couleur = 1 // gris
        val it = adjacents.iterator()
        while (it.hasNext()) {
            val g = it.next() as Graphe
            if (g.couleur == 0) {
                g.pere = this
                g.visiterProfondeur()
            }
        }
        couleur = 2 // noir
    }

    fun existeChemin(t: Graphe): Boolean {
        var trouve = false
        couleur = 1 // gris
        if (equals(t))
            trouve = true
        val it = adjacents.iterator()
        while (it.hasNext()) {
            val g = it.next() as Graphe
            if (g.couleur == 0) {
                g.pere = this
                trouve = trouve || g.existeChemin(t)
            }
        }
        couleur = 2 // noir
        return trouve
    }

    fun visiterLargeur() {
        couleur = 1 // gris
        distance = 0 // sommet de départ
        val file = LinkedList()
        file.add(this)
        while (!file.isEmpty()) {
            val u = file.removeAt(0) as Graphe
            print(u)
            val it = u.getAdjacents().iterator()
            while (it.hasNext()) {
                val g = it.next() as Graphe
                if (g.couleur == 0) {
                    g.pere = u
                    g.couleur = 2
                    g.distance = u.distance + 1
                    file.add(g)
                }
            }
            u.couleur = 2 // noir
        }
        couleur = 2
    }

    fun plusCourtChemin(t: Graphe): Int {
        couleur = 1 // gris
        distance = 0 // sommet de départ
        val file = LinkedList()
        file.add(this)
        while (!file.isEmpty()) {
            val u = file.removeAt(0) as Graphe
            if (u == t)
                return u.distance
            val it = u.getAdjacents().iterator()
            while (it.hasNext()) {
                val g = it.next() as Graphe
                if (g.couleur == 0) {
                    g.pere = u
                    g.couleur = 2
                    g.distance = u.distance + 1
                    file.add(g)
                }
            }
            u.couleur = 2 // noir
        }
        couleur = 2
        return -1
    }

    fun afficherAncetres() {
        val chemin = LinkedList()
        var aux: Graphe? = this
        while (aux!!.pere != null) {
            chemin.add(0, aux) // en debut de liste
            aux = aux.pere
        }
        chemin.add(0, aux)
        println(chemin)
    }

    override fun toString(): String {
        return "[$ident, $info]"
    }

    companion object {

        /** Les méthodes statiques manipulent plusieurs sommets du graphe
         * - le graphe est une collection de sommets
         * - chaque sommet est vu comme le graphe égal à
         * la composante connexe issue de ce sommet.
         */

        /** Pour remettre en situation initiale chaque sommet  */
        private fun reset(c: Collection<*>) {
            val it = c.iterator()
            while (it.hasNext()) {
                val g = it.next() as Graphe
                g.reset()
            }
        }

        /** Pour créer un arete non orientée entre deux sommets  */
        fun arete(g1: Graphe, g2: Graphe) {
            g1.addAdjacent(g2)
            g2.addAdjacent(g1)
        }

        /** Affichage des sommets dans l'ordre du parcours en profondeur  */
        fun parcoursProfondeur(c: Collection<*>) {
            reset(c)
            val it = c.iterator()
            while (it.hasNext()) {
                val g = it.next() as Graphe
                if (g.couleur == 0)
                    g.visiterProfondeur()
            }
            println()
        }

        /** S'il existe, affichage d'un chemin
         * - Méthode basée sur le parcours en profondeur
         */
        fun existeChemin(c: Collection<*>, s: Graphe, t: Graphe): Boolean {
            reset(c)
            val b = s.existeChemin(t)
            if (b)
                t.afficherAncetres()
            else
                println("pas de chemin")
            return b
        }

        /** Affichage des sommets dans l'ordre du parcours en largeur  */
        fun parcoursLargeur(c: Collection<*>) {
            reset(c)
            val it = c.iterator()
            while (it.hasNext()) {
                val g = it.next() as Graphe
                if (g.couleur == 0)
                    g.visiterLargeur()
            }
            println()
        }

        /** S'il existe, affichage d'un chemin
         * - Méthode basée sur le parcours en largeur
         */
        fun plusCourtChemin(c: Collection<*>, s: Graphe, t: Graphe): Int {
            reset(c)
            val d = s.plusCourtChemin(t) // parcours largeur depuis s
            if (d > 0)
                t.afficherAncetres()
            else
                println("pas de chemin")
            return t.distance
        }
    }

}