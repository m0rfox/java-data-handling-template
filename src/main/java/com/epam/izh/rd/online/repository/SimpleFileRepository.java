package com.epam.izh.rd.online.repository;

import java.io.*;

import java.net.URL;
import java.nio.file.Files;

import java.nio.file.Paths;


public class SimpleFileRepository implements FileRepository {

    /**
     * Метод рекурсивно подсчитывает количество файлов в директории
     *
     * @param path путь до директори
     * @return файлов, в том числе скрытых
     */
    @Override
    public long countFilesInDirectory(String path) {
        long count = 0;


        File[] files;
        File file = new File(path);
        files = file.listFiles();
        if (files == null) return count;
        for (File i : files) {
            if (i.isFile()) {
                count++;
            } else if (i.isDirectory()) {
                count = count + countFilesInDirectory(i.getAbsolutePath());
            }
        }
        return count;
    }


    /**
     * Метод рекурсивно подсчитывает количество папок в директории, считая корень
     *
     * @param path путь до директории
     * @return число папок
     */
    @Override
    public long countDirsInDirectory(String path) {

        long count = 0;
        File[] files;
        File file = new File(path);
        files = file.listFiles();
        if (files == null) return count;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                count++;
                count += countDirsInDirectory(files[i].getAbsolutePath());

                if (i == files.length - 1){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Метод копирует все файлы с расширением .txt
     *
     * @param from путь откуда
     * @param to   путь куда
     */
    @Override
    public void copyTXTFiles(String from, String to) {
        if (Files.exists(Paths.get(to))) {
            System.out.println("Директория уже существует");
        }
        File file = new File(from);
        FileFilter filter = new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith("txt");
            }
        };
        File[] files = file.listFiles(filter);
        assert files != null;
        for (File value : files) {
            try {
                Files.copy(Paths.get(from + "/" + value.getName()), Paths.get(to + "/" + value.getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод создает файл на диске с расширением txt
     *
     * @param path путь до нового файла
     * @param name имя файла
     * @return был ли создан файл
     */
    @Override
    public boolean createFile(String path, String name) {
        path = "target/classes/" + path;
        if (Files.exists(Paths.get(path))){
            System.out.println("Директория уже существует");
            try {
                Files.createFile(Paths.get(path + "/" + name));
                if (Files.exists(Paths.get(path + "/" + name))){
                    System.out.println("Файл создан");
                }
            } catch (IOException e) {
                System.out.println("Файл уже создан");
            }return Files.exists(Paths.get(path + "/" + name));
        }else {
            try {
                Files.createDirectory(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Files.createFile(Paths.get(path + "/" + name));
            } catch (IOException e) {
                System.out.println("Файл уже создан");;
            }
        }
        if (Files.exists(Paths.get(path  + "/" + name))){
            System.out.println("Файл создан");
        }
        return Files.exists(Paths.get(path  + "/" + name));
    }

    /**
     * Метод считывает тело файла .txt из папки src/main/resources
     *
     * @param fileName имя файла
     * @return контент
     */
    @Override
    public String readFileFromResources(String fileName) {
        String path = "src/main/resources/";
        String text = null;
        try (BufferedReader br = new BufferedReader(new FileReader(path + fileName))) {
            text = br.readLine();
        } catch (IOException e) {
            System.out.println("Файл не найден");
        }
        return text;
    }
}
