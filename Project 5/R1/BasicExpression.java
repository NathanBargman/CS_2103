public class BasicExpression extends SimpleExpression implements Expression {
	
	public BasicExpression(String value, CompoundExpression parent) {
		_value = value;
		_parent = parent;
	}
	
	@Override
	public CompoundExpression getParent() {
		// TODO Auto-generated method stub
		return _parent;
	}

	@Override
	public void setParent(CompoundExpression parent) {
		// TODO Auto-generated method stub
		_parent = parent;
	}

	@Override
	public Expression deepCopy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flatten() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void convertToString(StringBuilder stringBuilder, int indentLevel) {
		Expression.indent(stringBuilder, indentLevel);
		stringBuilder.append(_value);
		if(_parent != null) {stringBuilder.append("\n");}
	}
}