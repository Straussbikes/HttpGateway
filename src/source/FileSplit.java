package source;

import java.io.*;
import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

class FileSplit {
    public static void splitFile(File f) throws IOException {
        int partCounter = 1;//I like to name parts from 001, 002, 003, ...

        int sizeOfFiles = 60000;// 1MB

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


    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }



    public static void main(String[] args) throws IOException {
        splitFile(new File("C:\\Users\\StraussBikes\\Desktop\\3ano2sem\\CC\\HttpGateway\\src\\source\\test.mp4"));
        List<File> files = listOfFilesToMerge(new File ("C:\\Users\\StraussBikes\\Desktop\\3ano2sem\\CC\\HttpGateway\\src\\source\\test.mp4.001"));
        System.out.println(files);
     //   mergeFiles(files, new File("C:\\Users\\latot\\Desktop\\CC Teste\\output\\teste.mp4"));
    }
}