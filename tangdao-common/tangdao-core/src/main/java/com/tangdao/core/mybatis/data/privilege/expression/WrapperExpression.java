package com.tangdao.core.mybatis.data.privilege.expression;
//package com.tangdao.core.data.privilege.expression;
//
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.ExpressionVisitor;
//import net.sf.jsqlparser.expression.StringValue;
//import net.sf.jsqlparser.parser.ASTNodeAccessImpl;
//
///**
// * WrapperExpression
// * 
// * @ClassName: 
// * @author: Naughty Guo
// * @date: 2016年7月18日
// */
//public class WrapperExpression extends StringValue{
//	
//	private String wrapper;
//	private Expression expression;
//	
//	public WrapperExpression(String wrapper, Expression expression) {
//		super("''");
//		this.wrapper = wrapper;
//		this.expression = expression;
//	}
//
//	public String getWrapper() {
//		return wrapper;
//	}
//
//	@Override
//	public String toString() {
//		return wrapper.replaceAll("[?]", expression.toString());
//	}
//}
