import java.util.Collection;
import java.util.HashSet;

public class ActorNode implements Node{

	private String name;
	private Collection<MovieNode> neighbors;
	//constructor
	public ActorNode(String _name, Collection<MovieNode> _neighbors) {
		name = _name;
		neighbors = _neighbors;
	}
	
	@Override
	/*
	 * (non-Javadoc)
	 * @see Node#getName()
	 */
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see Node#getNeighbors()
	 */
	public Collection<? extends Node> getNeighbors() {
		// TODO Auto-generated method stub
		return neighbors;
	}
	/*
	 * @param Node to add
	 * adds the respective node to the list of neighbors
	 */
	public void addToNeighbors(MovieNode toAdd) {
		neighbors.add(toAdd);
	}
}
