import java.util.ArrayList;
import java.util.Collection;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class MultiplicationExpression extends SimpleExpression implements CompoundExpression{
	
	private Collection<Expression> _subExpressions;

	public MultiplicationExpression() {
		_subExpressions = new ArrayList<Expression>();
		_value = "*";
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
		Expression rnt = new MultiplicationExpression();
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
			if(exp instanceof MultiplicationExpression) {
				toRemove.add(exp);
				for (Expression exp_child : ((MultiplicationExpression) exp)._subExpressions) {
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
		if(subexpression != null) {
			_subExpressions.add(subexpression);
		}
		
	}
	
	@Override 
	public Node getNode() {
		//Make we don't overide our HBox
		if(_FXHBox.getChildren().isEmpty()) {
			_FXHBox = new HBox();
			boolean skipFirst = false;
			ArrayList<Node> nodesToAdd = new ArrayList<Node>();
			for(Expression exp : _subExpressions) {
				if(skipFirst) {
					Label temp = new Label(_value);
					temp.setFont(font);
					nodesToAdd.add(temp);
				}
				skipFirst = true;
				nodesToAdd.add(exp.getNode());
			}
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
	public Node reorgChildren(Expression childToMove, int loc) {
		ArrayList<Expression> rtnList = new ArrayList<Expression>();
		ArrayList<Expression> tempList = ((ArrayList<Expression>)_subExpressions);
		tempList.remove(childToMove);
		rtnList.addAll(tempList.subList(0, loc));
		rtnList.add(childToMove);
		rtnList.addAll(tempList.subList(loc, tempList.size()));
		_subExpressions = rtnList;
		_FXHBox = new HBox();
		return getNode();
	}
	
	@Override
	public void resetHBox() {
		_FXHBox = new HBox();
		getNode();
	}
}