package tokenizer;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TokenList
 * @Description //TODO
 * @Author Han ChengYi
 * @Date 2020/10/17 17:04
 * @Version 1.0
 **/

public class TokenList {
    private List<Token> tokens;
    private int index = -1;

    public TokenList() {
        this.tokens = new ArrayList<Token>();
    }

    public void addToken(Token token) {
        this.tokens.add(token);
    }

    public Token peek(){
        if(index == -1){
            return null;
        }
        return tokens.get(index);
    }

    public Token peekPrevious(){
        return index - 1 < 0 ? null : tokens.get(index - 1);
    }

    // 取出下一个Token
    public Token next(){
        return tokens.get(++index);
    }

    // 还有Token()
    public boolean hasToken(){
        return index < tokens.size() - 1;
    }


    public List<Token> getTokens() {
        return tokens;
    }


}
