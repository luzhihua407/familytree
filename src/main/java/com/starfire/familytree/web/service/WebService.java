package com.starfire.familytree.web.service;

import java.io.File;

public interface WebService {
    public void fullScreenShot(String url, File toFile);

    public void printPDF(String url, File toFile);
}
