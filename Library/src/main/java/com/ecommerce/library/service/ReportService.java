package com.ecommerce.library.service;

import jakarta.servlet.http.HttpServletResponse;

public interface ReportService {

    public void generateExcel(HttpServletResponse httpServletResponse);
    
}
