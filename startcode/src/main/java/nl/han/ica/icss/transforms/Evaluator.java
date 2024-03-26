package nl.han.ica.icss.transforms;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Evaluator implements Transform {

    private LinkedList<HashMap<String, Literal>> variableValues;

    public Evaluator() {
        variableValues = new LinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        variableValues = new LinkedList<>();
        variableValues.addFirst(new HashMap<>());
        applyStylesheet(ast.root);
    }

    private void nextScope(ASTNode node) {
        increaseScope();
        checkNode(node);
        decreaseScope();
    }

    private void increaseScope() {
        variableValues.addFirst(new HashMap<>());
    }

    private void decreaseScope() {
        variableValues.removeFirst();
    }

    private void checkNode(ASTNode node) {
        for (ASTNode child : node.getChildren()) {
            if (child instanceof Declaration) {
                applyDeclaration((Declaration) child);
            } else if (child instanceof IfClause) {
                applyIfStatement((IfClause) child, node);
                node.removeChild(child);
            } else if (child instanceof VariableAssignment)
                applyVariableAssignment((VariableAssignment) child);
        }
    }

    private void applyIfStatement(IfClause ifClause, ASTNode node) {
        nextScope(ifClause);
        if (ifClause.getChildren().get(0) instanceof VariableReference) {
            BoolLiteral boolLiteral = (BoolLiteral) getVariableValues((VariableReference) ifClause.getChildren().get(0));
            if (boolLiteral.value) {
                handleIfClause(ifClause, node);
            } else {
                handleElseClause(ifClause, node);
            }
        } else {
            if (ifClause.getChildren().get(0) instanceof BoolLiteral && ((BoolLiteral) ifClause.getChildren().get(0)).value) {
                handleIfClause(ifClause, node);
            } else {
                handleElseClause(ifClause, node);
            }
        }
    }

    private void handleIfClause(ASTNode clause, ASTNode node) {
        for (ASTNode child : clause.getChildren()) {
            if (!(child instanceof ElseClause)) {
                if (!(child instanceof IfClause)) {
                    node.addChild(child);
                }
            }
        }
    }

    private void handleElseClause(IfClause ifClause, ASTNode node) {
        for (ASTNode child : ifClause.getChildren()) {
            if (child instanceof ElseClause) {
                nextScope(child);
                for (ASTNode grandChild : child.getChildren()) {
                    if (!(grandChild instanceof IfClause)) {
                        node.addChild(grandChild);
                    }
                }
            }
        }
    }


    private void applyStylesheet(Stylesheet stylesheet) {
        for (ASTNode node : stylesheet.getChildren()) {
            if (node instanceof Stylerule) {
                applyStylerule((Stylerule) node);
            } else if (node instanceof VariableAssignment) {
                applyVariableAssignment((VariableAssignment) node);
            }
        }
    }

    private void applyStylerule(Stylerule stylerule) {
        nextScope(stylerule);
    }

    private void applyVariableAssignment(VariableAssignment variableAssignment) {
        variableAssignment.expression = evaluateExpression(variableAssignment.expression);
        variableValues.get(0).put(variableAssignment.name.name, (Literal) evaluateExpression(variableAssignment.expression));
    }

    private void applyDeclaration(Declaration declaration) {
        declaration.expression = evaluateExpression(declaration.expression);
    }

    private Expression evaluateExpression(Expression expression) {
        if (expression instanceof Literal) {
            return (Literal) expression;
        } else if (expression instanceof AddOperation) {
            return evaluateAddOperation((AddOperation) expression);
        } else if (expression instanceof SubtractOperation) {
            return evaluateSubtractOperation((SubtractOperation) expression);
        } else if (expression instanceof MultiplyOperation) {
            return evaluateMultiplyOperation((MultiplyOperation) expression);
        } else {
            return expression;
        }
    }

    private Expression evaluateMultiplyOperation(MultiplyOperation expression) {
        if (expression.lhs instanceof VariableReference) {
            expression.lhs = getVariableValues((VariableReference) expression.lhs);
        }
        if (expression.rhs instanceof VariableReference) {
            expression.rhs = getVariableValues((VariableReference) expression.rhs);
        }
        if (expression.lhs instanceof PixelLiteral) {
            PixelLiteral left = (PixelLiteral) evaluateExpression(expression.lhs);
            ScalarLiteral right = (ScalarLiteral) evaluateExpression(expression.rhs);
            return new PixelLiteral(left.value * right.value);
        } else if (expression.lhs instanceof PercentageLiteral) {
            PercentageLiteral left = (PercentageLiteral) evaluateExpression(expression.lhs);
            ScalarLiteral right = (ScalarLiteral) evaluateExpression(expression.rhs);
            return new PercentageLiteral(left.value * right.value);
        } else if (expression.lhs instanceof ScalarLiteral) {
            ScalarLiteral left = (ScalarLiteral) evaluateExpression(expression.lhs);
            ScalarLiteral right = (ScalarLiteral) evaluateExpression(expression.rhs);
            return new ScalarLiteral(left.value * right.value);
        } else if (expression.rhs instanceof PixelLiteral) {
            PixelLiteral right = (PixelLiteral) evaluateExpression(expression.rhs);
            ScalarLiteral left = (ScalarLiteral) evaluateExpression(expression.lhs);
            return new PixelLiteral(left.value * right.value);
        } else if (expression.rhs instanceof PercentageLiteral) {
            PercentageLiteral right = (PercentageLiteral) evaluateExpression(expression.rhs);
            ScalarLiteral left = (ScalarLiteral) evaluateExpression(expression.lhs);
            return new PercentageLiteral(left.value * right.value);
        } else if (expression.rhs instanceof ScalarLiteral) {
            ScalarLiteral right = (ScalarLiteral) evaluateExpression(expression.rhs);
            ScalarLiteral left = (ScalarLiteral) evaluateExpression(expression.lhs);
            return new ScalarLiteral(left.value * right.value);
        } else {
            return expression;
        }
    }

    private Expression evaluateSubtractOperation(SubtractOperation expression) {
        if (expression.lhs instanceof PixelLiteral) {
            PixelLiteral left = (PixelLiteral) evaluateExpression(expression.lhs);
            PixelLiteral right = (PixelLiteral) evaluateExpression(expression.rhs);
            return new PixelLiteral(left.value - right.value);
        } else if (expression.lhs instanceof PercentageLiteral) {
            PercentageLiteral left = (PercentageLiteral) evaluateExpression(expression.lhs);
            PercentageLiteral right = (PercentageLiteral) evaluateExpression(expression.rhs);
            return new PercentageLiteral(left.value - right.value);
        } else if (expression.lhs instanceof ScalarLiteral) {
            ScalarLiteral left = (ScalarLiteral) evaluateExpression(expression.lhs);
            ScalarLiteral right = (ScalarLiteral) evaluateExpression(expression.rhs);
            return new ScalarLiteral(left.value - right.value);
        } else {
            return expression;
        }
    }

    private Expression evaluateAddOperation(AddOperation expression) {
        if (expression.lhs instanceof PixelLiteral) {
            PixelLiteral left = (PixelLiteral) evaluateExpression(expression.lhs);
            PixelLiteral right = (PixelLiteral) evaluateExpression(expression.rhs);
            return new PixelLiteral(left.value + right.value);
        } else if (expression.lhs instanceof PercentageLiteral) {
            PercentageLiteral left = (PercentageLiteral) evaluateExpression(expression.lhs);
            PercentageLiteral right = (PercentageLiteral) evaluateExpression(expression.rhs);
            return new PercentageLiteral(left.value + right.value);
        } else if (expression.lhs instanceof ScalarLiteral) {
            ScalarLiteral left = (ScalarLiteral) evaluateExpression(expression.lhs);
            ScalarLiteral right = (ScalarLiteral) evaluateExpression(expression.rhs);
            return new ScalarLiteral(left.value + right.value);
        } else {
            return expression;
        }
    }

    private Literal getVariableValues(VariableReference variableReference) {
        Literal variable = null;
        for (HashMap<String, Literal> variableType : variableValues) {
            variable = variableType.get(variableReference.name);
            if (variable != null) {
                return variable;
            }
        }
        variableReference.setError("Variable doesnt exist");
        return variable;
    }
}
