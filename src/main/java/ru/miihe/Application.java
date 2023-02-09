package ru.miihe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.miihe.service.ReportService;

import java.util.Scanner;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final ReportService reportService;

    @Autowired
    public Application(ReportService reportService) {
        this.reportService = reportService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        while(true) {
            String newIteration = scanner.nextLine();
            if (newIteration.equals("go")) {
                String pathToFileRates = "src/main/resources/test-files/phone_rates.txt";
                String pathToFileSales = "src/main/resources/test-files/phone_sales.txt";
                reportService.calculateReport(pathToFileRates, pathToFileSales);
            } else if (newIteration.equals("exit")) {
                System.exit(0);
            }
        }
    }
}
