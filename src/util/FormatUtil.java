package util;

import entity.JSONArray;
import entity.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @ClassName FormatUtil
 * @Description //TODO
 * @Author Han ChengYi
 * @Date 2020/10/17 21:08
 * @Version 1.0
 **/

public class FormatUtil {
    private static final char SPACE_CHAR = ' ';
    private static final int INDENT_SIZE = 2;
    private static int callDepth = 0;

    public static String beautify(JSONObject jsonObject) {

        StringBuilder sb = new StringBuilder();
        sb.append(getIndentString());
        sb.append("{");
        callDepth++;

        List<Map.Entry<String, Object>> keyValues = jsonObject.getAllKeyValue();
        int size = keyValues.size();
        for (int i = 0; i < size; i++) {
            Map.Entry<String, Object> keyValue = keyValues.get(i);

            String key = keyValue.getKey();
            Object value = keyValue.getValue();

            sb.append("\n");
            sb.append(getIndentString());
            sb.append("\"");
            sb.append(key);
            sb.append("\"");
            sb.append(": ");

            if (value instanceof JSONObject) {
                sb.append("\n");
                sb.append(beautify((JSONObject) value));
            } else if (value instanceof JSONArray){
                sb.append("\n");
                sb.append(beautify((JSONArray) value));
            } else if (value instanceof String) {
                sb.append("\"");
                sb.append(value);
                sb.append("\"");
            } else {
                sb.append(value);
            }

            if (i < size - 1) {
                sb.append(",");
            }
        }

        callDepth--;
        sb.append("\n");
        sb.append(getIndentString());
        sb.append("}");

        return sb.toString();
    }

    public static String beautify(JSONArray jsonArray) {
        StringBuilder sb = new StringBuilder();
        sb.append(getIndentString());
        sb.append("[");
        callDepth++;

        int size = jsonArray.size();
        for (int i = 0; i < size; i++) {

            sb.append("\n");

            Object ele = jsonArray.get(i);
            if (ele instanceof JSONObject) {
                sb.append(beautify((JSONObject) ele));
            } else if (ele instanceof JSONArray) {
                sb.append(beautify((JSONArray) ele));
            } else if (ele instanceof String) {
                sb.append(getIndentString());
                sb.append("\"");
                sb.append(ele);
                sb.append("\"");
            } else {
                sb.append(getIndentString());
                sb.append(ele);
            }

            if (i < size - 1) {
                sb.append(",");
            }
        }

        callDepth--;
        sb.append("\n");
        sb.append(getIndentString());
        sb.append("]");

        return sb.toString();
    }

    private static String getIndentString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < callDepth * INDENT_SIZE; i++) {
            sb.append(SPACE_CHAR);
        }

        return sb.toString();
    }
}
