package com.starfire.familytree;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Enumeration;

public class Test {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Enumeration<URL> resources = Test.class.getClassLoader().getResources("com.starfire.familytree".replace(".","/"));
        while (resources.hasMoreElements()){
            URL url = resources.nextElement();
            File file = new File(url.toString().replace("file:",""));
            iteratorFile(file);
//            System.err.println(url);
        }


    }

    public static void  iteratorFile(File file) throws ClassNotFoundException {
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file1 = files[i];
                iteratorFile(file1);
            }
        }else{
//            System.err.println(file.getPath());
//            System.err.println(file.getName());
            String classPath = extractClassPath(file.getPath());
            loader(formatPackage(classPath));
//            System.err.println(classPath);
        }
    }

    public static String extractClassPath(String path){
        int classs_idx = path.indexOf("com");
        String classPath = path.substring(classs_idx,path.length()-6);
        return  classPath;
    }

    public static void loader(String classPath) throws ClassNotFoundException {
        if(match(classPath,"entity")){

            Class<?> aClass = Class.forName(classPath);
            System.err.println(aClass.getName());
            Field[] fields = aClass.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                if(field.getName().equals("serialVersionUID")){
                    continue;
                }
                System.err.print("'"+field.getName()+"', ");

            }
            System.err.println("");
        }
    }

    public static String formatPackage(String classPath){
        String replace = classPath.replace("\\", ".");
        return replace;
    }

    public static boolean match(String source,String match){
        if(source.contains(match)){
            return  true;
        }
        return  false;
    }
}
