package ru.otus.hw.datamigration.commands;

import org.h2.tools.Console;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.sql.SQLException;

@ShellComponent
public class H2ConsoleCommands {
    @ShellMethod(value = "Start H2 console", key = "h2")
    public void startConsole() throws SQLException {
        Console.main();
    }
}