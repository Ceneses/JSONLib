import entity.JSONArray;
import entity.JSONObject;
import tokenizer.Token;
import tokenizer.TokenList;
import tokenizer.Tokenizer;

/**
 * @ClassName Test
 * @Description //TODO
 * @Author Han ChengYi
 * @Date 2020/10/17 20:17
 * @Version 1.0
 **/

public class Test {
    public static void main(String[] args) {
//        Tokenizer tokenizer = new Tokenizer("{\"name\":\"HCY\",\"Group1\":false,\"keys\":[\"point1\":\"ppp\"]}");
//        TokenList tokens = tokenizer.getTokens();
//        for (Token token: tokens.getTokens()) {
//            System.out.println(token.toString());
//        }
        JSONParser jsonParser = new JSONParser();
        JSONObject o = (JSONObject) jsonParser.fromJson("{\"name\":\"HCY\",\"Group1\":false,\"keys\":[\"point1\",\"ppp\"]}");
        System.out.println(o);
    }
}
