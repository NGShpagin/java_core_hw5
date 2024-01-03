import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    /**
     * 1. Создать 2 текстовых файла, примерно по 50-100 символов в каждом (особого значения не имеет);
     * 2. Написать метод, "склеивающий" эти файлы, то есть вначале идет текст из первого файла, потом текст из второго;
     * 3.* Написать метод, который проверяет, присутствует ли указанное пользователем слово в файле (работаем только с латиницей)
     * 4.* Написать метод, проверяющий, есть ли указанное слово в папке
     */

    private static final Random random = new Random();
    private static final int CHAR_BOUND_LOW = 65; // Номер начального символа
    private static final int CHAR_BOUND_HIGH = 90; // Номер конечного символа
    private static final String TO_SEARCH = "GeekBrains"; // слово для поиска

    /**
     * Метод генерации некоторой последовательности символов
     *
     * @param amount кол-во символов
     * @return последовательность символов
     */
    private static String generateSymbols(int amount) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < amount; i++)
            stringBuilder.append((char) random.nextInt(CHAR_BOUND_LOW, CHAR_BOUND_HIGH + 1));
        return stringBuilder.toString();
    }

    /**
     * Записать последовательность символов в файл
     *
     * @param fileName имя файла
     * @param length   длина последовательности символов
     * @throws IOException
     */
    private static void writeFileContents(String fileName, int length) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            fileOutputStream.write(generateSymbols(length).getBytes());
            fileOutputStream.write('\n');
        }
    }

    /**
     * Записать последовательность символов в файл, при этом образом, случайным образом дописать осознанное слово для поиска
     *
     * @param fileName имя файла
     * @param length   длина последовательности символов
     * @param words    кол-во слов для поиска
     * @throws IOException
     */
    private static void writeFileContents(String fileName, int length, int words) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
            for (int i = 0; i < words; i++) {
                if (random.nextInt(5) == 5 / 2) {
                    fileOutputStream.write(TO_SEARCH.getBytes());
                } else {
                    fileOutputStream.write(generateSymbols(length).getBytes());
                }
            }
            fileOutputStream.write(' ');
        }
    }

    /**
     * @param fileIn1
     * @param fileIn2
     * @param fileOut
     * @throws IOException
     */
    private static void concatenate(String fileIn1, String fileIn2, String fileOut) throws IOException {
        // На запись
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileOut)) {

            int count;

            // На чтение
            try (FileInputStream fileInputStream = new FileInputStream(fileIn1)) {
                while ((count = fileInputStream.read()) != -1) {
                    fileOutputStream.write(count);
                }
            }

            // На чтение
            try (FileInputStream fileInputStream = new FileInputStream(fileIn2)) {
                while ((count = fileInputStream.read()) != -1) {
                    fileOutputStream.write(count);
                }
            }
        }
    }

    /**
     * Определить, содержится ли в файле искомое слово
     *
     * @param fileName имя файла
     * @param search   строка для поиска
     * @return результат поиска
     */
    private static boolean searchInFile(String fileName, String search) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            byte[] searchData = search.getBytes();
            int count;
            int i = 0;
            while ((count = fileInputStream.read()) != -1) {
                if (count == searchData[i]) {
                    i++;
                } else {
                    i = 0;
                    if (count == searchData[i]) {
                        i++;
                    }
                }
                if (i == searchData.length) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Поиск строки в переданном списке файлов
     *
     * @param files  список файлов, в которых необходимо произвести поиск
     * @param search искомая строка
     * @return список папок, в которых найдены совпадения
     * @throws IOException
     */
    private static List<String> searchMatch(String[] files, String search) throws IOException {
        List<String> list = new ArrayList<>();
        File path = new File(new File(".").getCanonicalPath());
        File[] dir = path.listFiles();
        for (int i = 0; i < dir.length; i++) {
            if (dir[i].isDirectory()) continue;
            for (int j = 0; j < files.length; j++) {
                if (dir[i].getName().equals(files[j])) {
                    if (searchInFile(dir[i].getName(), search)) {
                        list.add(dir[i].getName());
                        break;
                    }
                }
            }
        }
        return list;
    }

    /**
     * Резервное копирование файлов
     * @param file директория для резервного копирования вложенных файлов
     * @throws IOException
     */
    private static void backupFiles(File file) throws IOException {
        File backupDir = new File(file + "/backup");
        if (backupDir.mkdir()) System.out.println(backupDir.getName() + " created");

        InputStream is = null;
        OutputStream os = null;

        File[] files = file.listFiles();
        if (files != null) {
            for (File value : files) {
                if (value.isFile()) {
                    // на запись
                    try (OutputStream outputStream = new FileOutputStream(backupDir + "/" + value)) {
                        int count;
                        // на чтение
                        try (InputStream inputStream = new FileInputStream(value.getName())) {
                            while ((count = inputStream.read()) != -1) {
                                outputStream.write(count);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        writeFileContents("sample01.txt", 30);
        System.out.println(searchInFile("sample01.txt", TO_SEARCH));

        writeFileContents("sample02.txt", 30, 2);
        System.out.println(searchInFile("sample02.txt", TO_SEARCH));

        concatenate("sample01.txt", "sample02.txt", "sample01_out.txt");
        System.out.println(searchInFile("sample01_out.txt", TO_SEARCH));

        Tree.print(new File("."), "", true);

        String[] fileNames = new String[10];
        for (int i = 0; i < fileNames.length; i++) {
            fileNames[i] = "file_" + i + ".txt";
            writeFileContents(fileNames[i], 100, 4);
            System.out.printf("Файл %s создан\n", fileNames[i]);
        }

        List<String> result = searchMatch(fileNames, TO_SEARCH);
        for (String s : result) {
            System.out.printf("Файл %s содержит искомое слово %s\n", s, TO_SEARCH);
        }

        backupFiles(new File("."));

    }
}