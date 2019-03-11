import java.util.Collection;

public class MovieNode implements Node {
	
	private String name;
	private Collection<ActorNode> neighbors;
	//constructor
	public MovieNode(String _name, Collection<ActorNode> _neighbors) {
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
	 * adds respective node to the neighbors list
	 */
	public void addToNeighbors(ActorNode toAdd) {
		neighbors.add(toAdd);
	}
}
