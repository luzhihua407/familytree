package com.starfire.familytree.web.service;

import lombok.Data;
import java.io.File;
@Data
public class PagePrintParam {

    private String loginURL;

    private String username;

    private String password;

    private String url;

    private File toFile;
}
