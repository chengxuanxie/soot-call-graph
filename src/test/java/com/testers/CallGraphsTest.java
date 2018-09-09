package com.testers;

import soot.*;
import soot.jimple.toolkits.callgraph.CHATransformer;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.jimple.toolkits.callgraph.Targets;
import soot.util.dot.DotGraph;
import soot.util.queue.QueueReader;

import java.io.File;
import java.util.*;

public class CallGraphsTest {

    public static void serializeCallGraph(CallGraph graph, String fileName) {
        if (fileName == null) {
            fileName = soot.SourceLocator.v().getOutputDir();
            if (fileName.length() > 0) {
                fileName = fileName + File.separator;
            }
            fileName = fileName + "call-graph" + DotGraph.DOT_EXTENSION;
        }
        System.out.println("writing to file " + fileName);
        DotGraph canvas = new DotGraph("call-graph");
        QueueReader<Edge> listener = graph.listener();
        while (listener.hasNext()) {
            Edge next = listener.next();
            MethodOrMethodContext src = next.getSrc();
            MethodOrMethodContext tgt = next.getTgt();
            String srcString = src.toString();
            String tgtString = tgt.toString();

            if ((!srcString.startsWith("<java.") && !srcString.startsWith("<sun.") && !srcString.startsWith("<org.")
                    && !srcString.startsWith("<com.sun") && !srcString.startsWith("<jdk.")
                    && !srcString.startsWith("<javax."))
                    || (!tgtString.startsWith("<java.") && !tgtString.startsWith("<sun.")
                    && !tgtString.startsWith("<org.") && !tgtString.startsWith("<com.sun")
                    && !tgtString.startsWith("<jdk.") && !tgtString.startsWith("<javax."))) {

                canvas.drawNode(src.toString());
                canvas.drawNode(tgt.toString());
                canvas.drawEdge(src.toString(), tgt.toString());
                System.out.println("src = " + srcString);
                System.out.println("tgt = " + tgtString);
            }
        }
        canvas.plot(fileName);
        return;
    }

    public static void main(String[] args) {
        // From
        // https://github.com/pcpratts/soot-rb/blob/master/tutorial/guide/examples/call_graph/src/dk/brics/soot/callgraphs/CallGraphExample.java
        List<String> argsList = new ArrayList<String>(Arrays.asList(args));
        argsList.addAll(Arrays.asList(new String[]{"-w", "-whole-program","-cp","target/classes","-pp", "com.testers.CallGraphs", // main-class
                "com.testers.CallGraphs", // argument classes
                "com.testers.A" //
        }));
        String[] args2 = new String[argsList.size()];
        args2 = argsList.toArray(args2);
        PackManager.v().getPack("wjtp").add(new Transform("wjtp.myTrans", new SceneTransformer() {
            @Override
            protected void internalTransform(String phaseName, Map options) {
                CHATransformer.v().transform();
                SootClass a = Scene.v().getSootClass("com.testers.A");
                SootMethod src = Scene.v().getMainClass().getMethodByName("doStuff");
                CallGraph cg = Scene.v().getCallGraph();

                serializeCallGraph(cg, "output" + DotGraph.DOT_EXTENSION);
                System.out.println("serializeCallGraph completed.");

                Iterator<MethodOrMethodContext> targets = new Targets(cg.edgesOutOf(src));
                while (targets.hasNext()) {
                    SootMethod tgt = (SootMethod) targets.next();
                    System.out.println(src + " may call " + tgt);
                }
            }
        }));
        args = argsList.toArray(new String[0]);
        soot.Main.main(args2);
    }
}
