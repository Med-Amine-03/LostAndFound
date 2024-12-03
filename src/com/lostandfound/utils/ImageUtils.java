package com.lostandfound.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ImageUtils {

    private static final String IMAGE_UPLOAD_PATH = "src/resources/imagesprofile/";

    public static boolean uploadProfileImage(File profileImage) {
        if (!isValidImage(profileImage)) {
            System.out.println("Invalid image format");
            return false;
        }

        String uniqueImageName = UUID.randomUUID().toString() + ".jpg"; 
        File destination = new File(IMAGE_UPLOAD_PATH + uniqueImageName);

        try {
            Files.copy(profileImage.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error uploading image: " + e.getMessage());
            return false;
        }

        return true;
    }

    public static boolean isValidImage(File file) {
        String[] validExtensions = {".jpg", ".jpeg"};
        String fileName = file.getName().toLowerCase();
        for (String ext : validExtensions) {
            if (fileName.endsWith(ext)) {
                return true;
            }
        }
        return false;
    }
}
