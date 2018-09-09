package com.utils;

import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Targets;
import soot.util.dot.DotGraph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DotUtils {
    //a collection store the visited point in a graph when iterate the graph
    public static Map<String, Boolean> visitedNodes = new HashMap<String, Boolean>();
    //an DotGraph object
    public static DotGraph dotGraph = new DotGraph("defult-dotgraph");

    public DotUtils(){

    }

    /**
     * output dot graph as a dot file
     * @param fileName
     * @param storeDir
     * @param dotGraph
     */
    public static void exportDotGraph(String fileName, String storeDir, DotGraph dotGraph){
        String outPath = storeDir + "/" + fileName + ".dot";
        dotGraph.plot(outPath);
    }

    /**
     * represent call graph in the form of a dot graph
     * @param callGraph: call graph of an APK
     * @param curMethod: entry point of an APK (DummyMain)
     */
    public static void CGToDotWithMethodsSignature(CallGraph callGraph, SootMethod curMethod){
        //if this is the first to call this function, method is start point
        String sigOfCurNode = curMethod.getSignature();
        //iterate the call graph starting from the startPoint, after visited a node, put it into the visitedNodes collection
        visitedNodes.put(sigOfCurNode, true);
        //draw the current node into call graph
        dotGraph.drawNode(sigOfCurNode);

        //after draw a node into datGraph, we can set some attribute for the node

        //get the callers of current node, that is to say, find out methods who have edge point into current method
        Iterator<MethodOrMethodContext> callersOfMethod = new Targets(callGraph.edgesInto(curMethod));
        if(callersOfMethod != null){
            while(callersOfMethod.hasNext()){
                SootMethod predecessor = (SootMethod) callersOfMethod.next();
                if(predecessor == null){
                    System.out.println("This method is not existed!");
                }
                //if this predecessor of current node has not been visited, visit it!
                if(!visitedNodes.containsKey(predecessor.getSignature())){
                    CGToDotWithMethodsSignature(callGraph, predecessor);
                }
            }
        }

        //get the callees of current node, that is to say, get the methods called by curMethod
        Iterator<MethodOrMethodContext> calleesOfMethod = new Targets(callGraph.edgesOutOf(curMethod));
        if(calleesOfMethod != null){
            while(calleesOfMethod.hasNext()){
                SootMethod successor = (SootMethod) calleesOfMethod.next();
                if(successor == null){
                    System.out.println("This method is not existed!");
                }
                //draw this callee into DotGraph
                dotGraph.drawNode(successor.getSignature());
                //add an edge from currentNode to this successor
                dotGraph.drawEdge(sigOfCurNode, successor.getSignature());
                //if this successor of currentNode has not been visited, visit it!
                if(!visitedNodes.containsKey(successor.getSignature())){
                    CGToDotWithMethodsSignature(callGraph, successor);
                }
            }
        }
    }

    public static void CGToDotWithMethodsName(CallGraph callGraph, SootMethod curMethod){
        //if this is the first to call this function, method is start point
        String nameOfCurNode = curMethod.getName();
        //iterate the call graph starting from the startPoint, after visited a node, put it into the visitedNodes collection
        visitedNodes.put(nameOfCurNode, true);
        //draw the current node into call graph
        dotGraph.drawNode(nameOfCurNode);

        //after draw a node into datGraph, we can set some attribute for the node

        //get the callers of current node, that is to say, find out methods who have edge point into current method
        Iterator<MethodOrMethodContext> callersOfMethod = new Targets(callGraph.edgesInto(curMethod));
        if(callersOfMethod != null){
            while(callersOfMethod.hasNext()){
                SootMethod predecessor = (SootMethod) callersOfMethod.next();
                if(predecessor == null){
                    System.out.println("This method is not existed!");
                }
                //if this predecessor of current node has not been visited, visit it!
                if(!visitedNodes.containsKey(predecessor.getName())){
                    CGToDotWithMethodsName(callGraph, predecessor);
                }
            }
        }

        //get the callees of current node, that is to say, get the methods called by curMethod
        Iterator<MethodOrMethodContext> calleesOfMethod = new Targets(callGraph.edgesOutOf(curMethod));
        if(calleesOfMethod != null){
            while(calleesOfMethod.hasNext()){
                SootMethod successor = (SootMethod) calleesOfMethod.next();
                if(successor == null){
                    System.out.println("This method is not existed!");
                }
                //draw this callee into DotGraph
                dotGraph.drawNode(successor.getName());
                //add an edge from currentNode to this successor
                dotGraph.drawEdge(nameOfCurNode, successor.getName());
                //if this successor of currentNode has not been visited, visit it!
                if(!visitedNodes.containsKey(successor.getName())){
                    CGToDotWithMethodsName(callGraph, successor);
                }
            }
        }
    }

}
