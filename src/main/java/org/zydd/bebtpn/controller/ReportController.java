package org.zydd.bebtpn.controller;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zydd.bebtpn.service.ReportService;

import java.io.File;
import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/v1")
public class ReportController {
    private final ReportService service;

    @Autowired
    public ReportController(ReportService service) {
        this.service = service;
    }


    @GetMapping("/report-download")
    public ResponseEntity<Resource> createPDF(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "item", required = false) String item) throws IOException, JRException {

        // Generate report and get the file
        File pdfFile = service.exportJasperReport(name, item);

        // Prepare response
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pdfFile.getName());
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfFile.length())
                .body(new FileSystemResource(pdfFile));
    }
}
