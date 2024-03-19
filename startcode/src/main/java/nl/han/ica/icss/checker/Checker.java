package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Pipeline;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.types.ExpressionType;

import javax.swing.text.Style;
import java.util.HashMap;
import java.util.LinkedList;


public class Checker {

    private LinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        // variableTypes = new HANLinkedList<>();
        checkStylesheet(ast.root);
    }

    private void checkStylesheet(Stylesheet sheet) {
        checkStylerule((Stylerule) sheet.getChildren().get(0));
    }

    private void checkStylerule(Stylerule rule) {
        for(ASTNode child: rule.getChildren()){
            if(child instanceof Declaration){
                checkDeclaration((Declaration) child);
            }
        }
    }

    private void checkDeclaration(Declaration declaration) {
        if(declaration.property.name.equals("width")){
            if (!(declaration.expression instanceof PixelLiteral)){
                declaration.setError("you are stupid");
            }
        }
    }


}
