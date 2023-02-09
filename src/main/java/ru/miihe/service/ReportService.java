package ru.miihe.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.miihe.helper.Helper;
import ru.miihe.params.Rates;
import ru.miihe.entity.Report;
import ru.miihe.params.Sales;
import ru.miihe.repository.ReportRepository;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

@Service
public class ReportService {

    private final ReportRepository reportRepository;

    private final Helper helper;

    @Autowired
    public ReportService(ReportRepository reportRepository, Helper helper) {
        this.reportRepository = reportRepository;
        this.helper = helper;
    }

    @Transactional
    public void calculateReport(String ratesFile, String salesFile) {
        List<Rates> ratesList = helper.getRatesList(ratesFile);
        List<Sales> salesList = helper.getSalesList(salesFile);

        List<Report> reportListTemp = new ArrayList<>();

        for (Sales sale : salesList) {
            ListIterator<Rates> iterator = ratesList.listIterator();
            Rates rateCurrent = iterator.next();
            do {
                Rates rateNext;
                if (iterator.hasNext()) {
                    rateNext = iterator.next();
                } else {
                    rateNext = new Rates(rateCurrent.getProductName(), rateCurrent.getDate().plusYears(1), 0L);
                }
                if (sale.getDate().isEqual(rateCurrent.getDate()) | sale.getDate().isAfter(rateCurrent.getDate())
                        & sale.getDate().isBefore(rateNext.getDate())) {
                    reportListTemp.add(Report.builder()
                            .productName(sale.getProductName())
                            .period(transformToMonthYear(sale.getDate()))
                            .sales(sale.getQuantity() * rateCurrent.getCost())
                            .build());
                }
                rateCurrent = rateNext;
            } while (iterator.hasNext() || (sale.getDate().equals(rateCurrent.getDate()) || sale.getDate().isAfter(rateCurrent.getDate())));
        }
        for (Report report: reportListTemp) {
            saveOrUpdate(report);
        }
    }

    @Transactional
    private void saveOrUpdate(Report report) {
        if (reportRepository.existsByProductName(report.getProductName()) && reportRepository.existsByPeriod(report.getPeriod())) {
            reportRepository.update(report.getSales(), report.getProductName(), report.getPeriod());
        } else {
            reportRepository.saveAndFlush(report);
        }
    }

    private String transformToMonthYear(LocalDate date) {
        return date.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("ru")) + " " + date.getYear();
    }
}
