import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

//  LOOK INTO ADDING PROFIT AS FEATURE

    static int NumberOfFeatures = 7;

    // [0] = Action, [1] = Fantasy, [2] = Romance, [3] = Sci-Fi, [4] = Adventure, [5] = Horror,
    // [6] = Comedy, [7] = Thriller
    static double[] odds = new double[8];
    static double[] oddsRunTime = new double[4];
    static double[] runtimeQuartiles = new double[3];
    static double[] oddsRottenTomatoes = new double[4];
    static double[] rottenTomatoesQuartiles = new double[3];
    static double[] oddsYear = new double[4];
    static double[] yearQuartiles = new double[3];
    static double[] oddsImdb = new double[4];
    static double[] imdbQuartiles = new double[3];
    static double[] oddsBudget = new double[4];
    static double[] budgetQuartiles = new double[3];
    static double[] oddsBoxOffice = new double[4];
    static double[] boxOfficeQuartiles = new double[3];

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


        // finds out which of the 4 bins the current runtime belongs to
        // puts the odds of a 1 label into feature[1]
        if (runtime > runtimeQuartiles[2]) {
            feature[1] = oddsRunTime[3];
        } else if (runtime >runtimeQuartiles[1]) {
            feature[1] = oddsRunTime[2];
        } else if (runtime >runtimeQuartiles[0]) {
            feature[1] = oddsRunTime[1];
        } else {
            feature[1] = oddsRunTime[0];
        }

//        if (year > yearQuartiles[2]) {
//            feature[2] = oddsYear[3];
//        } else if (year >yearQuartiles[1]) {
//            feature[2] = oddsYear[2];
//        } else if (year >yearQuartiles[0]) {
//            feature[2] = oddsYear[1];
//        } else {
//            feature[2] = oddsYear[0];
//        }

        if (imdb > imdbQuartiles[2]) {
            feature[3] = oddsImdb[3];
        } else if (imdb >imdbQuartiles[1]) {
            feature[3] = oddsImdb[2];
        } else if (imdb >imdbQuartiles[0]) {
            feature[3] = oddsImdb[1];
        } else {
            feature[3] = oddsImdb[0];
        }

        if (rt > rottenTomatoesQuartiles[2]) {
            feature[4] = oddsRottenTomatoes[3];
        } else if (rt >rottenTomatoesQuartiles[1]) {
            feature[4] = oddsRottenTomatoes[2];
        } else if (rt >rottenTomatoesQuartiles[0]) {
            feature[4] = oddsRottenTomatoes[1];
        } else {
            feature[4] = oddsRottenTomatoes[0];
        }
