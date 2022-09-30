import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

/**
 *
 * @author Ghaith
 */
public class SeamCarver {

    private Picture picture;
    private double[][] energy;
    // create a seam carver object based on the given picture

    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("invalid picture!!");
        }
        this.picture = new Picture(picture);
        energy = new double[picture.width()][picture.height()];
        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                updateEnergy(x, y);
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(picture);

    }

    // width of current picture
    public int width() {

        return picture.width();

    }

    // height of current picture
    public int height() {

        return picture.height();

    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x > picture.width() - 1 || y > picture.height() - 1) {
            throw new IllegalArgumentException("pixel off borders!!");
        }
        return energy[x][y];

    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        int[][] edgeTo = new int[picture.width()][picture.height()];
        double[][] energyTo = new double[picture.width()][picture.height()];
        int minEY = 0;

        for (int x = 0; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                if (x == 0) {
                    energyTo[x][y] = energy[x][y];
                } else if (y > 0 && (energyTo[x - 1][y - 1] <= energyTo[x - 1][y] && (y == picture.height() - 1 || energyTo[x - 1][y - 1] <= energyTo[x - 1][y + 1]))) {
                    energyTo[x][y] = energy[x][y] + energyTo[x - 1][y - 1];
                    edgeTo[x][y] = y - 1;
                } else if ((y == 0 || energyTo[x - 1][y] <= energyTo[x - 1][y - 1]) && (y == picture.height() - 1 || energyTo[x - 1][y] <= energyTo[x - 1][y + 1])) {
                    energyTo[x][y] = energy[x][y] + energyTo[x - 1][y];
                    edgeTo[x][y] = y;
                } else if (y < picture.height() - 1 && (energyTo[x - 1][y + 1] <= energyTo[x - 1][y] && (y == 0 || energyTo[x - 1][y + 1] <= energyTo[x - 1][y - 1]))) {
                    energyTo[x][y] = energy[x][y] + energyTo[x - 1][y + 1];
                    edgeTo[x][y] = y + 1;
                }
                if (x == picture.width() - 1 && energyTo[x][y] < energyTo[x][minEY]) {
                    minEY = y;
                }
            }
        }
        int[] seam = new int[picture.width()];
        int py = minEY;
        for (int x = width() - 1; x >= 0; x--) {
            seam[x] = py;
            py = edgeTo[x][py];
        }
        return seam;

    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[][] edgeTo = new int[picture.width()][picture.height()];
        double[][] energyTo = new double[picture.width()][picture.height()];
        int minEX = 0;

        for (int y = 0; y < picture.height(); y++) {
            for (int x = 0; x < picture.width(); x++) {
                if (y == 0) {
                    energyTo[x][y] = energy[x][y];
                } else if (x > 0 && (energyTo[x - 1][y - 1] <= energyTo[x][y - 1] && (x == picture.width() - 1 || energyTo[x - 1][y - 1] <= energyTo[x + 1][y - 1]))) {
                    energyTo[x][y] = energy[x][y] + energyTo[x - 1][y - 1];
                    edgeTo[x][y] = x - 1;
                } else if ((x == 0 || energyTo[x][y - 1] <= energyTo[x - 1][y - 1]) && (x == picture.width() - 1 || energyTo[x][y - 1] <= energyTo[x + 1][y - 1])) {
                    energyTo[x][y] = energy[x][y] + energyTo[x][y - 1];
                    edgeTo[x][y] = x;
                } else if (x < picture.width() - 1 && (energyTo[x + 1][y - 1] <= energyTo[x][y - 1] && (x == 0 || energyTo[x + 1][y - 1] <= energyTo[x - 1][y - 1]))) {
                    energyTo[x][y] = energy[x][y] + energyTo[x + 1][y - 1];
                    edgeTo[x][y] = x + 1;
                }
                if (y == picture.height() - 1 && energyTo[x][y] < energyTo[minEX][y]) {
                    minEX = x;
                }
            }
        }
        int[] seam = new int[picture.height()];
        int px = minEX;
        for (int y = height() - 1; y >= 0; y--) {
            seam[y] = px;
            px = edgeTo[px][y];
        }
        return seam;

    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

        try {
            if (picture.height() < 2) {
                throw new IllegalArgumentException("out of seems");
            }
            if (seam.length != picture.width()) {
                throw new IllegalArgumentException("seam off borders");
            }
            Picture temP = new Picture(picture.width(), picture.height() - 1);
//            double[][] temE = new double[picture.width()][picture.height() - 1];
            for (int x = 0; x < picture.width(); x++) {
                for (int y = 0; y < picture.height() - 1; y++) {
                    if (seam[x] < 0 || seam[x] >= picture.height() || (x != 0 && (Math.abs(seam[x] - seam[x - 1])) > 1)) {
                        throw new IllegalArgumentException("seam disconnected!!");
                    }
                    if (y < seam[x]) {
                        temP.set(x, y, picture.get(x, y));
                    } else {
                        temP.set(x, y, picture.get(x, y + 1));
                    }
//                    if (y < seam[x] - 1) {
//                        temE[x][y] = energy[x][y];
////                    } else if (y > seam[x]) {
////                        temE[x][y] = energy[x][y + 1];
//                    } else {
//                        updateEnergy(x, y, temP, temE);
//                    }

                }
            }
            picture = temP;
//            energy = temE;
            for (int x = 0; x < picture.width(); x++) {
                for (int y = seam[x] - 1; y < seam[x]+1; y++) {

                    updateEnergy(x, y);
                }
                for(int y = seam[x]+1;y<picture.height();y++){
                    energy[x][y] = energy[x][y+1];
                }
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("null seam", e);
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

        try {
            if (picture.width() < 2) {
                throw new IllegalArgumentException("out of seems");
            }
            if (seam.length != picture.height()) {
                throw new IllegalArgumentException("seam off borders");
            }
            Picture temP = new Picture(picture.width() - 1, picture.height());
//            double[][] temE = new double[picture.width() - 1][picture.height()];
            for (int y = 0; y < picture.height(); y++) {
                for (int x = 0; x < picture.width() - 1; x++) {
                    if (seam[y] < 0 || seam[y] >= picture.width() || (y != 0 && (Math.abs(seam[y] - seam[y - 1])) > 1)) {
                        throw new IllegalArgumentException("seam disconnected!!");
                    }
                    if (x < seam[y]) {
                        temP.set(x, y, picture.get(x, y));
                    } else {
                        temP.set(x, y, picture.get(x + 1, y));
                    }
//                    if (x < seam[y] - 1) {
//                        temE[x][y] = energy[x][y];
////                    } else if (x > seam[y]) {
////                        temE[x][y] = energy[x + 1][y];
//                    } else {
//                        updateEnergy(x, y, temP, temE);
//                    }
                }
            }
            picture = temP;
//            energy = temE;
            for (int y = 0; y < picture.height(); y++) {
                for (int x = seam[y] - 1; x < seam[y] + 1; x++) {
                    updateEnergy(x, y);
                }
                for (int x = seam[y] + 1; x < picture.width(); x++) {
                    energy[x][y] = energy[x + 1][y];
                }
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("null seam", e);
        }
    }

    //  unit testing (optional)
    public static void main(String[] args) {
        Picture picture = new Picture(6, 6);

        int[][] pic = {
            {0x010500, 0x000006, 0x060903, 0x010005, 0x060909, 0x060400},
            {0x050205, 0x090405, 0x010406, 0x030501, 0x070104, 0x070805},
            {0x060105, 0x040209, 0x030102, 0x050308, 0x050702, 0x040908},
            {0x030903, 0x020907, 0x000604, 0x030204, 0x030808, 0x050104},
            {0x080401, 0x030606, 0x010008, 0x060902, 0x000805, 0x000508},
            {0x010707, 0x080502, 0x010004, 0x090401, 0x020502, 0x010805}
        };

        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                picture.set(x, y, new Color(pic[y][x]));

            }
        }
        SeamCarver carver = new SeamCarver(picture);
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 6; y++) {
                System.out.print(carver.energy(x, y) + ",\t\t\t");

            }
            System.out.print("\n");
        }
        System.out.println("............................................\n\n\n\n");
        int[] seam = {2, 3, 2, 2, 3, 2};
        carver.removeHorizontalSeam(seam);
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 5; y++) {
                System.out.print(carver.energy(x, y) + ",\t\t\t");

            }
            System.out.print("\n");
        }
        System.out.println("............................................\n\n\n\n");

        int[][] pic1 = {
            {0x010500, 0x000006, 0x060903, 0x010005, 0x060909, 0x060400},
            {0x050205, 0x090405, 0x010406, 0x030501, 0x070104, 0x070805},
            {0x030903, 0x040209, 0x000604, 0x030204, 0x050702, 0x050104},
            {0x080401, 0x030606, 0x010008, 0x060902, 0x000805, 0x000508},
            {0x010707, 0x080502, 0x010004, 0x090401, 0x020502, 0x010805}
        };
        picture = new Picture(6, 5);
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 5; y++) {
                picture.set(x, y, new Color(pic1[y][x]));

            }
        }
        carver = new SeamCarver(picture);
        for (int x = 0; x < 6; x++) {
            for (int y = 0; y < 5; y++) {
                System.out.print(carver.energy(x, y) + ",\t\t\t");

            }
            System.out.print("\n");
        }
        System.out.println("............................................\n\n\n\n");
    }

    private void updateEnergy(int x, int y) {
        try{
        if (y == 0 || x == 0 || y == picture.height() - 1 || x == picture.width() - 1) {
            energy[x][y] = 1000;
            return;
        }
        double VE2 = Math.pow(picture.get(x - 1, y).getBlue() - picture.get(x + 1, y).getBlue(), 2)
                + Math.pow(picture.get(x - 1, y).getRed() - picture.get(x + 1, y).getRed(), 2)
                + Math.pow(picture.get(x - 1, y).getGreen() - picture.get(x + 1, y).getGreen(), 2);
        double HE2 = Math.pow(picture.get(x, y - 1).getBlue() - picture.get(x, y + 1).getBlue(), 2)
                + Math.pow(picture.get(x, y - 1).getRed() - picture.get(x, y + 1).getRed(), 2)
                + Math.pow(picture.get(x, y - 1).getGreen() - picture.get(x, y + 1).getGreen(), 2);
        energy[x][y] = Math.sqrt(HE2 + VE2);
        }catch(IllegalArgumentException | ArrayIndexOutOfBoundsException e){
            
        }
    }

}
