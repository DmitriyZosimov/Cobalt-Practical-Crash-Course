package com.cobaltcourse.web;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@CrossOrigin(exposedHeaders = {"File-Content"})
@RestController
@RequestMapping("/**")
public class MainController {

    @RequestMapping(method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity main(HttpServletRequest httpRequest) {
        String url = httpRequest.getRequestURI().replaceAll("%20", " ");
        System.out.println("URL: ---------> " + url);
        File file = new File(url);
        if(file.isDirectory()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put("File-Content", Collections.singletonList("false"));
            return new ResponseEntity<List<String>>(getLinks(file), httpHeaders, HttpStatus.OK);
        } else if (file.isFile()) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.put("File-Content", Collections.singletonList("true"));
            return new ResponseEntity<List<String>>(getFile(file), httpHeaders, HttpStatus.OK);
        } if(!file.exists()) {
            return new ResponseEntity<String>("The file or directory don't exist", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<String>("You want the impossible", HttpStatus.BAD_REQUEST);
        }
    }

    private List<String> getLinks(File file) {
        String[] files = file.list();
        return Arrays.asList(files);
    }

    private List<String> getFile(File file) {
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = null;
        try {
            FileReader fileReader = new FileReader(file);
            scanner = new Scanner(fileReader);
            while(scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            //TODO: ExceptionHandler
            e.printStackTrace();
        } finally {
             if(scanner != null) {
                scanner.close();
             }
        }
        return Collections.singletonList(stringBuilder.toString());
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity createFileOrDir(@RequestParam("name") String name,
                                          @RequestParam(value = "file", required = false) Boolean isFile,
                                          HttpServletRequest httpRequest) {
        String url = httpRequest.getRequestURI().replaceAll("%20", " ");
        File file = new File(url + "/" + name);
        if (file.exists()) {
            return new ResponseEntity<String>("The " + name + " already exists", HttpStatus.BAD_REQUEST);
        }
        if(isFile != null && isFile) {
            try {
                return file.createNewFile() ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } catch (IOException e) {
                return new ResponseEntity<>("Something went wrong after creating " + name, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return file.mkdirs() ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity deleteFileOrDir(HttpServletRequest httpRequest) {
        String url = httpRequest.getRequestURI().replaceAll("%20", " ");
        File file = new File(url);
        if(file.exists()) {
            return file.delete() ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<String>("The file or directory doesn't exist", HttpStatus.BAD_REQUEST);
        }
    }
}
