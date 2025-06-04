package javaSyntaxHighlighter;

import java.util.*;

// Top-Down Parser Yaklaşımıyla Kod Değerlendirilir

public class Parser {
    private List<Token> tokens;
    private int pos = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return pos < tokens.size() ? tokens.get(pos) : null;
    }

    private Token next() {
        return pos < tokens.size() ? tokens.get(pos++) : null;
    }

    private void eat(Token.Type expectedType, String expectedValue) {
        Token token = next();
        if (token.type != expectedType || (expectedValue != null && !token.value.equals(expectedValue))) {
            throw new RuntimeException("Beklenen: " + expectedType + " '" + expectedValue + "', bulunan: " + token.type + " '" + token.value + "'");
        }
    }

    public void parse() {
        parseClass();
        System.out.println("Kod geçerli."); // Kod geçerliliği kontrolü
    }

    private void parseClass() {
        eat(Token.Type.KEYWORD, "class");
        eat(Token.Type.IDENTIFIER, null);
        eat(Token.Type.SYMBOL, "{");
        while (!peek().value.equals("}")) {
            parseMethod();
        }
        eat(Token.Type.SYMBOL, "}");
    }

    private void parseMethod() {
        if (peek().value.equals("public")) eat(Token.Type.KEYWORD, "public");
        if (peek().value.equals("static")) eat(Token.Type.KEYWORD, "static");
        eat(Token.Type.KEYWORD, "void");
        eat(Token.Type.IDENTIFIER, "main");
        eat(Token.Type.SYMBOL, "(");
        eat(Token.Type.SYMBOL, ")");
        eat(Token.Type.SYMBOL, "{");
        while (!peek().value.equals("}")) {
            parseStatement();
        }
        eat(Token.Type.SYMBOL, "}");
    }

    private void parseStatement() {
        Token current = peek();

        if (current.type == Token.Type.KEYWORD && current.value.equals("int")) {
            next();
            eat(Token.Type.IDENTIFIER, null);
            eat(Token.Type.SYMBOL, "=");
            eat(Token.Type.NUMBER, null);
            eat(Token.Type.SYMBOL, ";");
        } else if (current.type == Token.Type.KEYWORD && current.value.equals("if")) {
            eat(Token.Type.KEYWORD, "if");
            eat(Token.Type.SYMBOL, "(");
            parseExpression();
            eat(Token.Type.SYMBOL, ")");
            parseStatement();
            if (peek().type == Token.Type.KEYWORD && peek().value.equals("else")) {
                eat(Token.Type.KEYWORD, "else");
                parseStatement();
            }
        } else if (current.type == Token.Type.IDENTIFIER && current.value.equals("System")) {
            eat(Token.Type.IDENTIFIER, "System");
            eat(Token.Type.SYMBOL, ".");
            eat(Token.Type.IDENTIFIER, "out");
            eat(Token.Type.SYMBOL, ".");
            eat(Token.Type.IDENTIFIER, "println");
            eat(Token.Type.SYMBOL, "(");
            if (peek().type == Token.Type.STRING || peek().type == Token.Type.IDENTIFIER) {
                next();
            }
            eat(Token.Type.SYMBOL, ")");
            eat(Token.Type.SYMBOL, ";");
        } else {
            throw new RuntimeException("Beklenmeyen ifade: " + current.value);
        }
    }

    private void parseExpression() {
        if (peek().type == Token.Type.IDENTIFIER || peek().type == Token.Type.NUMBER) {
            next(); // a
            if (peek().type == Token.Type.SYMBOL && List.of(">", "<", "==", "!=", ">=", "<=").contains(peek().value)) {
                next(); // >
                if (peek().type == Token.Type.IDENTIFIER || peek().type == Token.Type.NUMBER) {
                    next(); // b
                }
            }
        } else {
            throw new RuntimeException("Geçersiz ifade");
        }
    }
}
