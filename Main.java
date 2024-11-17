import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    static int NumberOfFeatures = 5;

    static double[] toFeatureVector(double id, String genre, double runtime, double year, double imdb, double rt, double budget, double boxOffice) {

        double[] feature = new double[NumberOfFeatures];

        // remove the id as a feature

//        switch (genre) { // We also use represent each movie genre as an integer number:
//
//            case "Action":  feature[6] = 1; break;
//            case "Fantasy":   feature[7] = 1; break;
//            case "Romance": feature[8] = 1; break;
//            case "Sci-Fi": feature[9] = 1; break;
//            case "Adventure": feature[10] = 1; break;
//            case "Horror": feature[11] = 1; break;
//            case "Comedy": feature[12] = 1; break;
//            case "Thriller": feature[13] = 1; break;
//            default: Assert(false);
//        }
        feature[0] = runtime;
        feature[1] = imdb;
        feature[2] = rt;
        feature[3] = budget;
        feature[4] = boxOffice;
        //       feature[5] = year;

        return feature;
    }

    static void loadData(String filePath, double[][] dataFeatures, int[] dataLabels) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int idx = 0;
            br.readLine(); // skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Assuming csv format: MovieID,Title,Genre,Runtime,Year,Lead Actor,Director,IMDB,RT(%),Budget,Box Office Revenue (in million $),Like it
                double id = Double.parseDouble(values[0]);
                String genre = values[2];
                double runtime = Double.parseDouble(values[10]);
                double year = Double.parseDouble(values[3]);
                double imdb = Double.parseDouble(values[7]);
                double rt = Double.parseDouble(values[6]);
                double budget = Double.parseDouble(values[9]);
                double boxOffice = Double.parseDouble(values[8]);

                dataFeatures[idx] = toFeatureVector(id, genre, runtime, year, imdb, rt, budget, boxOffice);
                dataLabels[idx] = Integer.parseInt(values[11]); // Assuming the label is the last column and is numeric
                idx++;
            }
        }
    }

    public static void main(String[] args) {
        double[][] trainingData = new double[100][];
        int[] trainingLabels = new int[100];
        double[][] testingData = new double[100][];
        int[] testingLabels = new int[100];

        try {
            // You may need to change the path:
            loadData("training-set.csv", trainingData, trainingLabels);
            loadData("testing-set.csv", testingData, testingLabels);
        }
        catch (IOException e) {
            System.out.println("Error reading data files: " + e.getMessage());
            return;
        }

        Manhattan m = new Manhattan(trainingData, trainingLabels, testingData, testingLabels);
        Euclidean e = new Euclidean(trainingData, trainingLabels, testingData, testingLabels);
        Similarity s = new Similarity(trainingData,trainingLabels,testingData,testingLabels);

    }
}
