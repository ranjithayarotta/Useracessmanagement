package com.mindlabs.reportgenearator.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mindlabs.reportgenearator.service.XlsxCreateForSlr;

@RestController

public class LsrReportController {

	@Autowired
	private XlsxCreateForSlr slr;

	@GetMapping("/xlsx/slrreport")
	public ResponseEntity<InputStreamResource> generateTheLsrReport() throws IOException, SQLException {

		ByteArrayInputStream in = slr.createXlsxForSlr();

		// return IOUtils.toByteArray(in);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment; filename=slrreport.xlsx");

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));

	}

}