//
//        if (budget > budgetQuartiles[2]) {
//            feature[5] = oddsBudget[3];
//        } else if (budget >budgetQuartiles[1]) {
//            feature[5] = oddsBudget[2];
//        } else if (budget >budgetQuartiles[0]) {
//            feature[5] = oddsBudget[1];
//        } else {
//            feature[5] = oddsBudget[0];
//        }

        if (boxOffice > boxOfficeQuartiles[2]) {
            feature[6] = oddsBoxOffice[3];
        } else if (budget >boxOfficeQuartiles[1]) {
            feature[6] = oddsBoxOffice[2];
        } else if (budget >boxOfficeQuartiles[0]) {
            feature[6] = oddsBoxOffice[1];
        } else {
            feature[6] = oddsBoxOffice[0];
        }

        return feature;
    }



    /*
        finds the odds of the runtime being a 1, depending on the iqr
     */
    static void loadOddsRuntime(String filePath) throws IOException {


        double[] runTimeArraySorted = new double[100];
        double[] rottenTomatoesSorted = new double[100];
        double[] yearSorted = new double[100];
        double[] imdbSorted = new double[100];
        double[] budgetSorted = new double[100];
        double[] boxOfficeSorted = new double[100];

        double[] runTimeArray = new double[100];
        double[] rottenTomatoesArray = new double[100];
        double[] yearArray = new double[100];
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
                runTimeArraySorted[index] = runtime;
                runTimeArray[index] = runtime;

                double rottenTomatoes = Double.parseDouble(values[6]);
                rottenTomatoesSorted[index] = rottenTomatoes;
                rottenTomatoesArray[index] = rottenTomatoes;

                double year = Double.parseDouble(values[3]);
                yearSorted[index] = year;
                yearArray[index] = year;

                double imdb = Double.parseDouble(values[7]);
                imdbSorted[index] = imdb;
                imdbArray[index] = imdb;

                double budget = Double.parseDouble(values[9]);
                budgetSorted[index] = budget;
                budgetArray[index] = budget;

                double boxOffice = Double.parseDouble(values[8]);
                boxOfficeSorted[index] = boxOffice;
                boxOfficeArray[index] = boxOffice;

                labels[index] = Integer.parseInt(values[11]);
                index++;
            }
        }

        arraySorter(runTimeArraySorted);
        arraySorter(rottenTomatoesSorted);
        arraySorter(yearSorted);
        arraySorter(imdbSorted);
        arraySorter(budgetSorted);
        arraySorter(boxOfficeSorted);

        // Q2
        double q2 = (runTimeArraySorted[50] + runTimeArraySorted[49]) / 2;
        runtimeQuartiles[1] = q2;
        // Q1
        double q1 = (runTimeArraySorted[25] + runTimeArraySorted[24]) / 2;
        runtimeQuartiles[0] = q1;
        // Q3
        double q3 = (runTimeArraySorted[75] + runTimeArraySorted[74]) / 2;
        runtimeQuartiles[2] = q3;

        // find the odds associated with each bin
        for (int i = 0; i < runTimeArray.length; i++) {
            if (runTimeArray[i] >= runtimeQuartiles[2] && labels[i] == 1) {
                oddsRunTime[3]++;
            } else if (runTimeArray[i] >= runtimeQuartiles[1] && labels[i] == 1) {
                oddsRunTime[2]++;
            } else if (runTimeArray[i] >= runtimeQuartiles[0] && labels[i] == 1) {
                oddsRunTime[1]++;
            } else if (labels[i] == 1) {
                oddsRunTime[0]++;
            }
        }

        /*
        YEAR
         */
        // Q2
        q2 = (yearSorted[50] + yearSorted[49]) / 2;
        yearQuartiles[1] = q2;
        // Q1
        q1 = (yearSorted[25] + yearSorted[24]) / 2;
        yearQuartiles[0] = q1;
        // Q3
        q3 = (yearSorted[75] + yearSorted[74]) / 2;
        yearQuartiles[2] = q3;

        // find the odds associated with each bin
        for (int i = 0; i < 100; i++) {
            if (yearArray[i] >= yearQuartiles[2] && labels[i] == 1) {
                oddsYear[3]++;
            } else if (yearArray[i] >= yearQuartiles[1] && labels[i] == 1) {
                oddsYear[2]++;
            } else if (yearArray[i] >= yearQuartiles[0] && labels[i] == 1) {
                oddsYear[1]++;
            } else if (labels[i] == 1) {
                oddsYear[0]++;
            }
        }


        /*
        ROTTEN TOMATOES
         */
        // Q2
        q2 = (rottenTomatoesSorted[50] + rottenTomatoesSorted[49]) / 2;
        rottenTomatoesQuartiles[1] = q2;
        // Q1
        q1 = (rottenTomatoesSorted[25] + rottenTomatoesSorted[24]) / 2;
        rottenTomatoesQuartiles[0] = q1;
        // Q3
        q3 = (rottenTomatoesSorted[75] + rottenTomatoesSorted[74]) / 2;
        rottenTomatoesQuartiles[2] = q3;

        // find the odds associated with each bin
        for (int i = 0; i < 100; i++) {
            if (rottenTomatoesArray[i] >= rottenTomatoesQuartiles[2] && labels[i] == 1) {
                oddsRottenTomatoes[3]++;
            } else if (rottenTomatoesArray[i] >= rottenTomatoesQuartiles[1] && labels[i] == 1) {
                oddsRottenTomatoes[2]++;
            } else if (rottenTomatoesArray[i] >= rottenTomatoesQuartiles[0] && labels[i] == 1) {
                oddsRottenTomatoes[1]++;
            } else if (labels[i] == 1) {
                oddsRottenTomatoes[0]++;
            }
        }


        /*
        IMDB
         */
        // Q2
        q2 = (imdbSorted[50] + imdbSorted[49]) / 2;
        imdbQuartiles[1] = q2;
        // Q1
        q1 = (imdbSorted[25] + imdbSorted[24]) / 2;
        imdbQuartiles[0] = q1;
        // Q3
        q3 = (imdbSorted[75] + imdbSorted[74]) / 2;
        imdbQuartiles[2] = q3;

        // find the odds associated with each bin
        for (int i = 0; i < 100; i++) {
            if (imdbArray[i] >= imdbQuartiles[2] && labels[i] == 1) {
                oddsImdb[3]++;
            } else if (imdbArray[i] >= imdbQuartiles[1] && labels[i] == 1) {
                oddsImdb[2]++;
            } else if (imdbArray[i] >= imdbQuartiles[0] && labels[i] == 1) {
                oddsImdb[1]++;
            } else if (labels[i] == 1) {
                oddsImdb[0]++;
            }
        }

        /*
        BUDGET
         */
        // Q2
        q2 = (budgetSorted[50] + budgetSorted[49]) / 2;
        budgetQuartiles[1] = q2;
        // Q1
        q1 = (budgetSorted[25] + budgetSorted[24]) / 2;
        budgetQuartiles[0] = q1;
        // Q3
        q3 = (budgetSorted[75] + budgetSorted[74]) / 2;
        budgetQuartiles[2] = q3;

        // find the odds associated with each bin
        for (int i = 0; i < 100; i++) {
            if (budgetArray[i] >= budgetQuartiles[2] && labels[i] == 1) {
                oddsBudget[3]++;
            } else if (budgetArray[i] >= budgetQuartiles[1] && labels[i] == 1) {
                oddsBudget[2]++;
            } else if (budgetArray[i] >= budgetQuartiles[0] && labels[i] == 1) {
                oddsBudget[1]++;
            } else if (labels[i] == 1) {
                oddsBudget[0]++;
            }
        }

        /*
        BOX OFFICE SORTED
         */
        // Q2
        q2 = (boxOfficeSorted[50] + boxOfficeSorted[49]) / 2;
        boxOfficeQuartiles[1] = q2;
        // Q1
        q1 = (boxOfficeSorted[25] + boxOfficeSorted[24]) / 2;
        boxOfficeQuartiles[0] = q1;
        // Q3
        q3 = (boxOfficeSorted[75] + boxOfficeSorted[74]) / 2;
        boxOfficeQuartiles[2] = q3;

        // find the odds associated with each bin
        for (int i = 0; i < 100; i++) {
            if (boxOfficeArray[i] >= boxOfficeQuartiles[2] && labels[i] == 1) {
                oddsBoxOffice[3]++;
            } else if (boxOfficeArray[i] >= boxOfficeQuartiles[1] && labels[i] == 1) {
                oddsBoxOffice[2]++;
            } else if (boxOfficeArray[i] >= boxOfficeQuartiles[0] && labels[i] == 1) {
                oddsBoxOffice[1]++;
            } else if (labels[i] == 1) {
                oddsBoxOffice[0]++;
            }
        }

    }

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

    static void loadOddsGenre (String filePath) throws IOException {
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
            loadOddsGenre("training-set.csv");
            loadOddsRuntime("training-set.csv");
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
