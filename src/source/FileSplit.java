package source;

import java.io.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

class FileSplit {
    public static void splitFile(File f) throws IOException {
        int partCounter = 1;//I like to name parts from 001, 002, 003, ...

        int sizeOfFiles = 1024 * 1024;// 1MB

        byte[] buffer = new byte[sizeOfFiles];

        String fileName = f.getName();

        //try-with-resources to ensure closing stream
        try (FileInputStream fis = new FileInputStream(f);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            int bytesAmount = 0;
            while ((bytesAmount = bis.read(buffer)) > 0) {

                //write each chunk of data into separate file with different number in name
                String filePartName = String.format("%s.%03d", fileName, partCounter++);

                File newFile = new File(f.getParent(), filePartName);

                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                }
            }
        }
    }

    public static void mergeFiles(List<File> files, File into) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(into);
             BufferedOutputStream mergingStream = new BufferedOutputStream(fos)) {
                    for (File f : files) {
                        Files.copy(f.toPath(), mergingStream);
                    }
            }
    }

    public static List<File> listOfFilesToMerge(File oneOfFiles) {
        String tmpName = oneOfFiles.getName();//{name}.{number}

        String destFileName = tmpName.substring(0, tmpName.lastIndexOf('.'));//remove .{number}

        File[] files = oneOfFiles.getParentFile().listFiles(
                (File dir, String name) -> name.matches(destFileName + "[.]\\d+"));

        Arrays.sort(files);//ensuring order 001, 002, ..., 010, ...

        return Arrays.asList(files);
    }

    public static void main(String[] args) throws IOException {
        splitFile(new File("C:\\Users\\latot\\Desktop\\CC Teste\\teste.mp4"));
        List<File> files = listOfFilesToMerge(new File ("C:\\Users\\latot\\Desktop\\CC Teste\\teste.mp4.001"));
        mergeFiles(files, new File("C:\\Users\\latot\\Desktop\\CC Teste\\output\\teste.mp4"));
    }
}