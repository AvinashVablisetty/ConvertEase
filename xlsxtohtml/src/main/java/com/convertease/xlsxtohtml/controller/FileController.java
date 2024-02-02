package com.convertease.xlsxtohtml.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.convertease.xlsxtohtml.service.FileConverterService;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class FileController {

    private final FileConverterService fileConverterService;

    public FileController(FileConverterService fileConverterService) {
        this.fileConverterService = fileConverterService;
    }

    @GetMapping
    public String showForm() {
        return "uploadForm";
    }

    @PostMapping
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            fileConverterService.convertAndSave(file);
            model.addAttribute("message", "File converted successfully!");
        } catch (IOException e) {
            model.addAttribute("error", "Error converting file: " + e.getMessage());
            return "errorPage";
        }

        return "redirect:/result";
    }

    @GetMapping("/result")
    public String showResult() {
        return "result";
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadHtmlFile() {
        // Call a method to get the HTML file content as a byte array (you need to implement this)
        byte[] htmlContent = fileConverterService.getHtmlContent();

        // Create an InputStreamResource from the byte array
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(htmlContent));

        // Set headers for the response
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=output.html");

        // Return ResponseEntity with headers, content type, and status
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.TEXT_HTML)
                .body(resource);
    }
}
