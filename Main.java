import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    static int NumberOfFeatures = 6;

    // [0] = Action, [1] = Fantasy, [2] = Romance, [3] = Sci-Fi, [4] = Adventure, [5] = Horror,
    // [6] = Comedy, [7] = Thriller
    static double[] odds = new double[8];

    static double[] toFeatureVector(double id, String genre, double runtime, double year, double imdb, double rt, double budget, double boxOffice) {

        double[] feature = new double[NumberOfFeatures];

        // remove the id as a feature

        switch (genre) { // We also use represent each movie genre as an integer number:
            case "Action":  feature[0] = odds[0]; break;
            case "Fantasy":   feature[0] = odds[1]; break;
            case "Romance": feature[0] = odds[2]; break;
            case "Sci-Fi": feature[0] = odds[3]; break;
            case "Adventure": feature[0] = odds[4]; break;
            case "Horror": feature[0] = odds[5]; break;
            case "Comedy": feature[0] = odds[6]; break;
            case "Thriller": feature[0] = odds[7]; break;
        }

        feature[1] = runtime;
        feature[2] = imdb;
        feature[3] = rt;
        feature[4] = budget;
        feature[5] = boxOffice;
//        feature[6] = year;

        return feature;
    }

    static void loadOdds (String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header line
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String genre = values[2];
                int liked = Integer.parseInt(values[11]);
                if (liked==1) {
                    switch(genre) {
                        case "Action":  odds[0] += 1; break;
                        case "Fantasy":   odds[1] += 1; break;
                        case "Romance": odds[2] += 1; break;
                        case "Sci-Fi": odds[3] += 1; break;
                        case "Adventure": odds[4] += 1; break;
                        case "Horror": odds[5] += 1; break;
                        case "Comedy": odds[6] += 1; break;
                        case "Thriller": odds[7] += 1; break;
                    }
                }
            }
            for (int i =0; i<odds.length; i++) {
                odds[i] /= 100;
            }
        }
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
            loadOdds("training-set.csv");
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
