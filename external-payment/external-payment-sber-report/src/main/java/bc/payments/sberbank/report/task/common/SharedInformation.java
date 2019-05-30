/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc.payments.sberbank.report.task.common;

import java.util.HashMap;
import java.util.Map;

/**
 * hold shared information between different <b>batch:step</b>
 */
public class SharedInformation {
    private Map<String, Object> data=new HashMap<String, Object>();
    
    public void put(String key, Object value){
        this.data.put(key, value);
    }
    
    public Object get(String key){
        return this.data.get(key);
    }
}
