package com.starfire.familytree.web.service;

import java.io.File;

public interface PagePrintService {
    public void fullScreenShot(PagePrintParam pagePrintParam);

    public void printPDF(PagePrintParam pagePrintParam);
}
