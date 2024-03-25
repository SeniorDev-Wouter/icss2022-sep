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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


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
            if (declaration.expression instanceof Operation) {
                var operation = resolveOperation(declaration.expression);
                System.out.println(resolveOperation(declaration.expression));
                if (!(operation instanceof PixelLiteral) && !(operation instanceof PercentageLiteral) && !(operation instanceof VariableReference)) {
                    declaration.setError("Can only be a pixel or percentage");
                }
            } else if (!(declaration.expression instanceof PixelLiteral) && !(declaration.expression instanceof PercentageLiteral) && !(declaration.expression instanceof VariableReference)) {
                declaration.setError("Can only be a pixel or percentage");
            } else if (declaration.expression instanceof VariableReference) {
                if (getVariableType((VariableReference) declaration.expression, 0) != ExpressionType.PIXEL && getVariableType((VariableReference) declaration.expression, 0) != ExpressionType.PERCENTAGE) {
                    declaration.setError("Variable must be pixel or percentage");
                }
            }
        }

        if (declaration.property.name.equals("color") | declaration.property.name.equals("background-color")) {
            if (!(declaration.expression instanceof ColorLiteral) && !(declaration.expression instanceof VariableReference)) {
                declaration.setError("Can only be a color");
            } else if (declaration.expression instanceof VariableReference) {
                if (getVariableType((VariableReference) declaration.expression, 0) != ExpressionType.COLOR) {
                    declaration.setError("Can only be a color");
                }
            }
        }

    }

    private ExpressionType getVariableType(VariableReference variableReference, int scope) {
        var variable = variableTypes.get(scope).get(variableReference.name);
        if (variable == null) {
            variableReference.setError("Variable doesnt exist");
        }
        return variable;
    }

    private Literal getVariableLiteralType(VariableReference lhs) {
        var var = getVariableType(lhs, 0);
        if (var == ExpressionType.PIXEL) {
            return new PixelLiteral("10px");
        } else if (var == ExpressionType.PERCENTAGE) {
            return new PercentageLiteral("10%");
        } else if (var == ExpressionType.SCALAR) {
            return new ScalarLiteral(2);
        } else if (var == ExpressionType.COLOR) {
            return new ColorLiteral("color");
        } else if (var == ExpressionType.UNDEFINED) {
            lhs.setError("variable undefined");
        }
        return new BoolLiteral("true");

    }



    private ASTNode resolveOperation(ASTNode operation) {
        var rhs = operation.getChildren().get(1);
        var lhs = operation.getChildren().get(0);
        if (lhs instanceof VariableReference) {
            lhs = getVariableLiteralType((VariableReference) lhs);
        }
        if (rhs instanceof VariableReference) {
            rhs = getVariableLiteralType((VariableReference) rhs);
        }

        if (rhs instanceof Operation) {
            rhs = resolveOperation(operation.getChildren().get(1));
        } else if (lhs instanceof ColorLiteral || rhs instanceof ColorLiteral) {
            operation.setError("cant use colors in operations");
            return lhs;
        } else if (lhs instanceof BoolLiteral || rhs instanceof BoolLiteral) {
            operation.setError("cant use Booleans in operations");
        }

        if (operation instanceof MultiplyOperation) {
            return handleMultiplyOperation(operation, lhs, rhs);
        } else if (operation instanceof AddOperation || operation instanceof SubtractOperation) {
            return handleAddSubtractOperation(operation, lhs, rhs);
        }
        return lhs;
    }

    private ASTNode handleMultiplyOperation(ASTNode operation, ASTNode lhs, ASTNode rhs) {
        if ((lhs instanceof PixelLiteral && rhs instanceof PixelLiteral) || (lhs instanceof PercentageLiteral && rhs instanceof PercentageLiteral)) {
            operation.setError("One operand must be a scalar");
            return lhs;
        } else if (lhs instanceof ScalarLiteral && rhs instanceof ScalarLiteral) {
            return lhs;
        } else if ((lhs instanceof PercentageLiteral || lhs instanceof PixelLiteral) && rhs instanceof ScalarLiteral) {
            return lhs;
        } else if ((rhs instanceof PercentageLiteral || rhs instanceof PixelLiteral) && lhs instanceof ScalarLiteral) {
            return rhs;
        }
        return lhs;
    }

    private ASTNode handleAddSubtractOperation(ASTNode operation, ASTNode lhs, ASTNode rhs) {
        if (lhs.getClass() == rhs.getClass()) {
            return lhs;
        } else {
            operation.setError("must be same type");
            return lhs;
        }
    }



}
