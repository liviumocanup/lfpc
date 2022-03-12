package implementation;

import java.util.ArrayList;

import static implementation.TokenType.*;

public class Lexer {
    public String inputText;
    public ArrayList<Token> tokens = new ArrayList<>();
    public int currentPosition = 0;
    public int peekPosition = 1;

    public Lexer(String inputText) {
        this.inputText = inputText;
    }

    public ArrayList<Token> getTokens() {
        while (currentPosition < inputText.length()) {
            checkCharacter();
        }
        tokens.add(new Token(EOF, ""));
        return tokens;
    }

    public void checkCharacter() {
        Token t = null;
        char ch = inputText.charAt(currentPosition);

        switch (ch) {
            case '+':
                if(peek()=='+') {
                    t = new Token(INCREMENT, "++");
                    movePosition();
                }
                else t = new Token(PLUS, "+");
                break;
            case '-':
                if(peek()=='-') {
                    t = new Token(DECREMENT, "--");
                    movePosition();
                }
                else t = new Token(MINUS, "-");
                break;
            case '=':
                if(peek()=='=') {
                    t = new Token(EQUAL, "==");
                    movePosition();
                }
                else t = new Token(ASSIGN, "=");
                break;
            case '!':
                if(peek()=='=') {
                    t = new Token(NOT_EQUAL, "!=");
                    movePosition();
                }
                else t = new Token(NOT, "!");
                break;
            case '>':
                if(peek()=='=') {
                    t = new Token(GREAT_EQUAL, ">=");
                    movePosition();
                }
                else t = new Token(GREAT, ">");
                break;
            case '<':
                if(peek()=='=') {
                    t = new Token(LESS_EQUAL, "<=");
                    movePosition();
                }
                else t = new Token(LESS, "<");
                break;
            case '&':
                if(peek()=='&') {
                    t = new Token(AND, "&&");
                    movePosition();
                }
                else t = new Token(INVALID, "&");
                break;
            case '|':
                if(peek()=='|') {
                    t = new Token(OR, "||");
                    movePosition();
                }
                else t = new Token(INVALID, "|");
                break;
            case '(':
                t = new Token(L_PAREN, "(");
                break;
            case ')':
                t = new Token(R_PAREN, ")");
                break;
            case '{':
                t = new Token(L_BRACE, "{");
                break;
            case '}':
                t = new Token(R_BRACE, "}");
                break;
            case ',':
                t = new Token(COMMA, ",");
                break;
            case '*':
                t = new Token(ASTERISK, "*");
                break;
            case '/':
                t = new Token(SLASH, "/");
                break;
            case ';':
                t = new Token(SEMICOLON, ";");
                break;
            case '"':
                readString();
                break;

            case ' ': case '\r': case '\t': case '\n':
                break;

            default:
                if (isLetter(ch)) {
                    readWord();
                }else if (isDigit(ch)) {
                    number();
                } else {
                    t = new Token(INVALID, Character.toString(ch));
                }
                break;
        }
        movePosition();
        if(t!=null) tokens.add(t);
    }

    public void movePosition() {
        currentPosition = peekPosition;
        if(peekPosition >= inputText.length())
            return;
        peekPosition++;
    }

    public char peek() {
        if (peekPosition >= inputText.length()) return '\0';
        return inputText.charAt(peekPosition);
    }

    public void readString() {
        int start = currentPosition;
        while (peek() != '"' && currentPosition < inputText.length()) {
            movePosition();
        }

        if (currentPosition >= inputText.length()) {
            System.out.println("String not ended.");
            return;
        }

        movePosition();
        String value = inputText.substring(start + 1, currentPosition);
        tokens.add(new Token(STRING_LITERAL, value));
    }

    public void number() {
        int start = currentPosition;
        while (isDigit(peek()))
            movePosition();

        if (peek() == '.') {
            movePosition();
            while (isDigit(peek()))
                movePosition();
            tokens.add(new Token(FLOAT_LITERAL, inputText.substring(start, currentPosition+1)));
        }
        else
        {
            tokens.add(new Token(INT_LITERAL, inputText.substring(start, currentPosition+1)));
        }
    }

    public void readWord() {
        int start = currentPosition;
        while (isLetter(peek()) || isDigit(peek()))
            movePosition();

        String text = inputText.substring(start, currentPosition+1);
        if (Token.keywords.get(text) != null) tokens.add(new Token(Token.keywords.get(text), text));
        else tokens.add(new Token(IDENTIFIER, text));
    }
    public boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    public boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
}