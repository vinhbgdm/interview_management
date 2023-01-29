package com.fa.ims.service.impl;

import com.fa.ims.service.FileStorageService;
import com.fa.ims.util.VNCharacterUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author DatNV27
 */
@Service
public class FileLocalStorageService implements FileStorageService {
    @Value("${app.file.location.cv}")
    String fileLocation;

    @Override
    public String saveFile(MultipartFile file, String folderUser) throws IOException {
        Path getRelativePathFile = getRelativePathFile(folderUser,
                Objects.requireNonNull(VNCharacterUtils.removeAccent(file.getOriginalFilename())).replace(" ", "_"));
        createPathFile(folderUser);
        file.transferTo(Paths.get(fileLocation).resolve(getRelativePathFile));
        return getRelativePathFile.toString();
    }

    @Override
    public Resource loadFileAsResource(String relativePath) throws MalformedURLException, FileNotFoundException {
        Path absolutePath = Paths.get(fileLocation).resolve(relativePath);
        Resource resource = new UrlResource(absolutePath.toUri());
        if (resource.exists()) {
            return resource;
        }
        throw new FileNotFoundException("Can not find file with url " + relativePath);
    }

    private void createPathFile(String folderUser) throws IOException {
        Path fileLocationPath = Paths.get(fileLocation);
        if (StringUtils.hasLength(folderUser)) {
            fileLocationPath = fileLocationPath.resolve(folderUser);
        }
        try {
            Files.createDirectories(fileLocationPath);
        } catch (FileAlreadyExistsException ignore) {
        }
    }

    private Path getRelativePathFile(String folderUser, String fileName) {
        return (StringUtils.hasLength(folderUser)) ? Paths.get(folderUser).resolve(fileName) : Paths.get(fileName);
    }
}

