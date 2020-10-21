package entity;

import exception.JSONTypeException;
import util.FormatUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName JSONObject
 * @Description //TODO
 * @Author Han ChengYi
 * @Date 2020/10/17 20:56
 * @Version 1.0
 **/

public class JSONObject {
    private Map<String, Object> map = new HashMap<String, Object>();

    public void put(String key, Object value) {
        map.put(key, value);
    }

    public Object get(String key) {
        return map.get(key);
    }

    public List<Map.Entry<String, Object>> getAllKeyValue() {
        return new ArrayList<Map.Entry<String, Object>>(map.entrySet());
    }

    public JSONObject getJsonObject(String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }

        Object obj = map.get(key);
        if (!(obj instanceof JSONObject)) {
            throw new JSONTypeException("Type of value is not JsonObject");
        }

        return (JSONObject) obj;
    }

    public JSONArray getJsonArray(String key) {
        if (!map.containsKey(key)) {
            throw new IllegalArgumentException("Invalid key");
        }

        Object obj = map.get(key);
        if (!(obj instanceof JSONArray)) {
            throw new JSONTypeException("Type of value is not JsonArray");
        }

        return (JSONArray) obj;
    }

    @Override
    public String toString() {
        return FormatUtil.beautify(this);
    }

}
