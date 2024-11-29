import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    // [0] = Action, [1] = Fantasy, [2] = Romance, [3] = Sci-Fi, [4] = Adventure, [5] = Horror,
    // [6] = Comedy, [7] = Thriller
    static double[] oddsGenre = new double[8];

    // each oddsFeature[] stores the odds that a value occurring in Q1, Q2, Q3 or Q4 will result in a liked movie
    // not liked runtime stores the number of
    static double[] oddsRunTime = new double[4];
    static double[] runtimeQuartiles = new double[3];

    static double[] oddsRottenTomatoes = new double[4];
    static double[] rottenTomatoesQuartiles = new double[3];

    static double[] oddsImdb = new double[4];
    static double[] imdbQuartiles = new double[3];

    static double[] oddsBudget = new double[4];
    static double[] budgetQuartiles = new double[3];

    static double[] oddsBoxOffice = new double[4];
    static double[] boxOfficeQuartiles = new double[3];

    static int NumberOfFeatures = 6;

    static double[] toFeatureVector(double id, String genre, double runtime, double year, double imdb, double rt, double budget, double boxOffice) {

        double[] feature = new double[NumberOfFeatures];

        switch (genre) {
            case "Action":
                feature[0] = oddsGenre[0]; break;
            case "Fantasy":
                feature[0] = oddsGenre[1]; break;
            case "Romance":
                feature[0] = oddsGenre[2]; break;
            case "Sci-Fi":
                feature[0] = oddsGenre[3]; break;
            case "Adventure":
                feature[0] = oddsGenre[4]; break;
            case "Horror":
                feature[0] = oddsGenre[5]; break;
            case "Comedy":
                feature[0] = oddsGenre[6]; break;
            case "Thriller":
                feature[0] = oddsGenre[7]; break;
        }


        // finds out which of the 4 bins the current runtime belongs to
        // puts the odds of a 1 label into feature[1]
        if (runtime > runtimeQuartiles[2]) {
            feature[1] = oddsRunTime[3];
        } else if (runtime > runtimeQuartiles[1]) {
            feature[1] = oddsRunTime[2];
        } else if (runtime > runtimeQuartiles[0]) {
            feature[1] = oddsRunTime[1];
        } else {
            feature[1] = oddsRunTime[0];
        }

        if (imdb > imdbQuartiles[2]) {
            feature[2] = oddsImdb[3];
        } else if (imdb > imdbQuartiles[1]) {
            feature[2] = oddsImdb[2];
        } else if (imdb > imdbQuartiles[0]) {
            feature[2] = oddsImdb[1];
        } else {
            feature[2] = oddsImdb[0];
        }

        if (rt > rottenTomatoesQuartiles[2]) {
            feature[3] = oddsRottenTomatoes[3];
        } else if (rt > rottenTomatoesQuartiles[1]) {
            feature[3] = oddsRottenTomatoes[2];
        } else if (rt > rottenTomatoesQuartiles[0]) {
            feature[3] = oddsRottenTomatoes[1];
        } else {
            feature[3] = oddsRottenTomatoes[0];
        }

        if (budget > budgetQuartiles[2]) {
            feature[4] = oddsBudget[3];
        } else if (budget >budgetQuartiles[1]) {
            feature[4] = oddsBudget[2];
        } else if (budget >budgetQuartiles[0]) {
            feature[4] = oddsBudget[1];
        } else {
            feature[4] = oddsBudget[0];
        }

        if (boxOffice > boxOfficeQuartiles[2]) {
            feature[5] = oddsBoxOffice[3];
        } else if (budget > boxOfficeQuartiles[1]) {
            feature[5] = oddsBoxOffice[2];
        } else if (budget > boxOfficeQuartiles[0]) {
            feature[5] = oddsBoxOffice[1];
        } else {
            feature[5] = oddsBoxOffice[0];
        }

        return feature;
    }

    /*
        Sorts array in ascending order
     */
    static void arraySorter(double[] d) {
        int n = d.length;
        for (int i = 0; i < n - 1; i++) {

            int min_idx = i;

            for (int j = i + 1; j < n; j++) {
                if (d[j] < d[min_idx]) {
                    min_idx = j;
                }
            }
            double temp = d[i];
            d[i] = d[min_idx];
            d[min_idx] = temp;
        }
    }

    /*
        Finds the odds of the user liking a genre and assigns that value to the array odds
     */
    static void loadOddsGenre (String filePath) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header line
            int total[] = new int[8]; // stores the total number of each genre
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String genre = values[2];
                int liked = Integer.parseInt(values[11]);
                loadOddsGenreUtility(genre, total, liked);
            }

            // for each bin, finds the probability of liked and divides it by the probability of not liked to get the odds
            for (int i =0; i<oddsGenre.length; i++) {
                double probOfNotLiked = ( total[i]-oddsGenre[i] ) / total[i];
                if (probOfNotLiked==0) {
                    probOfNotLiked= 0.01;
                }
                if (oddsGenre[i] ==0) {
                    oddsGenre[i] = 0.01;
                    continue;
                }
                oddsGenre[i] /= probOfNotLiked;
            }
        }
    }

    static void loadOddsGenreUtility(String genre, int[] total, int liked) {
        int n = 0;
        switch (genre) {
            case "Action":
                n = 0; break;
            case "Fantasy":
                n = 1; break;
            case "Romance":
                n = 2; break;
            case "Sci-fi":
                n = 3; break;
            case "Adventure":
                n = 4; break;
            case "Horror":
                n = 5; break;
            case "Comedy":
                n = 6; break;
            case "Thriller":
                n = 7; break;
        }

        if (liked==1) {
            oddsGenre[n]++;
        }
        total[n]++;
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

    /*
        Loads the odds into the odds arrays
     */
    static void loadOdds() throws IOException {

        String filePath = "training-set.csv";

        double[] runTimeArray = new double[100];
        double[] rottenTomatoesArray = new double[100];
        double[] imdbArray = new double[100];
        double[] budgetArray = new double[100];
        double[] boxOfficeArray = new double[100];

        int[] labels = new int[100];

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            br.readLine(); // skip header line

            int index = 0;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                double runtime = Double.parseDouble(values[10]);
                runTimeArray[index] = runtime;

                double rottenTomatoes = Double.parseDouble(values[6]);
                rottenTomatoesArray[index] = rottenTomatoes;

                double imdb = Double.parseDouble(values[7]);
                imdbArray[index] = imdb;

                double budget = Double.parseDouble(values[9]);
                budgetArray[index] = budget;

                double boxOffice = Double.parseDouble(values[8]);
                boxOfficeArray[index] = boxOffice;

                labels[index] = Integer.parseInt(values[11]);
                index++;
            }
        }

        oddsUtility("Runtime", runTimeArray, labels);
        oddsUtility("IMDB", imdbArray, labels);
        oddsUtility("Rotten Tomatoes", rottenTomatoesArray, labels);
        oddsUtility("Budget", budgetArray, labels);
        oddsUtility("Box Office", boxOfficeArray, labels);


    }

    /*
        Breaks each feature up into 4 bins (quartiles)
        finds the odds that the values in each quartile will result in a liked movie
        sets the odds arrays accordingly
     */
    static void oddsUtility(String typeOfValue, double[] unsorted, int[] labels) {

        double[] oddsLiked = new double[10];
        double[] notLiked = new double[4];
        double[] quartiles = new double[10];
        //separate array for sorted because the labels correspond to the original array
        double[] sorted = new double[100];

        for (int i =0; i<100; i++) {
            sorted[i] = unsorted[i];
        }

        // sorts the array to find the median
        arraySorter(sorted);

        int[] totalPerQuartile = new int[4];
        switch(typeOfValue){
            case "Runtime":
                oddsLiked = oddsRunTime;
                quartiles = runtimeQuartiles;
                break;
            case "IMDB":
                oddsLiked = oddsImdb;
                quartiles = imdbQuartiles;
                break;
            case "Rotten Tomatoes":
                oddsLiked = oddsRottenTomatoes;
                quartiles = rottenTomatoesQuartiles;
                break;
            case "Budget":
                oddsLiked = oddsBudget;
                quartiles = budgetQuartiles;
                break;
            case "Box Office":
                oddsLiked = oddsBoxOffice;
                quartiles = boxOfficeQuartiles;
                break;
        }

        // Q2
        double q2 = (sorted[50] + sorted[49]) / 2;
        quartiles[1] = q2;
        // Q1
        double q1 = (sorted[25] + sorted[24]) / 2;
        quartiles[0] = q1;
        // Q3
        double q3 = (sorted[75] + sorted[74]) / 2;
        quartiles[2] = q3;

        // finds the total number of liked in each bin for the feature
        for (int i = 0; i < 100; i++) {
            if (unsorted[i] >= quartiles[2]) {          // Q4
                if (labels[i] ==1) {
                    oddsLiked[3]++;
                }
                totalPerQuartile[3]++;
            } else if (unsorted[i] >= quartiles[1]) {   // Q3
                if (labels[i] ==1) {
                    oddsLiked[2]++;
                }
                totalPerQuartile[2]++;
            } else if (unsorted[i] >= quartiles[0]) {   // Q2
                if (labels[i]==1) {
                    oddsLiked[1]++;
                }
                totalPerQuartile[1]++;
            } else {                                    // Q1
                if (labels[i] ==1) {
                    oddsLiked[0]++;
                }
                totalPerQuartile[0]++;
            }
        }

        // finds the probabilities of liked and not liked for each quartile, then finds the odds
        for (int i =0; i<4; i++) {
            notLiked[i] = ( totalPerQuartile[i] - oddsLiked[i] ) / totalPerQuartile[i];
            oddsLiked[i] /= totalPerQuartile[i];
            if (notLiked[i]==0) {
                notLiked[i] = 0.01;
            }
            if (oddsLiked[i]==0) {
                oddsLiked[i] = 0.01;
            }
            oddsLiked[i]/=notLiked[i];
        }
    }

    public static void main(String[] args) throws IOException {
        double[][] trainingData = new double[100][];
        int[] trainingLabels = new int[100];
        double[][] testingData = new double[100][];
        int[] testingLabels = new int[100];

        try {
            loadOddsGenre("training-set.csv");
            loadOdds();
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
