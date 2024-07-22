package org.zydd.bebtpn.service;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.zydd.bebtpn.dto.ResponGetAllData;
import org.zydd.bebtpn.entity.Orders;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {
    private final OrderService orderService;

    @Autowired
    public ReportService(OrderService orderService) {
        this.orderService = orderService;
    }


    public File exportJasperReport(String name, String item) throws JRException, IOException {
        ResponGetAllData<Orders> data = orderService.getAllOrder(name, item, 1, 100);
        List<Orders> ordersList = data.getData();

        // Get file and compile it
        File file = ResourceUtils.getFile("classpath:order_report.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(ordersList);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Simplifying Tech");

        // Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // Define the output directory and file name
        String reportDir = "src/main/resources/report";
        File reportDirFile = new File(reportDir);
        if (!reportDirFile.exists()) reportDirFile.mkdirs(); // Create directory if it does not exist

        String fileName = "order_report_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf";
        File pdfFile = new File(reportDirFile, fileName);

        // Export report to PDF
        try (OutputStream outputStream = new FileOutputStream(pdfFile)) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        }

        return pdfFile;
    }
}

