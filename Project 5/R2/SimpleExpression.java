import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/**
 * This class gives accses to _parent and _value.
 */
public class SimpleExpression {
	
	protected CompoundExpression _parent;
	protected String _value;
	protected Pane _FXHBox;
	final protected Font font = new Font("Arial", 30);
	
}
