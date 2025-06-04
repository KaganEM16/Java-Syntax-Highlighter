package javaSyntaxHighlighter;

public class Token {
    public enum Type {
        KEYWORD, IDENTIFIER, SYMBOL, NUMBER, STRING, EOF
    }

    public Type type;
    public String value;

    public Token(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return type + ": " + value;
    }
}

