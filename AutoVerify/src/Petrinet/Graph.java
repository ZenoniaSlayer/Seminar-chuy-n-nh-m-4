package Petrinet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.PriorityQueue;
import AST.*;


public class Graph {

  public Vertex initVertex;
  private HashMap<String, Vertex> vertices = new HashMap<String, Vertex>();
  private int count = 0;
  private int numOfSensors = 0;
  public Graph() {
  }

  public void setInit(Vertex v) {
    this.initVertex = v;
  }

  public void add(Vertex v) {
    this.vertices.put(v.toString(), v);
  }

  public int getSize() {
    return this.vertices.size();
  }

  public Vertex find(String s) {
    return (Vertex)this.vertices.get(s);
  }

  public String recursiveSearch(String path, HashSet<String> visited, Vertex current, SearchStmt stmt, CompilerVisitor cv) {
    count++;
    if (((Boolean)stmt.accept(cv, current.getVarList())).booleanValue()) {
      return path;
    }
    for (Edge e: current.getChildren()) {
      if (!visited.contains(e.end.toString())) {
        visited.add(e.end.toString());
        String a = recursiveSearch(path+e.id+" ", visited, e.end, stmt, cv);
        if (a!="") {
          return a;
        }
      }
    }
    return "";
  }

  public String search(SearchStmt stmt, HashMap<String, Const> constList) {
    count = 0;
    String res = recursiveSearch("", new HashSet<String>(), initVertex, stmt, new CompilerVisitor(constList));
    //System.out.println("Number of searching: " + count);
    if (res.isEmpty()) {
      return res;
    } else {
      String s = "Detect congestion after " + count + " steps.\n\nTransition sequence: " + res + "\n";
      
      return s;
    }
  }

  public void countNumOfSensors() {
	    int i = 0;
	    HashMap<String, Var> varList = initVertex.getVarList();
	    while (true) {
	      if (!varList.containsKey("Energy_" + Integer.toString(i))) {
	        break;
	      }
	      i++;
	    }
	    numOfSensors = i;
	  }
  
  public int getSum(Vertex v) {
	    int res = 0;
	    HashMap<String, Var> varList = v.getVarList();
	    for (int i = 1; i <= numOfSensors; i++) {
	      res += Integer.parseInt(varList.get("Buffer_" + Integer.toString(i)).value);
	      res += Integer.parseInt(varList.get("Queue_" + Integer.toString(i)).value);
	    }
	    res += Integer.parseInt(varList.get("NUMBER_OF_PACKAGE").value);
	    res += Integer.parseInt(varList.get("Channel_Buffer_1_2").value);
	    res += Integer.parseInt(varList.get("Channel_Buffer_2_4").value);
	    res += Integer.parseInt(varList.get("Channel_Buffer_3_4").value);
	    res += Integer.parseInt(varList.get("Channel_Buffer_4_5").value);
	    return res;
	  }
  
  public String newSearch(SearchStmt stmt, HashMap<String, Const> constList) {
	    count = 1;
	    countNumOfSensors();
	    String res = recursiveNewSearch("", new HashSet<String>(), initVertex, stmt, new CompilerVisitor(constList));
	    //System.out.println("Number of searching: " + count);
	    if (res.isEmpty()) {
	      return res;
	    } else {
	      String s = "Dectet congestion (by Heuristc) after " + count + " steps.\nTransition sequence: " + res + "\n";
	      return s;
	    }
	  }
  
  public String recursiveNewSearch(String path, HashSet<String> visited, Vertex current, SearchStmt stmt, CompilerVisitor cv) {
	    if (((Boolean)stmt.accept(cv, current.getVarList())).booleanValue()) {
	      count++;
	      return path;
	    }
	    Vertex chosenOne = null;
	    int min = 999999;
	    String id = "";
	    for (Edge e: current.getChildren()) {
	      if (!visited.contains(e.end.toString())) {
	        Vertex temp = e.end;
	        if (((Boolean)stmt.accept(cv, temp.getVarList())).booleanValue()) {
	          return recursiveNewSearch(path+e.id+" ", visited, temp, stmt, cv);
	        }
	        int sum = getSum(temp);
	        if (min > sum) {
	          chosenOne = temp;
	          min = sum;
	          id = e.id;
	        }
	      }
	    }
	    if (chosenOne != null) {
	      visited.add(chosenOne.toString());
	      count++;
	      String a = recursiveNewSearch(path+id+" ", visited, chosenOne, stmt, cv);
	      if (a!="") {
	        return a;
	      } else {
	        return recursiveNewSearch(path, visited, current, stmt, cv);
	      }
	    }
	    return "";
	  }
  
  public Vertex recursiveSearchState(HashSet<String> visited, Vertex current, SearchStmt stmt, CompilerVisitor cv) {
    if (((Boolean)stmt.accept(cv, current.getVarList())).booleanValue()) {
      return current;
    }
    for (Edge e: current.getChildren()) {
      if (!visited.contains(e.end.toString())) {
        visited.add(e.end.toString());
        Vertex a = recursiveSearchState(visited, e.end, stmt, cv);
        if (a!=null) {
          return a;
        }
      }
    }
    return null;
  }

