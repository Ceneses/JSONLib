package tokenizer;

import exception.JSONParserException;

/**
 * @ClassName Tokenizer
 * @Description //TODO
 * @Author Han ChengYi
 * @Date 2020/10/17 17:05
 * @Version 1.0
 **/

public class Tokenizer {
    private String jsonString;
    private CharReader charReader;
    private TokenList tokens;

    public Tokenizer(String jsonString) {
        this.jsonString = jsonString;
        this.charReader = new CharReader(jsonString);
        this.tokens = new TokenList();
    }

    public TokenList getTokens() {
        tokenizer();
        return this.tokens;
    }

    public void tokenizer(){
        // 接收对象
        Token token = null;
        // 读取一个Token
        do{
            token = start();
        // 存储一个Token
            tokens.addToken(token);
        }while (token.getType() != TokenType.END_DOCUMENT);
    }

    public Token start(){
        char next;
        if (charReader.hasNext()) {
            do {
                next = charReader.next();
            }while (isWhiteSpace(next));
            switch (next) {
                case '{':
                    return new Token(TokenType.BEGIN_OBJ,"{");
                case '[':
                    return new Token(TokenType.BEGIN_ARRAY,"[");
                case ']':
                    return new Token(TokenType.END_ARRAY,"]");
                case '}':
                    return new Token(TokenType.END_OBJ,"}");
                case ':':
                    return new Token(TokenType.SEP_COLON,":");
                case ',':
                    return new Token(TokenType.SEP_COMMA,",");
                case 't':
                case 'f':
                    return readBoolean();
                case 'n':
                    return readNULL();
                case '"':
                    return readString();
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case '-':
                    return readNumber();
                default:
                    throw new JSONParserException("JsonToken解析失败，包含无效字符！");
            }
        }
        return new Token(TokenType.END_DOCUMENT,"EOF");
    }

    public boolean isWhiteSpace(char next) {
        return (next == ' ' || next == '\t' || next == '\r' || next == '\n');
    }

    public Token readBoolean(){
        if(charReader.peek() == 't'){
            if(!(charReader.next() == 'r' && charReader.next() == 'u' && charReader.next() == 'e')){
                throw new JSONParserException("JsonToken解析true失败，包含无效字符！");
            }
            return new Token(TokenType.BOOLEAN,"true");
        }else{
            if(!(charReader.next() == 'a' && charReader.next() == 'l' && charReader.next() == 's' && charReader.next() == 'e')){
                throw new JSONParserException("JsonToken解析false失败，包含无效字符！");
            }
            return new Token(TokenType.BOOLEAN,"false");
        }
    }

    public Token readNULL(){
        if(!(charReader.next() == 'u' && charReader.next() == 'l' && charReader.next() == 'l')){
            throw new JSONParserException("JsonToken解析NULL失败，包含无效字符！");
        }
        return new Token(TokenType.NULL, "null");
    }

    public Token readString(){
        StringBuilder sb = new StringBuilder();
        while(true) {
            char ch = charReader.next();
            if (ch == '\\') {   // 处理转义字符
                if (!isEscape()) {
                    throw new JSONParserException("Invalid escape character");
                }
                sb.append('\\');
                ch = charReader.peek();
                sb.append(ch);
                if (ch == 'u') {   // 处理 Unicode 编码，形如 \u4e2d。且只支持 \u0000 ~ \uFFFF 范围内的编码
                    for (int i = 0; i < 4; i++) {
                        ch = charReader.next();
                        if (isHex(ch)) {
                            sb.append(ch);
                        } else {
                            throw new JSONParserException("Invalid character");
                        }
                    }
                }
                /**
                 * 碰到另一个双引号，则认为字符串解析结束，返回 Token
                 */
            } else if (ch == '"') {
                return new Token(TokenType.STRING, sb.toString());
                // 传入的 JSON 字符串不允许换行
            } else if (ch == '\r' || ch == '\n') {
                throw new JSONParserException("Invalid character");
            } else {
                sb.append(ch);
            }
        }    }

    public Token readNumber() {
        char ch = charReader.peek();
        StringBuilder sb = new StringBuilder();
        if (ch == '-') {    // 处理负数
            sb.append(ch);
            ch = charReader.next();
            if (ch == '0') {    // 处理 -0.xxxx
                sb.append(ch);
                sb.append(readFracAndExp());
            } else if (isDigitOneToNine(ch)) {
                do {
                    sb.append(ch);
                    ch = charReader.next();
                } while (isDigit(ch));
                if (ch != (char) -1) {
                    charReader.back();
                    sb.append(readFracAndExp());
                }
            } else {
                throw new JSONParserException("Invalid minus number");
            }
        } else if (ch == '0') {    // 处理小数
            sb.append(ch);
            sb.append(readFracAndExp());
        } else {
            do {
                sb.append(ch);
                ch = charReader.next();
            } while (isDigit(ch));
            if (ch != (char) -1) {
                charReader.back();
                sb.append(readFracAndExp());
            }
        }

        return new Token(TokenType.NUMBER, sb.toString());
    }

    /**
     * 判断是否有乱传转义字符
     * @return
     * @throws
     */
    private boolean isEscape() {
        char ch = charReader.next();
        return (ch == '"' || ch == '\\' || ch == 'u' || ch == 'r'
                || ch == 'n' || ch == 'b' || ch == 't' || ch == 'f' || ch == '/');
    }

    /**
     * 判断是否是十六进制数
     * @param ch
     * @return
     */
    private boolean isHex(char ch) {
        return ((ch >= '0' && ch <= '9') || ('a' <= ch && ch <= 'f')
                || ('A' <= ch && ch <= 'F'));
    }


    public String readFracAndExp(){
        StringBuilder sb = new StringBuilder();
        char ch = charReader.next();
        if (ch ==  '.') {
            sb.append(ch);
            ch = charReader.next();
            if (!isDigit(ch)) {
                throw new JSONParserException("Invalid frac");
            }
            do {
                sb.append(ch);
                ch = charReader.next();
            } while (isDigit(ch));

            if (isExp(ch)) {    // 处理科学计数法
                sb.append(ch);
                sb.append(readExp());
            } else {
                if (ch != (char) -1) {
                    charReader.back();
                }
            }
        } else if (isExp(ch)) {
            sb.append(ch);
            sb.append(readExp());
        } else {
            charReader.back();
        }

        return sb.toString();
    }

    public boolean isDigit(char next){
        return next >= '0' && next <= '9';
    }

    public boolean isDigitOneToNine(char next){
        return next >= '1' && next <= '9';
    }

    public boolean isExp(char next){
        return next == 'e' || next == 'E';
    }

    public String readExp(){
        StringBuilder stringBuilder = new StringBuilder();
        char next = charReader.next();
        if(next == '+' || next == '-'){
            stringBuilder.append(next);
            next = charReader.next();
            if(isDigit(next)){
                do{
                    stringBuilder.append(next);
                    next = charReader.next();
                }while (isDigit(next));
                // 最后一位数据-1(EOF)
                if(next != (char) -1){
                    charReader.back();
                }
            }else {
                throw new JSONParserException("JsonToken解析E or e失败，包含无效字符！");
            }
        }else {
            throw new JSONParserException("JsonToken解析E or e失败，包含无效字符！");
        }
        return stringBuilder.toString();
    }
}
