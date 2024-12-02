package ru.otus.hw.datamigration.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.datamigration.config.JobConfig;

@RequiredArgsConstructor
@ShellComponent
public class BatchJobCommands {

    private final Job migrateDbJob;

    private final JobLauncher jobLauncher;

    private final JobExplorer jobExplorer;

    @ShellMethod(value = "startMigrationJobWithJobLauncher", key = {"st", "s", "start"})
    public void startMigrationJobWithJobLauncher() throws Exception {
        try {
            var execution = jobLauncher.run(migrateDbJob, new JobParametersBuilder().toJobParameters());
            System.out.println(execution);
        } catch (Exception e) {
            throw new Exception("Exception in BatchJobCommands startMigrationJobWithJobLauncher()", e);
        }
    }

    @ShellMethod(value = "showInfo", key = "i")
    public void showInfo() {
        System.out.println(jobExplorer.getJobNames());
        System.out.println(jobExplorer.getLastJobInstance(JobConfig.JOB_NAME));
    }
}