  public Vertex searchState(SearchStmt stmt, HashMap<String, Const> constList) {
    return recursiveSearchState(new HashSet<String>(), initVertex, stmt, new CompilerVisitor(constList));
  }

  public void recursiveHeuristicSearch(Vertex con, HashSet<String> visited, Vertex current, PriorityQueue<Vertex> heap) {
    count++;
    if (current.toString().equals(con.toString())) {
      System.out.println(current);
      return;
    }
    for (Edge e: current.getChildren()) {
      if (!visited.contains(e.end.toString())) {
        visited.add(e.end.toString());
        heap.add(e.end);
      }
    }
    Vertex head = heap.poll();
    recursiveHeuristicSearch(con, visited, head, heap);
    return;
  }

  public void heuristicSearch(Vertex con, HashMap<String, Integer> heuristicTable) {
    count = 0;
    @SuppressWarnings({ "unchecked", "rawtypes" })
	PriorityQueue<Vertex> heap  = new PriorityQueue(100, new VCompare(heuristicTable));
    recursiveHeuristicSearch(con, new HashSet<String>(), initVertex, heap);
    System.out.println("Number of searching: " + count);
  }

public String getEnergy(Vertex con, HashMap<String, Integer> heuristicTable) {
    count = 0;
    @SuppressWarnings({ "rawtypes", "unchecked" })
	PriorityQueue<Vertex> heap  = new PriorityQueue(100, new VCompare(heuristicTable));
    //recursiveHeuristicSearch(con, new HashSet<String>(), initVertex, heap);
    Vertex head = heap.poll();
    return getBindingVar(head.toString());
  }

  private String getBindingVar(String id) {
    String[] array = id.split("_");
    String type = array[0];
    String sub = array[1];
    String num = array[2];
    switch (type) {
      case "src":
        if (sub.equals("in")) {
          return "NUMBER_OF_PACKAGE";
        } else if (sub.equals("int")) {
          return "Buffer_"+num;
        } else {
          return "Queue_"+num;
        }
      case "int": case "sink":
        if (sub.equals("in")) {
          return "Buffer_"+num;
        } else {
          return "Queue_"+num;
        }
      default:
        return "Channel_Buffer_"+num+"_"+array[3];
    }
  }

  class VCompare implements Comparator<Vertex> {
    HashMap<String, Integer> heuristicTable;
    public VCompare(HashMap<String, Integer> table) {
      this.heuristicTable = table;
    }
    @Override
    public int compare(Vertex v1, Vertex v2) {
      int value1 = 10000;
      int value2 = 10000;
      if (heuristicTable.containsKey(v1.toString())) {
        value1 = heuristicTable.get(v1.toString()).intValue();
      }
      if (heuristicTable.containsKey(v2.toString())) {
        value2 = heuristicTable.get(v2.toString()).intValue();
      }
      return value1 - value2;
    }
  }

  public HashMap<String, Integer> generateHeuristicTable(Vertex congestState) {
    HashMap<String, Integer> heuristicTable = new HashMap<>();
    HashSet<String> visited = new HashSet<>();
    LinkedList<Vertex> queue = new LinkedList<>();
    queue.add(congestState);
    visited.add(congestState.toString());
    heuristicTable.put(congestState.getMarking().toString(), new Integer(0));
    while (!queue.isEmpty()) {
      Vertex current = (Vertex)queue.removeFirst();
      String currentKey = current.getMarking().toString();
      for (Edge e: current.getParents()) {
        Vertex v = e.end;
        if (visited.contains(v.toString())) {
          continue;
        }
        visited.add(v.toString());
        queue.add(v);
        String vKey = v.getMarking().toString();
        if (heuristicTable.containsKey(vKey)) {
          int oldValue = heuristicTable.get(vKey).intValue();
          int newValue = heuristicTable.get(currentKey).intValue() + 1;
          if (newValue < oldValue) {
            heuristicTable.put(vKey, new Integer(newValue));
          }
        } else {
          heuristicTable.put(vKey, new Integer(heuristicTable.get(currentKey).intValue()+1));
        }
      }
    }
    return heuristicTable;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("The reachability graph has " + this.getSize() + " vertices.\n");
    for (Vertex v: vertices.values()) {
      sb.append(v.toString() + "\n\n\n");
      for (Edge e: v.getChildren()) {
        sb.append("\t-> " + e.toString() +"\n\n\n");
      }
    }
    sb.append("The reachability graph has " + this.getSize() + " vertices.\n");
    return sb.toString();
  }

  public void println() {
    System.out.print("The reachability graph has " + this.getSize() + " vertices.\n");
    for (Vertex v: vertices.values()) {
      System.out.print(v.toString() + "\n\n\n");
      for (Edge e: v.getChildren()) {
        System.out.print("\t-> " + e.toString() +"\n\n\n");
      }
    }
    System.out.print("The reachability graph has " + this.getSize() + " vertices.\n");
  }
}
