package parser;

import entity.JSONArray;
import entity.JSONObject;
import exception.JSONParserException;
import tokenizer.Token;
import tokenizer.TokenList;
import tokenizer.TokenType;

/**
 * @ClassName Parser
 * @Description //TODO
 * @Author Han ChengYi
 * @Date 2020/10/17 21:11
 * @Version 1.0
 **/

public class Parser {
    private static final int BEGIN_OBJECT_TOKEN = 1;
    private static final int END_OBJECT_TOKEN = 1<<1;
    private static final int BEGIN_ARRAY_TOKEN = 1<<2;
    private static final int END_ARRAY_TOKEN = 1<<3;
    private static final int NULL_TOKEN = 1<<4;
    private static final int NUMBER_TOKEN = 1<<5;
    private static final int STRING_TOKEN = 1<<6;
    private static final int BOOLEAN_TOKEN = 1<<7;
    private static final int SEP_COLON_TOKEN = 1<<8;
    private static final int SEP_COMMA_TOKEN = 1<<9;

    private TokenList tokens;

    public Object parse(TokenList tokens) {
        this.tokens = tokens;
        return parse();
    }

    public Object parse(){
        Token next = this.tokens.next();
        if(null == next){
            return new JSONObject();
        }else if (next.getType() == TokenType.BEGIN_OBJ){
            return parseJSONObject();
        }else if(next.getType() == TokenType.BEGIN_ARRAY){
            return parseJSONArray();
        }else{
            throw new JSONParserException("解析出错,非有效JSONToken");
        }
    }


    private JSONObject parseJSONObject() {
        JSONObject jsonObject = new JSONObject();
        int expectToken = STRING_TOKEN | END_OBJECT_TOKEN;
        String key = null;
        Object value = null;
        while (tokens.hasToken()) {
            Token token = tokens.next();
            TokenType tokenType = token.getType();
            String tokenValue = token.getValue();
            switch (tokenType) {
                case BEGIN_OBJ:
                    checkExpectToken(tokenType, expectToken);
                    jsonObject.put(key, parseJSONObject());    // 递归解析 json object
                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case END_OBJ:
                    checkExpectToken(tokenType, expectToken);
                    return jsonObject;
                case BEGIN_ARRAY:    // 解析 json array
                    checkExpectToken(tokenType, expectToken);
                    jsonObject.put(key, parseJSONArray());
                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case NULL:
                    checkExpectToken(tokenType, expectToken);
                    jsonObject.put(key, null);
                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case NUMBER:
                    checkExpectToken(tokenType, expectToken);
                    if (tokenValue.contains(".") || tokenValue.contains("e") || tokenValue.contains("E")) {
                        jsonObject.put(key, Double.valueOf(tokenValue));
                    } else {
                        Long num = Long.valueOf(tokenValue);
                        if (num > Integer.MAX_VALUE || num < Integer.MIN_VALUE) {
                            jsonObject.put(key, num);
                        } else {
                            jsonObject.put(key, num.intValue());
                        }
                    }
                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case BOOLEAN:
                    checkExpectToken(tokenType, expectToken);
                    jsonObject.put(key, Boolean.valueOf(token.getValue()));
                    expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    break;
                case STRING:
                    checkExpectToken(tokenType, expectToken);
                    Token preToken = tokens.peekPrevious();
                    /**
                     * 在 JSON 中，字符串既可以作为键，也可作为值。
                     * 作为键时，只期待下一个 Token 类型为 SEP_COLON。
                     * 作为值时，期待下一个 Token 类型为 SEP_COMMA 或 END_OBJECT
                     */
                    if (preToken.getType() == TokenType.SEP_COLON) {
                        value = token.getValue();
                        jsonObject.put(key, value);
                        expectToken = SEP_COMMA_TOKEN | END_OBJECT_TOKEN;
                    } else {
                        key = token.getValue();
                        expectToken = SEP_COLON_TOKEN;
                    }
                    break;
                case SEP_COLON:
                    checkExpectToken(tokenType, expectToken);
                    expectToken = NULL_TOKEN | NUMBER_TOKEN | BOOLEAN_TOKEN | STRING_TOKEN
                            | BEGIN_OBJECT_TOKEN | BEGIN_ARRAY_TOKEN;
                    break;
                case SEP_COMMA:
                    checkExpectToken(tokenType, expectToken);
                    expectToken = STRING_TOKEN;
                    break;
                case END_DOCUMENT:
                    checkExpectToken(tokenType, expectToken);
                    return jsonObject;
                default:
                    throw new JSONParserException("Unexpected Token.");
            }
        }

        throw new JSONParserException("Parse error, invalid Token.");
    }

    private JSONArray parseJSONArray() {
        int expectToken = BEGIN_ARRAY_TOKEN | END_ARRAY_TOKEN | BEGIN_OBJECT_TOKEN | NULL_TOKEN
                | NUMBER_TOKEN | BOOLEAN_TOKEN | STRING_TOKEN;
        JSONArray jsonArray = new JSONArray();
        while (tokens.hasToken()) {
            Token token = tokens.next();
            TokenType tokenType = token.getType();
            String tokenValue = token.getValue();
            switch (tokenType) {
                case BEGIN_OBJ:
                    checkExpectToken(tokenType, expectToken);
                    jsonArray.add(parseJSONObject());
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case BEGIN_ARRAY:
                    checkExpectToken(tokenType, expectToken);
                    jsonArray.add(parseJSONArray());
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case END_ARRAY:
                    checkExpectToken(tokenType, expectToken);
                    return jsonArray;
                case NULL:
                    checkExpectToken(tokenType, expectToken);
                    jsonArray.add(null);
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case NUMBER:
                    checkExpectToken(tokenType, expectToken);
                    if (tokenValue.contains(".") || tokenValue.contains("e") || tokenValue.contains("E")) {
                        jsonArray.add(Double.valueOf(tokenValue));
                    } else {
                        Long num = Long.valueOf(tokenValue);
                        if (num > Integer.MAX_VALUE || num < Integer.MIN_VALUE) {
                            jsonArray.add(num);
                        } else {
                            jsonArray.add(num.intValue());
                        }
                    }
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case BOOLEAN:
                    checkExpectToken(tokenType, expectToken);
                    jsonArray.add(Boolean.valueOf(tokenValue));
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case STRING:
                    checkExpectToken(tokenType, expectToken);
                    jsonArray.add(tokenValue);
                    expectToken = SEP_COMMA_TOKEN | END_ARRAY_TOKEN;
                    break;
                case SEP_COMMA:
                    checkExpectToken(tokenType, expectToken);
                    expectToken = STRING_TOKEN | NULL_TOKEN | NUMBER_TOKEN | BOOLEAN_TOKEN
                            | BEGIN_ARRAY_TOKEN | BEGIN_OBJECT_TOKEN;
                    break;
                case END_DOCUMENT:
                    checkExpectToken(tokenType, expectToken);
                    return jsonArray;
                default:
                    throw new JSONParserException("Unexpected Token.");
            }
        }
        throw new JSONParserException("Parse error, invalid Token.");
    }

    private void checkExpectToken(TokenType tokenType, int expectToken) {
        if ((tokenType.getCode() & expectToken) == 0) {
            throw new JSONParserException("Parse error, invalid Token.");
        }
    }
}
