import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GraphSearchEngineImpl implements GraphSearchEngine{
	
	public GraphSearchEngineImpl() {}

	@SuppressWarnings("null")
	@Override
	/*
	 * (non-Javadoc)
	 * @see GraphSearchEngine#findShortestPath(Node, Node)
	 */
	public List<Node> findShortestPath(Node s, Node t) {
		ArrayList<Node> visitedNodes = new ArrayList<Node>();
		Queue<Node> nodesToVisit=new LinkedList<Node>();
		HashMap<Node,Node> visitedPath=new HashMap<Node, Node>();
		
		nodesToVisit.add(s);
		visitedPath.put(s, null);
		System.out.print("Exploring Graph...");
		while(nodesToVisit.size()>0) {
			Node n = nodesToVisit.remove();
			visitedNodes.add(n);
			for(Node node:n.getNeighbors()) {
				if(!nodesToVisit.contains(node) && !visitedNodes.contains(node)) {
					nodesToVisit.add(node);
					visitedPath.put(node, n);
					if(node.equals(t)){
						visitedNodes.add(node); nodesToVisit = new LinkedList<Node>(); break;
					}
				}
			}
			
		}
		if(!visitedNodes.contains(t)) {return null;}
		System.out.print("Finding Path...");
		Node crntNode = t; 
		List<Node> returnList = new ArrayList<Node>();
		returnList.add(crntNode);
		while (!crntNode.equals(s)) {
			crntNode = visitedPath.get(crntNode);
			returnList.add(crntNode);
		}
		System.out.println("Polishing Output");
		Collections.reverse(returnList);
		return returnList;
	
	}
}