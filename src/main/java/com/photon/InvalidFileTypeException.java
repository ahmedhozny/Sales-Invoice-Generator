package com.photon;

import java.io.IOException;

public class InvalidFileTypeException extends IOException {
    public InvalidFileTypeException(String path, String acceptedTypeMask){
        super(String.format("File %s is invalid. Expected extension: %s", path, acceptedTypeMask));
    }
}