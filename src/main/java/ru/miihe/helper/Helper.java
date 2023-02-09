package ru.miihe.helper;

import org.springframework.stereotype.Component;
import ru.miihe.params.Rates;
import ru.miihe.params.Sales;
import ru.miihe.exceptions.SmartException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Component
public class Helper {

    public List<Sales> getSalesList(String pathToFile) {
        Path path = Path.of(pathToFile);
        String productName = path.getFileName().toString().split("_")[0];
        try(Stream<String> stream = Files.lines(path)) {
            List<Sales> listOfSales = new ArrayList<>();
            stream.map(f1 -> f1.replaceAll(" ", ""))
                    .map(f -> f.split("\t"))
                    .forEach(line -> listOfSales.add(new Sales(productName, dateTransfer(line[0]), Long.valueOf(line[1]))));
            return listOfSales;
        } catch (IOException e) {
            throw new SmartException("File: " + pathToFile + " - is not valid name");
        }
    }

    public List<Rates> getRatesList(String pathToFile) {
        Path path = Path.of(pathToFile);
        String productName = path.getFileName().toString().split("_")[0];
        try(Stream<String> stream = Files.lines(path)) {
            List<Rates> listOfRates = new ArrayList<>();
            stream.map(f1 -> f1.replaceAll(" ", ""))
                    .map(f2 -> f2.split("\t"))
                    .forEach(line -> listOfRates.add(new Rates(productName, dateTransfer(line[0]), Long.valueOf(line[1]))));
            return listOfRates;
        } catch (IOException e) {
            throw new SmartException("File: " + pathToFile + " - is not valid name");
        }
    }

    private LocalDate dateTransfer(String input) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy");
        return LocalDate.parse(input, formatter);
    }
}
