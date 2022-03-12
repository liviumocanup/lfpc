package implementation;

import java.util.HashMap;
import java.util.Map;

import static implementation.TokenType.*;

public class Token {
    TokenType type;
    String literal;
    static Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("true", TRUE);
        keywords.put("false", FALSE);
        keywords.put("if", IF);
        keywords.put("else", ELSE);
        keywords.put("function", FUNCTION);
        keywords.put("return", RETURN);
        keywords.put("for", FOR);
        keywords.put("while", WHILE);
        keywords.put("int", INTEGER);
        keywords.put("float", FLOAT);
        keywords.put("string", STRING);
        keywords.put("boolean", BOOLEAN);
        keywords.put("void", VOID);
        keywords.put("print", PRINT);
        keywords.put("break", BREAK);
    }

    Token(TokenType type, String literal) {
        this.type = type;
        this.literal = literal;
    }

    public String toString() {
        return "["+type + " " + literal+"]";
    }
}

enum TokenType {
    IDENTIFIER,
    BOOLEAN, TRUE, FALSE,
    INTEGER, INT_LITERAL,
    FLOAT, FLOAT_LITERAL,
    STRING, STRING_LITERAL,
    L_PAREN, R_PAREN,
    L_BRACE, R_BRACE,

    COMMA, SEMICOLON,
    SLASH, ASTERISK,

    PLUS, MINUS,
    INCREMENT, DECREMENT,

    NOT, NOT_EQUAL,
    ASSIGN, EQUAL,
    GREAT, GREAT_EQUAL,
    LESS, LESS_EQUAL,

    AND, OR,
    IF, ELSE,
    FUNCTION, RETURN,
    BREAK,
    FOR, WHILE,
    PRINT, VOID,

    INVALID,
    EOF
}