package com.photon;

import java.io.IOException;
import java.util.Arrays;

/**
 * Exception: Thrown when provided file does have a valid extension
 */
public class InvalidFileTypeException extends IOException {
    public InvalidFileTypeException(String path, String[] acceptedTypeMasks){
        super(String.format("File %s is invalid. Expected extensions: %s", path, Arrays.toString(acceptedTypeMasks)));
    }
}