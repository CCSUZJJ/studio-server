package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/30
 * @version: 1.7
 */
public class ErrorMessage extends HashMap<String,List<String> >{
    public boolean hasError(){
        return !this.isEmpty();
    }

    @SuppressWarnings("unchecked")
    public ErrorMessage add(ErrorMessage another){
        for (Map.Entry entry : another.entrySet()) {
            if (this.containsKey(entry.getKey())){
                List<String> errs = this.get(entry.getKey());
                errs.addAll(((List<String>) entry.getValue()));
                this.put(entry.getKey().toString(), errs);
            } else {
                this.put(entry.getKey().toString(), (List<String>) entry.getValue());
            }
        }
        return this;
    }
}
