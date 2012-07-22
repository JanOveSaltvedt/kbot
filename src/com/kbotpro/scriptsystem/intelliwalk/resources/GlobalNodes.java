/*	
	Copyright 2012 Jan Ove Saltvedt
	
	This file is part of KBot.

    KBot is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    KBot is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with KBot.  If not, see <http://www.gnu.org/licenses/>.
	
*/

/*
 * Copyright © 2010 Jan Ove Saltvedt.
 * All rights reserved.
 */

package com.kbotpro.scriptsystem.intelliwalk.resources;

import com.kbotpro.scriptsystem.intelliwalk.data.GlobalNode;
import com.kbotpro.scriptsystem.wrappers.Tile;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.PriorityQueue;

/**
 * Created by IntelliJ IDEA.
 * User: PwnZ
 * Date: May 27, 2010
 * Time: 12:30:35 AM
 */
public class GlobalNodes {
    ArrayList<GlobalNode> globalNodeWeb = new ArrayList<GlobalNode>();

    /**
     *
     */
    public GlobalNodes() {
    }

    /**
     * Constructs default GlobalNode array
     * @author PwnZ
     */
    public void constructGlobalNodeArray(){
        ArrayList<GlobalNode> nodes = new ArrayList<GlobalNode>();

        /* Lumbridge */
        connect(nodes, new GlobalNode(3221, 3219), new GlobalNode(3236, 3233));
        connect(nodes, new GlobalNode(3236, 3233), new GlobalNode(3219, 3247));

        globalNodeWeb = nodes;
    }

    /**
     * Connects two GlobalNodes (used for formatting)
     * @author PwnZ
     * @param a the GlobalNode list
     * @param g a GlobalNode
     * @param h a GlobalNode
     * @return connected
     */
    public boolean connect(ArrayList<GlobalNode> a, GlobalNode g, GlobalNode h){

        GlobalNode mutable = new GlobalNode(-1, -1);
        if(a.contains(g)){
            mutable = a.get(a.indexOf(g));
            a.remove(g);
            mutable.addNodeToConnected(h);
            a.add(mutable);
        }else{
            g.addNodeToConnected(h);
            a.add(g);
        }

        if(a.contains(h)){
            mutable = a.get(a.indexOf(h));
            a.remove(h);
            mutable.addNodeToConnected(g);
            a.add(mutable);
        }else{
            h.addNodeToConnected(g);
            a.add(h);
        }

        boolean connected = (a.contains(g) && a.contains(h) && ArrayUtils.contains(a.get(a.indexOf(g)).getConnected(), h));
        return connected;
    }

    public GlobalNode[] getGlobalNodeArray(){
        return globalNodeWeb.toArray(new GlobalNode[globalNodeWeb.size()]);    
    }

    public GlobalNode closestGlobalNode(GlobalNode g){
        int closest = 9999;
        GlobalNode r = null;

        Iterator<GlobalNode> web = globalNodeWeb.iterator();
        while(web.hasNext()){
            GlobalNode h = web.next();
            int dist = g.getTile().distanceTo(h.getTile());
            if(closest > dist){
                closest = dist;
                r = h;
            }
        }

        return r;
    }

    /**
     * Attaches a new GlobalNode to the web.
     * @param g a GlobalNode
     * @param h a GlobalNode
     * @return attached
     */
    public boolean attach(GlobalNode g, GlobalNode h){
        return connect(this.globalNodeWeb, g, h);
    }

    /**
     *
     * @param g
     * @return
     */
     public boolean attachToClosest(GlobalNode g){
        GlobalNode h = closestGlobalNode(g);
        if(h == null)
            return false;
        
        return attach(g, h);
    }

//    public GlobalNode[] calculateNodePath(final Tile startTile, final Tile destination){
//        ArrayList<GlobalNode> nodes = new ArrayList<GlobalNode>();
//        GlobalNode cur = new GlobalNode(startTile);
//        System.out.println("Closest current Position: "+cur.getTile().toString());
//        GlobalNode dest = new GlobalNode(destination);
//        System.out.println("Created GlobalNode at destination: "+dest.getTile().toString());
//        final Tile destTile = dest.getTile();
//
//        nodes.add(cur);
//        GlobalNode next = cur;
//        if(!attachToClosest(cur) || !attachToClosest(dest))
//            throw new RuntimeException("Invalid GlobalNode[], Could not attach destination to web");
//        else{
//            System.out.println("cur connected to "+globalNodeWeb.get(globalNodeWeb.indexOf(cur)).getConnected()[0].getTile().toString());
//            System.out.println("dest connected to "+globalNodeWeb.get(globalNodeWeb.indexOf(dest)).getConnected()[0].getTile().toString());
//        }
//
//
//        while(!nodes.contains(dest)){
//            cur = next;
//            final Tile curTile = cur.getTile();
//            int closest = 999999999;
//            next = cur;
//            System.out.println(cur.getTile().toString());
//            for (GlobalNode g : cur.getConnected()){
//                if(nodes.contains(g))
//                    continue;
//
//                if(g.getTile().equals(dest)){
//                    next = g;
//                    break;
//                }
//                final Tile gTile = g.getTile();
//                final int cost = (Math.abs(gTile.getX() - curTile.getX()) + Math.abs(gTile.getY() - curTile.getY())) + (Math.abs(curTile.getX() - destTile.getX()) + Math.abs(curTile.getY() - destTile.getY()));
//
//                System.out.println("--> "+g.getTile().toString()+" | COST: "+cost);
//                if(cost < closest){
//                    closest = cost;
//                    next = g;
//                }
//            }
//            if(!nodes.contains(next))
//                nodes.add(next);
//            else{
//                System.out.println("ERROR: Tried to add a node that is already in system." + next.getTile().toString());
//                break;
//            }
//
//            //System.out.println("Added new node "+next.getTile().toString());
//        }
//
//
//        return nodes.toArray(new GlobalNode[nodes.size()]);
//    }


    /**
     * Dijkstra algorithm, used for calculating
     * @param start
     * @param dest
     */
    public GlobalNode[] Dijkstra(GlobalNode start, GlobalNode dest){
        ComparableGlobalNode cStart = new ComparableGlobalNode(start);
        cStart.setCost(0);

        LinkedHashSet<ComparableGlobalNode> closed = new LinkedHashSet<ComparableGlobalNode>();
        closed.add(cStart);

        ComparableGlobalNode cCurrent = cStart;
        while(!(cCurrent.getTile().equals(dest.getTile()))){
            
        }

        return null;
    }

    /**
     * This may not be needed, used for priority queue
     */
    protected class ComparableGlobalNode extends GlobalNode implements Comparable {
        protected int cost = Integer.MAX_VALUE;

        public ComparableGlobalNode(GlobalNode g) {
            super(g.getTile());
        }

        public void setCost(final int c){
            this.cost = c;
        }

        public int getCost(){
            return this.cost;
        }

        public int compareTo(Object o) {
            if(!(o instanceof ComparableGlobalNode))
                return this.cost;
            final ComparableGlobalNode gn = (ComparableGlobalNode) o;
            return (this.cost - gn.getCost());
        }
    }
}
