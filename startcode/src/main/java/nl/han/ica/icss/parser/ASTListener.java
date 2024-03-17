package nl.han.ica.icss.parser;

import java.util.Stack;


import nl.han.ica.datastructures.IHANStack;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.selectors.ClassSelector;
import nl.han.ica.icss.ast.selectors.IdSelector;
import nl.han.ica.icss.ast.selectors.TagSelector;

/**
 * This class extracts the ICSS Abstract Syntax Tree from the Antlr Parse tree.
 */
public class ASTListener extends ICSSBaseListener {
	
	//Accumulator attributes:
	private AST ast;

	//Use this to keep track of the parent nodes when recursively traversing the ast
//	private IHANStack<ASTNode> currentContainer;
	private Stack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		//currentContainer = new HANStack<>();
		currentContainer = new Stack<>();
	}
    public AST getAST() {
        return ast;
    }


	@Override
	public void enterStylesheet(ICSSParser.StylesheetContext ctx) {
		Stylesheet sheet = new Stylesheet();
		currentContainer.push(sheet);
	}

	@Override
	public void exitStylesheet(ICSSParser.StylesheetContext ctx) {
		Stylesheet sheet = (Stylesheet) currentContainer.pop();
		ast.setRoot(sheet);
	}

	@Override
	public void enterDeclaration(ICSSParser.DeclarationContext ctx) {
		Declaration declaration = new Declaration();
		currentContainer.push(declaration);
	}

	@Override
	public void exitDeclaration(ICSSParser.DeclarationContext ctx) {
		Declaration declaration = (Declaration) currentContainer.pop();
		currentContainer.peek().addChild(declaration);
	}

	@Override
	public void enterElseclause(ICSSParser.ElseclauseContext ctx) {
		ElseClause elseclause = new ElseClause();
		currentContainer.push(elseclause);
	}

	@Override
	public void exitElseclause(ICSSParser.ElseclauseContext ctx) {
		ElseClause elseclause = (ElseClause) currentContainer.pop();
		currentContainer.peek().addChild(elseclause);
	}

	@Override
	public void enterIfclause(ICSSParser.IfclauseContext ctx) {
		IfClause ifClause = new IfClause();
		currentContainer.push(ifClause);
	}

	@Override
	public void exitIfclause(ICSSParser.IfclauseContext ctx) {
		IfClause ifClause = (IfClause) currentContainer.pop();
		currentContainer.peek().addChild(ifClause);
	}

	@Override
	public void enterLiteral(ICSSParser.LiteralContext ctx) {
		Literal literal;
		 if (ctx.TRUE() != null | ctx.FALSE() !=null) {
			literal = new BoolLiteral(ctx.getText());
		} else if (ctx.PIXELSIZE() != null) {
			literal = new PixelLiteral(ctx.getText());
		} else if (ctx.PERCENTAGE() != null) {
			literal = new PercentageLiteral(ctx.getText());
		} else if (ctx.SCALAR() != null) {
			literal = new ScalarLiteral(ctx.getText());
		} else if (ctx.COLOR() != null) {
			literal = new ColorLiteral(ctx.getText());
		} else {
			throw new IllegalArgumentException("Unknown literal type");
		}
		currentContainer.push(literal);
	}


	@Override
	public void exitLiteral(ICSSParser.LiteralContext ctx) {
		Literal literal = (Literal) currentContainer.pop();
		currentContainer.peek().addChild(literal);
	}

	@Override
	public void enterOperation(ICSSParser.OperationContext ctx) {
		Operation operation;
		if(ctx.addOperation() != null){
			operation = new AddOperation();
		}else if(ctx.subtractOperation() != null){
			operation = new SubtractOperation();
		}else if(ctx.multiplyOperation() != null){
			operation = new MultiplyOperation();
		}else {
			throw new IllegalArgumentException("Unknown operation type");
		}
		currentContainer.push(operation);
	}

	@Override
	public void exitOperation(ICSSParser.OperationContext ctx) {
		Operation operation = (Operation) currentContainer.pop();
		currentContainer.peek().addChild(operation);
	}

	@Override
	public void enterPropertyName(ICSSParser.PropertyNameContext ctx) {
		PropertyName propertyName = new PropertyName(ctx.getText());
		currentContainer.push(propertyName);
	}

	@Override
	public void exitPropertyName(ICSSParser.PropertyNameContext ctx) {
		PropertyName propertyName = (PropertyName) currentContainer.pop();
		currentContainer.peek().addChild(propertyName);
	}

	@Override
	public void enterSelector(ICSSParser.SelectorContext ctx) {
		Selector selector;
		if(ctx.idSelector() != null){
		selector = new IdSelector(ctx.getText());
		}else if(ctx.tagSelector() != null){
			selector = new TagSelector(ctx.getText());
		}else if(ctx.classSelector() != null){
			selector = new ClassSelector(ctx.getText());
		}
		else {
			throw new IllegalArgumentException("Unknown selector type");
		}
		currentContainer.push(selector);
	}

	@Override
	public void exitSelector(ICSSParser.SelectorContext ctx) {
		Selector selector = (Selector) currentContainer.pop();
		currentContainer.peek().addChild(selector);
	}

	@Override
	public void enterStylerule(ICSSParser.StyleruleContext ctx) {
		Stylerule stylerule = new Stylerule();
		currentContainer.push(stylerule);
	}

	@Override
	public void exitStylerule(ICSSParser.StyleruleContext ctx) {
		Stylerule stylerule = (Stylerule) currentContainer.pop();
		currentContainer.peek().addChild(stylerule);
	}

	@Override
	public void enterVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		VariableAssignment variableAssignment = new VariableAssignment();
		currentContainer.push(variableAssignment);
	}

	@Override
	public void exitVariableAssignment(ICSSParser.VariableAssignmentContext ctx) {
		VariableAssignment variableAssignment = (VariableAssignment) currentContainer.pop();
		currentContainer.peek().addChild(variableAssignment);
	}

	@Override
	public void enterVariableReference(ICSSParser.VariableReferenceContext ctx) {
		VariableReference variableReference = new VariableReference(ctx.getText());
		currentContainer.push(variableReference);
	}

	@Override
	public void exitVariableReference(ICSSParser.VariableReferenceContext ctx) {
		VariableReference variableReference = (VariableReference) currentContainer.pop();
		currentContainer.peek().addChild(variableReference);
	}

	@Override
	public void enterClassSelector(ICSSParser.ClassSelectorContext ctx) {
		ClassSelector classSelector = new ClassSelector(ctx.getText());
		currentContainer.push(classSelector);
	}

	@Override
	public void exitClassSelector(ICSSParser.ClassSelectorContext ctx) {
		ClassSelector classSelector = (ClassSelector) currentContainer.pop();
		currentContainer.peek().addChild(classSelector);
	}

	@Override
	public void enterIdSelector(ICSSParser.IdSelectorContext ctx) {
		IdSelector idSelector = new IdSelector(ctx.getText());
		currentContainer.push(idSelector);
	}

	@Override
	public void exitIdSelector(ICSSParser.IdSelectorContext ctx) {
		IdSelector idSelector = (IdSelector) currentContainer.pop();
		currentContainer.peek().addChild(idSelector);
	}

	@Override
	public void enterTagSelector(ICSSParser.TagSelectorContext ctx) {
		TagSelector tagSelector = new TagSelector(ctx.getText());
		currentContainer.push(tagSelector);
	}

	@Override
	public void exitTagSelector(ICSSParser.TagSelectorContext ctx) {
		TagSelector tagSelector = (TagSelector) currentContainer.pop();
		currentContainer.peek().addChild(tagSelector);
	}

	@Override
	public void enterAddOperation(ICSSParser.AddOperationContext ctx) {
		AddOperation addOperation = new AddOperation();
		currentContainer.push(addOperation);
	}

	@Override
	public void exitAddOperation(ICSSParser.AddOperationContext ctx) {
		AddOperation addOperation = (AddOperation) currentContainer.pop();
		currentContainer.peek().addChild(addOperation);
	}

	@Override
	public void enterMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
		MultiplyOperation multiplyOperation = new MultiplyOperation();
		currentContainer.push(multiplyOperation);
	}

	@Override
	public void exitMultiplyOperation(ICSSParser.MultiplyOperationContext ctx) {
		MultiplyOperation multiplyOperation = (MultiplyOperation) currentContainer.pop();
		currentContainer.peek().addChild(multiplyOperation);
	}

	@Override
	public void enterSubtractOperation(ICSSParser.SubtractOperationContext ctx) {
		SubtractOperation subtractOperation = new SubtractOperation();
		currentContainer.push(subtractOperation);
	}

	@Override
	public void exitSubtractOperation(ICSSParser.SubtractOperationContext ctx) {
		SubtractOperation subtractOperation = (SubtractOperation) currentContainer.pop();
		currentContainer.peek().addChild(subtractOperation);
	}

}