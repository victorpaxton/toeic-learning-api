package com.tma.english.file;

import com.tma.english.exception.NotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class FilesStorageService {

    private static String OS = System.getProperty("os.name").toLowerCase();

    public String getRootPath() throws Exception {
        if (isWindows() || isUnix()) {
            return "./home/static";
        } else {
            throw new Exception("Cannot set root path because of Unknown OS");
        }
    }

    private Path convertRelativeToAbsolutePath(String relativePath) throws Exception {
        return Paths.get(getRootPath() + relativePath);
    }

    public void saveAs(MultipartFile file, String relativePath) {
        try {
            File directory = new File(convertRelativeToAbsolutePath(relativePath).getParent().toString()); //check folder cha đã có chưa

            if (!directory.exists()) {
                directory.mkdirs();
            }

            Files.copy(file.getInputStream(), convertRelativeToAbsolutePath(relativePath)); //Lưu file vào folder ở dòng 42
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }

    public Resource load(String filename) {
        try {
            Path file = convertRelativeToAbsolutePath(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file !");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

    }
    public Resource loadFileFromResource(String filename) {
        try {
            File filePath = new File(this.getClass().getClassLoader().getResource(filename).getFile());
            Resource resource = new UrlResource(filePath.toURI());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file !");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }

    }

    public boolean deleteFile(String relativePath) throws NotFoundException {

        try {
            File directory = new File(convertRelativeToAbsolutePath(relativePath).toString());

            return directory.delete();
        } catch (Exception e) {
            throw new NotFoundException("File is not found");
        }

    }

    private boolean isWindows() {
        return OS.contains("win");
    }

    private boolean isMac() {
        return OS.contains("mac");
    }

    private Boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }

    private boolean isSolaris() {
        return OS.contains("sunos");
    }

    public String uploadFile(MultipartFile file, String basePath) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddHHmmssSSS_");
        String currentDateTime = dateFormatter.format(new Date());
//        String url = currentDateTime + file.getOriginalFilename();
//        String Path = basePath + "/" + currentDateTime + file.getOriginalFilename();
        String path = basePath + "/" + currentDateTime + file.getOriginalFilename();
        saveAs(file, path);
        return path;
    }
}

