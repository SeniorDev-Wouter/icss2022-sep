package nl.han.ica.icss.generator;

import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.selectors.*;

import java.util.stream.Collectors;

public class Generator {

    public String generate(AST ast) {
        return generateStylesheet((Stylesheet) ast.root);
    }

    private String generateStylesheet(Stylesheet stylesheet) {
        return stylesheet.getChildren().stream()
                .filter(node -> node instanceof Stylerule)
                .map(node -> generateStylerule((Stylerule) node))
                .collect(Collectors.joining("\n"));
    }

    private String generateStylerule(Stylerule rule) {
        StringBuilder styleruleString = new StringBuilder();
        for (ASTNode node : rule.getChildren()) {
            if (node instanceof Selector) {
                styleruleString.append(generateSelector((Selector) node)).append(" {\n");
            } else if (node instanceof Declaration) {
                styleruleString.append("\t").append(generateDeclaration((Declaration) node)).append("\n");
            }
        }
        styleruleString.append("}\n");
        return styleruleString.toString();
    }

    private String generateSelector(Selector selector) {
        if (selector instanceof TagSelector)
            return ((TagSelector) selector).tag;
        if (selector instanceof ClassSelector)
            return ((ClassSelector) selector).cls;
        if (selector instanceof IdSelector)
            return ((IdSelector) selector).id;
        return "";
    }

    private String generateDeclaration(Declaration declaration) {
        return declaration.property.name + ": " + generateExpression(declaration.expression) + ";";
    }

    private String generateExpression(Expression expression) {
        if (expression instanceof PixelLiteral)
            return ((PixelLiteral) expression).value + "px";
        if (expression instanceof PercentageLiteral)
            return ((PercentageLiteral) expression).value + "%";
        if (expression instanceof ColorLiteral)
            return ((ColorLiteral) expression).value;
        return "";
    }
}
