package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.IHANLinkedList;
import nl.han.ica.icss.Pipeline;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

import javax.swing.text.Style;
import java.util.HashMap;
import java.util.LinkedList;


public class Checker {

    private LinkedList<HashMap<String, ExpressionType>> variableTypes;

    public void check(AST ast) {
        variableTypes = new LinkedList<>();
        variableTypes.addFirst(new HashMap<>());
        checkStylesheet(ast.root);
    }

    private void checkStylesheet(Stylesheet sheet) {
        for (int i = 0; i < sheet.getChildren().size(); i++) {
            var child = sheet.getChildren().get(i);
            if (child instanceof Stylerule) {
                checkStylerule((Stylerule) child);
            } else if (child instanceof VariableAssignment)
                checkVariableAssignment((VariableAssignment) child);
            {
            }
        }
    }

    private void checkVariableAssignment(VariableAssignment variable) {
        if (!(variable.getChildren().get(0) instanceof VariableReference) || !(variable.getChildren().get(1) instanceof Literal)) {
            variable.setError("you are stupid idiot");
        } else if (variable.expression instanceof PercentageLiteral) {
            variableTypes.get(0).put(variable.name.name, ExpressionType.PERCENTAGE);
        } else if (variable.expression instanceof PixelLiteral) {
            variableTypes.get(0).put(variable.name.name, ExpressionType.PIXEL);
        } else if (variable.expression instanceof ColorLiteral) {
            variableTypes.get(0).put(variable.name.name, ExpressionType.COLOR);
        } else if (variable.expression instanceof BoolLiteral) {
            variableTypes.get(0).put(variable.name.name, ExpressionType.BOOL);
        } else if (variable.expression instanceof ScalarLiteral) {
            variableTypes.get(0).put(variable.name.name, ExpressionType.SCALAR);
        } else {
            variableTypes.get(0).put(variable.name.name, ExpressionType.UNDEFINED);
        }
        System.out.println(variableTypes);
    }

    private void checkStylerule(Stylerule rule) {
        for (ASTNode child : rule.getChildren()) {
            if (child instanceof Declaration) {
                checkDeclaration((Declaration) child);
            }
        }
    }

    private void checkDeclaration(Declaration declaration) {
        if (declaration.property.name.equals("width") | declaration.property.name.equals("height")) {
            if (!(declaration.expression instanceof PixelLiteral) && !(declaration.expression instanceof PercentageLiteral) && getVariableType(declaration.expression, 0) != ExpressionType.PIXEL && getVariableType(declaration.expression, 0) != ExpressionType.PERCENTAGE) {
                declaration.setError("you are stupid width");
            }
//            if(!checkIfPixelOperationIsValid(declaration.expression)){
//                declaration.setError("cant do math? idiot.");
//            }
        }

        if (declaration.property.name.equals("color") | declaration.property.name.equals("background-color")) {
            if (!(declaration.expression instanceof ColorLiteral) && getVariableType(declaration.expression, 0) != ExpressionType.COLOR) {
                declaration.setError("you are stupid color");
            }
//            if(!checkIfPixelOperationIsValid(declaration.expression)){
//                declaration.setError("cant do math? idiot.");
//            }
        }

    }

    private ExpressionType getVariableType(Expression expression, int scope) {
        if (variableTypes.get(scope).get(((VariableReference) expression).name) == null) {
            expression.setError("Variable doesnt exist");
        }
        return variableTypes.get(scope).get(((VariableReference) expression).name);
    }

//
//    private boolean checkIfPixelOperationIsValid(Expression expression){
//if(expression instanceof AddOperation){
//    if((!(expression.getChildren().get(0) instanceof PixelLiteral) && !(expression.getChildren().get(1) instanceof PixelLiteral))){
//        return false;
//    }
//} else if (expression instanceof MultiplyOperation) {
//
//} else if (expression instanceof SubtractOperation) {
//
//}
//        if(expression.getChildren().get(0) instanceof PixelLiteral){
//            return false;
//        }
//        return true;
//    }
//

}
