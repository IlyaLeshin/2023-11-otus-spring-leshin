package ru.otus.hw.shell.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import ru.otus.hw.domain.Login;
import ru.otus.hw.service.IdentificationService;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent
@RequiredArgsConstructor
public class ApplicationCommands {

    private final IdentificationService identificationService;

    private final TestRunnerService testRunnerService;

    private final LocalizedIOService localizedIOService;

    private Login userName;

    @ShellMethod(value = "Log in", key = {"l", "log"})
    @ShellMethodAvailability(value = "isLogAvailable")
    public String logIn() {
        userName = identificationService.identify();
        return localizedIOService.getMessage("Shell.identification.pass");
    }

    @ShellMethod(value = "Start test", key = {"s", "t", "st", "start", "test"})
    @ShellMethodAvailability(value = "isTestRunAvailable")
    public String startTest() {
        testRunnerService.run();
        return localizedIOService.getMessage("Shell.test.application.finish");
    }

    @ShellMethod(value = "Log out", key = {"lo", "logout"})
    @ShellMethodAvailability(value = "isTestRunAvailable")
    public String logOut() {
        userName = null;
        return localizedIOService.getMessage("Shell.identification.reset");
    }

    private Availability isTestRunAvailable() {
        return userName == null
                ? Availability.unavailable(localizedIOService.getMessage("Shell.identification.fail"))
                : Availability.available();
    }

    private Availability isLogAvailable() {
        return userName != null
                ? Availability.unavailable(localizedIOService.getMessage("Shell.identification.start.fail"))
                : Availability.available();
    }
}
