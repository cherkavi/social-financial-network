package bc.payments.sberbank.report;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * execute job
 */
public class ReportReader {
	private final static Logger	LOGGER = Logger.getLogger(ReportReader.class);
	
	/** path to JOB description */
	private final static String[]	CONTEXT_PATH = new String[]{"spring/batch/config/context.xml", "spring/batch/jobs/batch-job.xml"};
	/** name of job into batch job description file  */
	private final static String SPRING_DESCRIPTION_JOB_NAME="sberReportJob";
	
	private final static String DEFAULT_PROPERTIES_SPRING_KEY="main.properties";
	/** default name of file with properties */
	private final static String DEFAULT_PROPERTIES_FILENAME="/home/technik/projects/nmtg/wm-system/trunk/external-payment-sber-report/application-demo.properties";

	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		LOGGER.info("begin");
		JobExecution execution = null;
		try {
			if(args.length>0){
				System.setProperty(DEFAULT_PROPERTIES_SPRING_KEY, args[0]);
			}else{
				System.setProperty(DEFAULT_PROPERTIES_SPRING_KEY, DEFAULT_PROPERTIES_FILENAME);
			}
			AbstractApplicationContext context=new ClassPathXmlApplicationContext(CONTEXT_PATH);
			
			execution = executeJob(context, SPRING_DESCRIPTION_JOB_NAME);  
			
		} catch (JobExecutionAlreadyRunningException ex) {
			LOGGER.error(ex.getMessage());
			System.exit(1);
		} catch (JobRestartException ex) {
			LOGGER.error(ex.getMessage());
			System.exit(1);
		} catch (JobInstanceAlreadyCompleteException ex) {
			LOGGER.error(ex.getMessage());
			System.exit(1);
		} catch (JobParametersInvalidException ex) {
			LOGGER.error(ex.getMessage());
			System.exit(1);
		}
		LOGGER.info("end:" + execution.getStatus());
	}

	private static JobExecution executeJob(ApplicationContext context, String jobName) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("sberReportJob");
		JobParameters jobParameters = new JobParametersBuilder().addDate("ExecutionTime:", new Date()).toJobParameters();
		return jobLauncher.run(job, jobParameters);
	}

}
