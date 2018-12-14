package com.chastagnier.reffay.appsig.activity;

import android.util.Log;

import com.chastagnier.reffay.appsig.model.GEO_ARC;
import com.chastagnier.reffay.appsig.model.GEO_POINT;
import com.chastagnier.reffay.appsig.model.Graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class PEL {

    private LinkedList<Integer> adj[]; //liste d'adjacence
    private List<GEO_POINT> points;
    private List<GEO_ARC> arcs;

    public PEL(Graph graphe) {
        this.points = new ArrayList<GEO_POINT>(graphe.getListPoint());
        this.arcs = new ArrayList<GEO_ARC>(graphe.getListArc());
        adj = new LinkedList[300];
        for (int i = 0; i < points.size(); ++i){
            adj[points.get(i).getId()] = new LinkedList();
        }
        for (int i = 0; i < arcs.size(); ++i){
           // adj[arcs.get(i).getDeb()].add(arcs.get(i).getFin());
            Log.d("mlk", String.valueOf(arcs.get(i).getDeb()));
            Log.d("mlk", String.valueOf(arcs.get(i).getFin()));

            adj[arcs.get(i).getDeb()].push(arcs.get(i).getFin());


        }
    }

    public List<Integer> execute (GEO_POINT s)
    {
        List<Integer> liste_parcouru = new ArrayList<>();
        boolean visited[] = new boolean[points.size()];
        LinkedList<Integer> queue = new LinkedList<Integer>();
        visited[s.getId()] = true;
        queue.add(s.getId());
        while (queue.size() != 0) {
            int poll = queue.poll();
            liste_parcouru.add(poll);
            Iterator<Integer> i = adj[poll].listIterator();
            while (i.hasNext()) {
                int n = i.next();
                if (!visited[n]) {
                    visited[n] = true;
                    queue.add(n);
                }
            }
        }
        return liste_parcouru;
    }

}
