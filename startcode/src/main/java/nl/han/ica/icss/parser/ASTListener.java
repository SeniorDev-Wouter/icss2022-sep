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
		// remove the stylesheet from the stack and add it to the AST
		Stylesheet sheet = (Stylesheet) currentContainer.pop();
		ast.setRoot(sheet);
	}


	@Override
	public void enterCssdefinition(ICSSParser.CssdefinitionContext ctx) {
		Stylerule stylerule = new Stylerule();
		currentContainer.push(stylerule);
	}

	@Override
	public void exitCssdefinition(ICSSParser.CssdefinitionContext ctx) {
		Stylerule rule = (Stylerule) currentContainer.pop();
		currentContainer.peek().addChild(rule);
	}

	@Override
	public void enterVariabledefinition(ICSSParser.VariabledefinitionContext ctx) {
		VariableReference variable = new VariableReference(ctx.getText());
		currentContainer.push(variable);
	}

	@Override
	public void exitVariabledefinition(ICSSParser.VariabledefinitionContext ctx) {
		VariableReference variable = (VariableReference) currentContainer.pop();
		currentContainer.peek().addChild(variable);
	}

	@Override
	public void enterCssclass(ICSSParser.CssclassContext ctx) {
		Declaration declaration = new Declaration();
		currentContainer.push(declaration);
	}

	@Override
	public void exitCssclass(ICSSParser.CssclassContext ctx) {
		Declaration declaration = (Declaration) currentContainer.pop();
		currentContainer.peek().addChild(declaration);
	}

	@Override
	public void enterIdent(ICSSParser.IdentContext ctx) {
		PropertyName propertyName = new PropertyName(ctx.getText());
		currentContainer.push(propertyName);
	}

	@Override
	public void exitIdent(ICSSParser.IdentContext ctx) {
		PropertyName propertyName = (PropertyName) currentContainer.pop();
		currentContainer.peek().addChild(propertyName);
	}

	@Override
	public void enterEntry(ICSSParser.EntryContext ctx) {
		Declaration propertyName = new Declaration();
		currentContainer.push(propertyName);
	}

	@Override
	public void exitEntry(ICSSParser.EntryContext ctx) {
		Declaration propertyName = (Declaration) currentContainer.pop();
		currentContainer.peek().addChild(propertyName);
	}
}