package com.starfire.familytree.web.service;

import java.io.File;

public interface PagePrintService {
    public void fullScreenShot(String host,String sessionId,String url, File toFile);

    public void printPDF(String host,String sessionId,String url, File toFile);
}
