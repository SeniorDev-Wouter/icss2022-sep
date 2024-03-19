package nl.han.ica.icss.parser;

import nl.han.ica.datastructures.HANStack;
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
	private IHANStack<ASTNode> currentContainer;

	public ASTListener() {
		ast = new AST();
		//currentContainer = new HANStack<>();
		currentContainer = new HANStack<>();
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
	public void enterBoolfalse(ICSSParser.BoolfalseContext ctx) {
		Literal boolFalse = new BoolLiteral(ctx.getText());
		currentContainer.push(boolFalse);
	}
	@Override
	public void exitBoolfalse(ICSSParser.BoolfalseContext ctx) {
		Literal boolFalse = (BoolLiteral) currentContainer.pop();
		currentContainer.peek().addChild(boolFalse);
	}

	@Override
	public void enterBooltrue(ICSSParser.BooltrueContext ctx) {
		Literal boolTrue = new BoolLiteral(ctx.getText());
		currentContainer.push(boolTrue);
	}
	@Override
	public void exitBooltrue(ICSSParser.BooltrueContext ctx) {
		Literal boolTrue = (BoolLiteral) currentContainer.pop();
		currentContainer.peek().addChild(boolTrue);
	}

	@Override
	public void enterColor(ICSSParser.ColorContext ctx) {
		Literal color = new ColorLiteral(ctx.getText());
		currentContainer.push(color);	}

	@Override
	public void exitColor(ICSSParser.ColorContext ctx) {
		Literal color = (ColorLiteral) currentContainer.pop();
		currentContainer.peek().addChild(color);
	}

	@Override
	public void enterPercentage(ICSSParser.PercentageContext ctx) {
		Literal percentage = new PercentageLiteral(ctx.getText());
		currentContainer.push(percentage);	}

	@Override
	public void exitPercentage(ICSSParser.PercentageContext ctx) {
		Literal percentage = (PercentageLiteral) currentContainer.pop();
		currentContainer.peek().addChild(percentage);	}

	@Override
	public void enterPixelsize(ICSSParser.PixelsizeContext ctx) {
		Literal pixel = new PixelLiteral(ctx.getText());
		currentContainer.push(pixel);
	}

	@Override
	public void exitPixelsize(ICSSParser.PixelsizeContext ctx) {
		Literal pixel = (PixelLiteral) currentContainer.pop();
		currentContainer.peek().addChild(pixel);
	}

	@Override
	public void enterScalar(ICSSParser.ScalarContext ctx) {
		Literal scalar = new ScalarLiteral(ctx.getText());
		currentContainer.push(scalar);
	}

	@Override
	public void exitScalar(ICSSParser.ScalarContext ctx) {
		Literal scalar = (ScalarLiteral) currentContainer.pop();
		currentContainer.peek().addChild(scalar);
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