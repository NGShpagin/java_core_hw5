import java.io.File;

public class Tree {

    public static void print(File file, String indent, boolean isLast) {
        System.out.print(indent); // рисуем отступ
        if (isLast) {
            System.out.print("└─");
            indent += "  ";
        } else {
            System.out.print("├─");
            indent += "│ ";
        }
        System.out.println(file.getName());

        File[] files = file.listFiles();
        if (files == null) return;

        int subTotalFiles = 0;
        int subCounter = 0;
        for (File value : files)
            subTotalFiles++;

        for (File value : files) {
            if (value.isDirectory())
                print(value, indent, subCounter == subTotalFiles - 1);
            else
                print(value, indent, subCounter == subTotalFiles - 1);
            subCounter++;
        }
    }

}
