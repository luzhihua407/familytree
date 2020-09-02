package com.starfire.familytree.bs.controller;


import com.starfire.familytree.bs.entity.ImageFile;
import com.starfire.familytree.bs.entity.Village;
import com.starfire.familytree.bs.service.IImageFileService;
import com.starfire.familytree.bs.service.IVillageService;
import com.starfire.familytree.response.Response;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author luzh
 * @since 2019-12-18
 */
@RestController
@RequestMapping("imageFile")
public class ImageFileController {

    @Autowired
    private IVillageService villageService;

    @Autowired
    private IImageFileService imageFileService;

    @GetMapping("/getVillageImages")
    public Response<Object> getVillageImages() {
        String villageCode="changqitang";
        Village village = villageService.getVillage(villageCode);
        List<ImageFile> imageFiles = imageFileService.getImageFiles(village.getId());
        Response<Object> response = new Response<>();
        Map<String,Object> map=new HashMap<>();
        map.put("images",imageFiles);
        return response.success(map);

    }

//    public static void main(String[] args) throws IOException {
//        List<String> txt=new ArrayList<>();
//        Document doc = Jsoup.connect("http://www.360doc.com/content/18/0127/23/34974788_725645052.shtml").get();
//        Element artContent = doc.getElementById("artContent");
//        List<Node> nodes = artContent.childNodes();
//        for (int i = 0; i < nodes.size(); i++) {
//            Node node =  nodes.get(i);
//            List<Node> nodeList = node.childNodes();
//            for (int j = 0; j < nodeList.size(); j++) {
//                Node node1 =  nodeList.get(j);
//                List<Node> childNodes = node1.childNodes();
//                for (int k = 0; k < childNodes.size(); k++) {
//                    Node node2 =  childNodes.get(k);
//                    String s = node2.toString();
//                    if(!s.contains("<img") && !s.contains("<br")){
//                        if(s.startsWith("<strong>")){
//                            txt.add("");
//                            txt.add(s.replace("<strong>","").replace("</strong>",""));
//                            txt.add("");
//                        }else{
//                            txt.add(s);
//                        }
//
//                    }
//
//                }
//
//            }
//        }
//        String text = artContent.text();
//        FileUtils.writeLines(new File("D:/3.txt"),txt,false);
//    }

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\86137\\IdeaProjects\\server3");
        loopDir(file);
        System.err.println("Over 。。。。。。");
    }

    private static void loopDir(File file) throws IOException {
        boolean directory = file.isDirectory();
        if(directory){
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file1 = files[i];
                loopDir(file1);
            }
        }else{
            readFile(file);
        }

    }

    private static void readFile(File file) throws IOException {
        boolean flag=false;
        StringBuffer sb=new StringBuffer();
        List<String> readLines = FileUtils.readLines(file, "UTF-8");
        for (int j = 0; j < readLines.size(); j++) {
            String s =  readLines.get(j);
           boolean anEnum = s.contains("@UserEnum");
            if(anEnum){
                flag=true;
            }
            if(flag){
                sb.append(s.replace("@UserEnum(\"","").replace("\")",""));
                sb.append(" ");
                String clazz = readLines.get(j + 1);
                sb.append(clazz.replace("public enum ","").replace("{",""));
                for (int i = 2; i <5 ; i++) {
                    String s1 = readLines.get(j + i);
                    if(s1.contains("(")){
                        sb.append(s1);

                    }
                    if(s1.contains(";")){
                        flag=false;
                        sb.append("\n");
                        System.err.println(sb.toString());
                        FileUtils.write(new File("D:/aa.txt"),sb.toString(),"UTF-8",true);
                        break;
                    }
                }


            }
        }


    }
}
