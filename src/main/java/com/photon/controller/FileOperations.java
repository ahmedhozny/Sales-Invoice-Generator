package com.photon.controller;

import com.photon.InvalidFileTypeException;

import java.io.*;

public class FileOperations extends File{
    private static final String delimiter = ",";

    public FileOperations(File file){
        this(file.getPath());
    }

    public FileOperations(String pathname){
        super(pathname);
        try{
            if (!this.getName().toLowerCase().endsWith(".csv"))
                throw new InvalidFileTypeException(this.getName(), ".csv");
        }catch (InvalidFileTypeException e){
            e.printStackTrace();
        }
    }

    public String[][] readFile(){
        String[] lines = new String[]{};
        FileInputStream f;
        byte[] bytes;
        try {
            f = new FileInputStream(this);
            bytes = new byte[f.available()];
            int result = f.read(bytes);
            f.close();
            lines = new String(bytes).split(System.lineSeparator());
        } catch (FileNotFoundException e){
            System.out.println("File not found. Unable to read data");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[][] content = new String[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            content[i] = lines[i].split(delimiter);
        }

        return content;
    }

    public void writeFile(String[][] content){
        String[] lines = new String[content.length];
        for (int i = 0; i < content.length; i++) {
            lines[i] = String.join(delimiter, content[i]);
        }

        String s = String.join(System.lineSeparator(), lines);

        try {
            if (this.createNewFile()) System.out.println("File " + this.getName() + " not found. Creating new file...");

            FileOutputStream f = new FileOutputStream(this);
            byte[] bytes = s.getBytes();
            f.write(bytes);
            f.close();
        }catch (FileNotFoundException e) {
            System.out.println("Unable to create new file. Save operation failed");
        }catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Save operation failed");
        }
    }
}
