package com.photon.controller;

import com.photon.InvalidFileTypeException;

import java.io.*;

/**
 * Custom file manager class that extends the popular File class.
 */
public class FileOperations extends File{
    private static final String delimiter = ",";

    public FileOperations(String pathname) throws InvalidFileTypeException {
        super(pathname);
            if (!this.getName().toLowerCase().endsWith(".csv"))
                throw new InvalidFileTypeException(this.getName(), new String[]{".csv"});
    }

    public String[][] readFile(){
        String[] lines = new String[]{};
        byte[] bytes;
        try {
            FileInputStream f = new FileInputStream(this);
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
