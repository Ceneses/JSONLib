import parser.Parser;
import tokenizer.TokenList;
import tokenizer.Tokenizer;

/**
 * @ClassName JSONParser
 * @Description //TODO
 * @Author Han ChengYi
 * @Date 2020/10/17 21:12
 * @Version 1.0
 **/

public class JSONParser {
    private Tokenizer tokenizer;
    private Parser parser;

    public JSONParser() {
    }

    public Object fromJson(String jsonString){
       tokenizer = new Tokenizer(jsonString);
       parser = new Parser();
       TokenList tokens = tokenizer.getTokens();
       return parser.parse(tokens);
    }
}
