import javafx.scene.Node;
import javafx.scene.layout.HBox;

interface CompoundExpression extends Expression {	
	
	/**
	 * Adds the specified expression as a child.
	 * @param subexpression the child expression to add
	 */
	void addSubexpression (Expression subexpression);
	
	/**
	 * Fetches which Expression is the focus
	 * @param box a HBox
	 * @return the Expression that made the HBox in their respective getNode Method
	 */
	Expression getOwnerOfBox(HBox box);
	
	/**
	 * Changes the list order of subexpressions.
	 * @param childToMove the Expression to re-order in the Compound Expression's subexpressions.
	 * @param loc new location for the Expression.
	 * @return the node that was changed
	 */
	Node reorgChildren(Expression childToMove, int loc);
	
	/**
	 * Sets the HBox to a new HBox and rebuilds whole tree.
	 */
	void resetHBox();
}
