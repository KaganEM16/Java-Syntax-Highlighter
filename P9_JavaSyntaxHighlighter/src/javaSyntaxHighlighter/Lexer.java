package javaSyntaxHighlighter;

import java.util.*;

public class Lexer {
    private String input;
    private int pos;
    private static final Set<String> keywords = Set.of(
        "class", "public", "private", "protected", "static", "void", "int", "if", "else", "return"
    );

    public Lexer(String input) {
        this.input = input;
        this.pos = 0;
    }

    private char peek() {
        return pos < input.length() ? input.charAt(pos) : '\0';
    }

    private char next() {
        return pos < input.length() ? input.charAt(pos++) : '\0';
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        while (pos < input.length()) {
            char current = peek();

            if (Character.isWhitespace(current)) {
                next();
            } else if (Character.isLetter(current) || current == '_') {
                StringBuilder sb = new StringBuilder();
                while (Character.isLetterOrDigit(peek()) || peek() == '_') {
                    sb.append(next());
                }
                String word = sb.toString();
                if (keywords.contains(word)) {
                    tokens.add(new Token(Token.Type.KEYWORD, word));
                } else {
                    tokens.add(new Token(Token.Type.IDENTIFIER, word));
                }
            } else if (Character.isDigit(current)) {
                StringBuilder sb = new StringBuilder();
                while (Character.isDigit(peek())) {
                    sb.append(next());
                }
                tokens.add(new Token(Token.Type.NUMBER, sb.toString()));
            } else if (current == '"') {
                next(); // opening "
                StringBuilder sb = new StringBuilder();
                while (peek() != '"' && peek() != '\0') {
                    sb.append(next());
                }
                next(); // closing "
                tokens.add(new Token(Token.Type.STRING, sb.toString()));
            } else {
                // Single-character symbols
                tokens.add(new Token(Token.Type.SYMBOL, Character.toString(next())));
            }
        }

        tokens.add(new Token(Token.Type.EOF, ""));
        return tokens;
    }
}

