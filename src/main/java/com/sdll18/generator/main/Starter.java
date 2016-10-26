package com.sdll18.generator.main;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright (C) 2015 - 2016 SOHU FOCUS Inc., All Rights Reserved.
 *
 * @Author: junlanli@sohu-inc.com
 * @Date: 2016-10-26
 */
public class Starter {

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("[ERROR]  args are not enough");
            return;
        }
        String fromLocation = args[0];
        String toLocation = args[1];
        File from = new File(fromLocation);
        File to = new File(toLocation);
        if (from.isDirectory()) {
            File[] files = from.listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    File tof = new File(to.getAbsolutePath() + "/" + file.getName() + ".generate");
                    File tof2 = new File(to.getAbsolutePath() + "/" + file.getName());
                    generate(file, tof);
                    addAnnotation(file,tof2);
                }
            }
        } else {
            generate(from, to);
        }
        System.out.println("[INFO] SUCCESS!");
    }

    public static String changeName(String old) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < old.length(); i++) {
            char a = old.charAt(i);
            if (a >= 'A' && a <= 'Z') {
                sb.append("_");
                sb.append(a);
            } else {
                sb.append(a);
            }
        }
        return sb.toString().toLowerCase();
    }

    public static void addAnnotation(File from, File to) throws IOException {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        StringBuilder sb = new StringBuilder();
        try {
            fr = new FileReader(from);
            br = new BufferedReader(fr);
            String line = br.readLine();
            Pattern pattern = Pattern.compile(" *private *[a-zA-Z,<,>]* *[a-zA-Z]*;");
            Pattern p = Pattern.compile("[a-zA-Z]*;$");
            String className = "";
            while (line != null) {
                if(line.contains("public class ")){
                    Pattern p1 = Pattern.compile("[a-zA-z]+Form");
                    Matcher m1 = p1.matcher(line);
                    if (m1.find()) {
                        className = m1.group();
                    }
                }
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        String pName = m.group();
                        pName = pName.substring(0, pName.length() - 1);
                        sb.append("    @Field(value = ");
                        sb.append(className);
                        sb.append("Tag.");
                        sb.append("FIELD_");
                        sb.append(changeName(pName).toUpperCase());
                        sb.append(")\n");
                        sb.append("    @JSONField(name = ");
                        sb.append(className);
                        sb.append("Tag.");
                        sb.append("JSON_FIELD_");
                        sb.append(pName.toUpperCase());
                        sb.append(")\n");
                    }
                    sb.append(line);
                    sb.append("\n");
                } else {
                    sb.append(line);
                    sb.append("\n");
                }
                line = br.readLine();
            }
            fw = new FileWriter(to);
            bw = new BufferedWriter(fw);
            bw.write(sb.toString());
        } finally {
            if (br != null) {
                br.close();
            }
            if (fr != null) {
                fr.close();
            }
            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        }
    }

    public static void generate(File from, File to) throws IOException {
        FileReader fr = null;
        BufferedReader br = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        StringBuilder sb = new StringBuilder();
        try {
            fr = new FileReader(from);
            br = new BufferedReader(fr);
            String line = br.readLine();
            Pattern pattern = Pattern.compile(" *private *[a-zA-Z,<,>]* *[a-zA-Z]*;");
            Pattern p = Pattern.compile("[a-zA-Z]*;$");
            while (line != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        String pName = m.group();
                        pName = pName.substring(0, pName.length() - 1);
                        sb.append("String JSON_FIELD_");
                        sb.append(pName.toUpperCase());
                        sb.append(" = \"");
                        sb.append(pName);
                        sb.append("\";\n\n");
                        sb.append("String FIELD_");
                        sb.append(changeName(pName).toUpperCase());
                        sb.append(" = \"");
                        sb.append(changeName(pName));
                        sb.append("\";\n\n");
                    }
                }
                line = br.readLine();
            }
            fw = new FileWriter(to);
            bw = new BufferedWriter(fw);
            bw.write(sb.toString());
        } finally {
            if (br != null) {
                br.close();
            }
            if (fr != null) {
                fr.close();
            }
            if (bw != null) {
                bw.close();
            }
            if (fw != null) {
                fw.close();
            }
        }
    }
}
