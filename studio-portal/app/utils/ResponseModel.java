package utils;

import java.util.Map;

/**
 * @author: Zhang.Min
 * @since: 2016/3/16
 * @version: 1.7
 */
public class ResponseModel {
    private Integer status;
    private Map<String, Object> result;

    public ResponseModel(Integer status, Map<String, Object> result) {
        this.status = status;
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }
}
