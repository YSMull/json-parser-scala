public class Token {
    public TokenType tokenType;
    public Object value;
    
    enum TokenType {
        L_CURLY_BRACKET,
        R_CURLY_BRACKET,
        L_SQUARE_BRACKET,
        R_SQUARE_BRACKET,
        COMMA,
        COLON,
        NULL,
        BOOL,
        STRING,
        NUMBER,
        EOF
    }

    public Token(TokenType tokenType, Object value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType=" + tokenType +
                ", value=" + value +
                '}';
    }
}