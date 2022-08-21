import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public static final char EOF = (char) -1;
    private final String input;
    private int pos = 0;
    private char c;

    public Lexer(String input) {
        this.input = input;
        this.c = input.charAt(pos);
    }

    public void consume() {
        pos++;
        if (pos >= input.length()) {
            c = EOF;
        } else {
            c = input.charAt(pos);
        }
    }

    private Token BOOL() {
        StringBuilder bool = new StringBuilder();
        do {
            if (bool.length() > 5) break;
            if (bool.length() == 4 && bool.toString().equalsIgnoreCase("true")) {
                return new Token(Token.TokenType.BOOL, true);
            }
            if (bool.length() == 5 && bool.toString().equalsIgnoreCase("false")) {
                return new Token(Token.TokenType.BOOL, false);
            }
            bool.append(c);
            consume();
        } while (c != EOF);
        throw new RuntimeException("token BOOL error, pos at: " + pos);
    }

    private Token NULL() {
        StringBuilder bool = new StringBuilder();
        do {
            if (bool.length() > 4) break;
            if (bool.length() == 4 && bool.toString().equalsIgnoreCase("null")) {
                return new Token(Token.TokenType.NULL, null);
            }
            bool.append(c);
            consume();
        } while (c != EOF);
        throw new RuntimeException("token NULL error, pos at: " + pos);
    }

    private Token NUMBER() {
        int sign = 1;
        if (c == '-') {
            sign = -1;
            consume();
        }
        if (!Character.isDigit(c)) {
            throw new RuntimeException("token NUMBER error, pos at: " + pos);
        }
        StringBuilder number = new StringBuilder();
        do {
            number.append(c);
            consume();
        } while (Character.isDigit(c) || c == '.');
        String n = number.toString();
        try { // 可能有多个 .
            if (n.contains(".")) {
                return new Token(Token.TokenType.NUMBER, sign * Double.parseDouble(n));
            } else {
                return new Token(Token.TokenType.NUMBER, sign * Long.parseLong(n));
            }
        } catch (Exception e) {
            throw new RuntimeException("token NUMBER error: " + n);
        }
    }

    private Token STRING() {
        consume(); // 消费掉第一个 "
        StringBuilder str = new StringBuilder();
        do {
            if (c == EOF) {
                throw new RuntimeException("token STRING error: " + str);
            }
            if (c == '"') {
                consume();
                break;
            }
            if (c == '\\') {
                str.append(c);
                consume();
            }
            str.append(c);
            consume();
        } while (true);
        return new Token(Token.TokenType.STRING, str.toString());
    }

    public Token nextToken() {
        while (c != EOF) {
            switch (c) {
                case '{':
                    consume();
                    return new Token(Token.TokenType.L_CURLY_BRACKET, null);
                case '}':
                    consume();
                    return new Token(Token.TokenType.R_CURLY_BRACKET, null);
                case '[':
                    consume();
                    return new Token(Token.TokenType.L_SQUARE_BRACKET, null);
                case ']':
                    consume();
                    return new Token(Token.TokenType.R_SQUARE_BRACKET, null);
                case ',':
                    consume();
                    return new Token(Token.TokenType.COMMA, null);
                case ':':
                    consume();
                    return new Token(Token.TokenType.COLON, null);
                case 'n':
                    return NULL();
                case 'f':
                case 't':
                    return BOOL();
                case '"':
                    return STRING();
                default: {
                    if (c == '-' || Character.isDigit(c)) return NUMBER();
                    if (c == ' ' || c == '\t' || c == '\n' || c == '\r') {
                        consume();
                        continue;
                    }
                    throw new RuntimeException("invalid character: " + c);
                }
            }
        }
        return new Token(Token.TokenType.EOF, null);
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        Token t = nextToken();
        while (t.tokenType != Token.TokenType.EOF) {
            tokens.add(t);
            t = nextToken();
        }
        return tokens;
    }
}
