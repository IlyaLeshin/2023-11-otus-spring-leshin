package ru.otus.hw.datamigration.commands;

import org.h2.tools.Console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.sql.SQLException;

@ShellComponent
public class H2ConsoleCommands {
    @ShellMethod(value = "Start H2 console", key = "h2")
    public void startConsole() throws SQLException {
        Logger logger = LoggerFactory.getLogger(H2ConsoleCommands.class);
        logger.info("""
                Started H2 console
                username: sa
                password: 1
                """);
        Console.main();
    }
}