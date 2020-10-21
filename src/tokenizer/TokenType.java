package tokenizer;

/**
 * @ClassName TokenType
 * @Description //TODO
 * @Author Han ChengYi
 * @Date 2020/10/17 16:55
 * @Version 1.0
 **/

public enum TokenType {
    /**
     * 2 对 OBJECT 和 ARRAY
     * BEGIN_OBJ(1<<0),
     * BEGIN_ARRAY(1<<1),
     * END_OBJ(1<<2),
     * END_ARRAY(1<<3),
     * 4 个 Value
     * STRING(1<<4),
     * NUMBER(1<<5),
     * NULL(1<<6),
     * BOOLEAN(1<<7),
     * 2 个 分隔符
     * SEP_COLON(1<<8),
     * SEP_COMMA(1<<9),
     * 1 个 结束符
     * END_DOCUMENT(1<<10);
     */
    BEGIN_OBJ(1<<0),
    END_OBJ(1<<1),
    BEGIN_ARRAY(1<<2),
    END_ARRAY(1<<3),
    NULL(1<<4),
    NUMBER(1<<5),
    STRING(1<<6),
    BOOLEAN(1<<7),
    SEP_COLON(1<<8),
    SEP_COMMA(1<<9),
    END_DOCUMENT(1<<10);

    private int code;

    TokenType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
