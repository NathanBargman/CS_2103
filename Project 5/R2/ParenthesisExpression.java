import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.Collection;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class ParenthesisExpression extends SimpleExpression implements CompoundExpression{
	
	Collection<Expression> _subExpressions;

	public ParenthesisExpression() {
		_subExpressions = new ArrayList<Expression>();
		_value = "()";
		_FXHBox = new HBox();
	}
	
	@Override
	public CompoundExpression getParent() {
		return _parent;
	}

	@Override
	public void setParent(CompoundExpression parent) {
		_parent = parent;
		
	}

	@Override
	public Expression deepCopy() {
		Expression rnt = new ParenthesisExpression();
		for(Expression exp : _subExpressions) {
			Expression temp = exp.deepCopy();
			temp.setParent((CompoundExpression) rnt);	
			((CompoundExpression) rnt).addSubexpression(temp);
		}
		return rnt;
	}

	@Override
	public void flatten() {
		//Compress children first
		for(Expression exp : _subExpressions) {
			exp.flatten();
		}
		
		//Remove like nodes, add their children
		Collection<Expression> toAdd = new ArrayList<Expression>();
		Collection<Expression> toRemove = new ArrayList<Expression>();
		for (Expression exp : _subExpressions) {
			if(exp instanceof ParenthesisExpression) {
				toRemove.add(exp);
				for (Expression exp_child : ((ParenthesisExpression) exp)._subExpressions) {
					exp_child.setParent(this);
					toAdd.add(exp_child);
				}
			}
		}
		for (Expression exp : toAdd) {
			addSubexpression(exp);
		}
		for (Expression exp : toRemove) {
			_subExpressions.remove(exp);
		}
	}

	@Override
	public void convertToString(StringBuilder stringBuilder, int indentLevel) {
		if(_parent == null) {indentLevel = 0;}
		Expression.indent(stringBuilder, indentLevel);
		stringBuilder.append(_value);
		stringBuilder.append("\n");
		for(Expression exp : _subExpressions) {
			exp.convertToString(stringBuilder, indentLevel + 1);
		}
	}

	@Override
	public void addSubexpression(Expression subexpression) {
		// TODO Auto-generated method stub
		if(subexpression != null) {
			_subExpressions.add(subexpression);
		}
	}
	
	@Override 
	public Node getNode() {
		//Make we don't overide our HBox
		if(_FXHBox.getChildren().isEmpty()) {
			_FXHBox = new HBox();
			ArrayList<Node> nodesToAdd = new ArrayList<Node>();
			Label temp = new Label("(");
			temp.setFont(font);
			nodesToAdd.add(temp);
			for(Expression exp : _subExpressions) {
				nodesToAdd.add(exp.getNode());
			}
			Label temp2 = new Label(")");
			temp2.setFont(font);
			nodesToAdd.add(temp2);
			_FXHBox.getChildren().addAll(nodesToAdd);
		}
		return _FXHBox;
	}
	
	@Override
	public Expression getOwnerOfBox(HBox box) {
		for(Expression exp : _subExpressions) {
			if(exp.getNode().equals(box)) {
				return exp;
			}
		}
		return null;
	}
	
	@Override
	/**
	 * Should not be used with a ParenthesisExpression
	 */
	public Node reorgChildren(Expression childToMove, int loc) {
		return null;
	}
	
	@Override
	/**
	 * Should not be used with a ParenthesisExpression
	 */
	public void resetHBox() {}
}