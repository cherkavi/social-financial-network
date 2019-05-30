/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bc.payments.sberbank.report.task;

import bc.payments.sberbank.report.task.common.SharedInformation;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author technik
 */
public class ReportWriter implements ItemWriter<String>, StepExecutionListener{
    private final static Logger LOGGER=Logger.getLogger(ReportWriter.class);
    public final static String FILE_PARAMETER="DATE_FILE_PATH";
    
    @Autowired(required=true)
    private SharedInformation sharedInformation;
    /**
     * @param list data from processor
    */
    @Override
    public void write(List<? extends String> list) throws Exception {
        LOGGER.info("output value="+list);
    }

    @Override
    public void beforeStep(StepExecution se) {
        LOGGER.info("before job");
    }

    @Override
    public ExitStatus afterStep(StepExecution se) {
        sharedInformation.put(FILE_PARAMETER, new JobParameter(" this is stub "));
        LOGGER.info("after job:"+se.getExecutionContext());
        return se.getExitStatus();
    }
    
}
