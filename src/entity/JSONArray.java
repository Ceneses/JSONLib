package entity;

import exception.JSONTypeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName JSONArray
 * @Description //TODO
 * @Author Han ChengYi
 * @Date 2020/10/17 20:56
 * @Version 1.0
 **/

public class JSONArray {
    private List<Object> list = new ArrayList<>();

    public void add(Object object){
        list.add(object);
    }

    public Object get(int index){
        return list.get(index);
    }

    public int size(){
        return list.size();
    }

    public JSONObject getJsonObject(int index){
        Object obj = list.get(index);
        if (!(obj instanceof JSONObject)) {
            throw new JSONTypeException("值的类型不是JSONObject");
        }
        return (JSONObject) obj;
    }

    public JSONArray getJsonArray(int index){
        Object obj = list.get(index);
        if (!(obj instanceof JSONArray)) {
            throw new JSONTypeException("值的类型不是JSONObject");
        }
        return (JSONArray) obj;
    }

    @Override
    public String toString() {
        return null;
    }

    public Iterator iterator(){
        return list.iterator();
    }
}
