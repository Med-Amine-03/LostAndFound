package com.lostandfound.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ImageUtils {

    public static String saveProfileImage(File sourceFile) {
        return saveImage(sourceFile, Constants.PROFILE_IMAGES_PATH);
    }

    public static String saveItemImage(File sourceFile) {
        return saveImage(sourceFile, Constants.ITEM_IMAGES_PATH);
    }

    private static String saveImage(File sourceFile, String targetDirectory) {
        try {
            Path dirPath = Paths.get(targetDirectory);
            Files.createDirectories(dirPath);
            
            String fileName = UUID.randomUUID().toString() + getFileExtension(sourceFile.getName());
            Path targetPath = dirPath.resolve(fileName);
            
            Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            return fileName.substring(i);
        }
        return ".jpg";
    }

    public static boolean isValidImage(File file) {
        String[] validExtensions = {".jpg", ".jpeg", ".png"};
        String fileName = file.getName().toLowerCase();
        for (String ext : validExtensions) {
            if (fileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
