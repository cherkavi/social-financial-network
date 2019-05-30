package bc.payments.sberbank.report.task;


import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemProcessor;


public class RecordToStringConverter implements ItemProcessor<List<String>, String>{

    @Override
    public String process(List<String> list) throws Exception {
        return Arrays.toString(list.toArray());
    }
    
}
