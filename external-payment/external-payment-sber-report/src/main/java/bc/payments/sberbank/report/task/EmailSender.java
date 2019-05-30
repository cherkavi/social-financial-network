package bc.payments.sberbank.report.task;

import static bc.payments.sberbank.report.task.ReportWriter.FILE_PARAMETER;
import bc.payments.sberbank.report.task.common.SharedInformation;
import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;

import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;


public class EmailSender implements Tasklet, StepExecutionListener{
    private final static Logger LOGGER=Logger.getLogger(EmailSender.class);
    
    @Autowired(required=true)
    private SharedInformation sharedInformation;
    
    @Override
    public RepeatStatus execute(StepContribution sc, ChunkContext chunkContext) throws Exception {
        LOGGER.info("email-sender");
        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution se) {
        LOGGER.info("e-mail sender:before:"+sharedInformation.get(FILE_PARAMETER));
    }

    @Override
    public ExitStatus afterStep(StepExecution se) {
        LOGGER.info("e-mail sender:after");
        return se.getExitStatus();
    }
    
}
