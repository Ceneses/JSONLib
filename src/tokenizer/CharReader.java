package tokenizer;

/**
 * @ClassName CharReader
 * @Description //TODO
 * @Author Han ChengYi
 * @Date 2020/10/17 17:06
 * @Version 1.0
 **/

public class CharReader {
    private String inputString;
    private char[] outputCharArray;
    private int index = -1;
    private int size = 0;

    public CharReader(String inputString) {
        this.inputString = inputString;
        this.outputCharArray = inputString.toCharArray();
        this.index = -1;
        this.size = inputString.length();
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
        this.outputCharArray = inputString.toCharArray();
        this.index = -1;
        this.size = inputString.length();
    }

    public char[] getOutputCharArray(){
        return this.outputCharArray;
    }

    public char peek(){
        if(index == -1){
            return '\u0000';
        }else {
            return outputCharArray[index];
        }
    }

    public char next(){
        if (hasNext()) {
            return this.outputCharArray[++index];
        }else{
            return '\u0000';
        }
    }

    public void back(){
        index--;
    }

    public boolean hasNext(){
        return this.index < this.size - 1;
    }
}
