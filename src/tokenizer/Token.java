package tokenizer;

/**
 * @ClassName Token
 * @Description //TODO
 * @Author Han ChengYi
 * @Date 2020/10/17 17:03
 * @Version 1.0
 **/

public class Token {
    private TokenType type;
    private String value;

    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", value='" + value + '\'' +
                '}';
    }
}
