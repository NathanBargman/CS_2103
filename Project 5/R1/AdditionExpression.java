import java.util.ArrayList;
import java.util.Collection;

public class AdditionExpression extends SimpleExpression implements CompoundExpression{
	
	Collection<Expression> _subExpressions;
	
	public AdditionExpression() {
		_subExpressions = new ArrayList<Expression>();
		_value = "+";
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
		return null;
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
			if(exp instanceof AdditionExpression) {
				toRemove.add(exp);
				for (Expression exp_child : ((AdditionExpression) exp)._subExpressions) {
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
}