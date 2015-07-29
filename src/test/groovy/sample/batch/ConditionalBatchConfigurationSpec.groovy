package sample.batch

import org.springframework.batch.core.*
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import sample.BaseSpecification

class ConditionalBatchConfigurationSpec extends BaseSpecification {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job conditionalJob;

    def "Normal"() {
        setup:
        Map<String, JobParameter> m = new HashMap<>()
        m.put("time", new JobParameter(System.currentTimeMillis()))
        JobParameters jobParameters = new JobParameters(m)

        when:
        JobExecution jobExecution = jobLauncher.run(conditionalJob, jobParameters)

        then:
        jobExecution.getStatus() == BatchStatus.COMPLETED

        and:
        def list = []
        jobExecution.getStepExecutions().each { list << it.getStepName() }
        list == ['conditionalStep1', 'conditionalStep2']
    }

    def "Fail1"() {
        setup:
        Map<String, JobParameter> m = new HashMap<>()
        m.put("time", new JobParameter(System.currentTimeMillis()))
        m.put("fail", new JobParameter("1"))
        JobParameters jobParameters = new JobParameters(m)

        when:
        JobExecution jobExecution = jobLauncher.run(conditionalJob, jobParameters)

        then:
        jobExecution.getStatus() == BatchStatus.COMPLETED

        and:
        def list = []
        jobExecution.getStepExecutions().each { list << it.getStepName() }
        list == ['conditionalStep1', 'conditionalStep3']
    }

    def "Fail2"() {
        setup:
        Map<String, JobParameter> m = new HashMap<>()
        m.put("time", new JobParameter(System.currentTimeMillis()))
        m.put("fail", new JobParameter("2"))
        JobParameters jobParameters = new JobParameters(m)

        when:
        JobExecution jobExecution = jobLauncher.run(conditionalJob, jobParameters)

        then:
        jobExecution.getStatus() == BatchStatus.STOPPED

        and:
        def list = []
        jobExecution.getStepExecutions().each { list << it.getStepName() }
        list == ['conditionalStep1', 'conditionalStep4', 'conditionalStep5']
    }
}
