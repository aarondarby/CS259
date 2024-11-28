import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Bayes {

    public Bayes () throws IOException {
        loadOdds();
        loadData("testing-set.csv", testingData, testingLabels);
        checkForZeros();

        // tests for all possible (odd) k values

        int correctPredictions = 0;

        double liked = 0;
        for (int i =0; i<testingData.length; i++) {

            liked = returnBayes(testingData[i])>=0.5? 1 : 0;
            if (testingLabels[i] == liked) {
                correctPredictions++;
            }
        }

        double accuracy = (double) correctPredictions / testingData.length * 100;
//            System.out.println("k value: " + k + "    accuracy:" + accuracy);



        System.out.println("Using bayes");
        System.out.printf("A: %.2f%%\n", accuracy);

    }

    double[][] trainingData = new double[100][];
    int[] trainingLabels = new int[100];
    double[][] testingData = new double[100][];
    int[] testingLabels = new int[100];

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

                String director = values[4];
                String actor = values[5];


                dataFeatures[idx] = toFeatureVector(id, genre, runtime, year, imdb, rt, budget, boxOffice, director, actor);
                dataLabels[idx] = Integer.parseInt(values[11]); // Assuming the label is the last column and is numeric
                idx++;
            }
        }
    }

    static double[] toFeatureVector(double id, String genre, double runtime, double year, double imdb,
                                    double rt, double budget, double boxOffice, String director, String actor) {

        double[] feature = new double[11];
        feature[10] = runtime;
        feature[7] = imdb;
        feature[6] = rt;
        feature[9] = budget;
        feature[8] = boxOffice;

        return feature;
    }


    static double[] oddsRunTime = new double[4];
    static double[] oddsNotLikedRunTime = new double[4];
    static double[] runtimeQuartiles = new double[3];

    static double[] oddsRottenTomatoes = new double[4];
    static double[] oddsNotLikedRottenTomatoes = new double[4];
    static double[] rottenTomatoesQuartiles = new double[3];

    static double[] oddsImdb = new double[4];
    static double[] oddsNotLikedImdb = new double[4];
    static double[] imdbQuartiles = new double[3];

    static double[] oddsBudget = new double[4];
    static double[] oddsNotLikedBudget = new double[4];
    static double[] budgetQuartiles = new double[3];

    static double[] oddsBoxOffice = new double[4];
    static double[] oddsNotLikedBoxOffice = new double[4];
    static double[] boxOfficeQuartiles = new double[3];


    static void checkForZeros() {
        for (int i =0; i<4; i++) {
            if (oddsRunTime[i]==0.0) {
                oddsRunTime[i] = 0.01;
            }
            if (oddsNotLikedRunTime[i]==0.0) {
                oddsNotLikedRunTime[i] = 0.01;
            }
            if (oddsRottenTomatoes[i]==0.0) {
                oddsRottenTomatoes[i] = 0.01;
            }
            if (oddsNotLikedRottenTomatoes[i]==0.0) {
                oddsNotLikedRottenTomatoes[i] = 0.01;
            }
            if (oddsImdb[i] == 0.0) {
                oddsImdb[i] = 0.01;
            }
            if (oddsNotLikedImdb[i] == 0.0) {
                oddsNotLikedImdb[i] = 0.01;
            }
            if (oddsBudget[i] == 0.0) {
                oddsBudget[i] = 0.01;
            }
            if (oddsNotLikedBudget[i] == 0.0) {
                oddsNotLikedBudget[i] = 0.01;
            }
            if (oddsBoxOffice[i] == 0.0) {
                oddsBoxOffice[i] = 0.01;
            }
            if (oddsNotLikedBoxOffice[i] == 0.0) {
                oddsNotLikedBoxOffice[i] = 0.01;
            }
        }
    }

    /*
    Returns the bayes probability.
    odds is set to .5 as there are 50 liked and 50 not liked
 */
    static double returnBayes(double[] testingData) {
        double odds = 0.5;

        if (testingData[10] > runtimeQuartiles[2]) {
            odds*= oddsRunTime[3] / oddsNotLikedRunTime[3];
        } else if (testingData[10] >runtimeQuartiles[1]) {
            odds *= oddsRunTime[2] / oddsNotLikedRunTime[2];
        } else if (testingData[10] >runtimeQuartiles[0]) {
            odds *= oddsRunTime[1] / oddsNotLikedRunTime[1];
        } else {
            odds *= oddsRunTime[0] / oddsNotLikedRunTime[0];
        }

        if (odds==0) System.out.println("There's an issue");

        if (testingData[6] > rottenTomatoesQuartiles[2]) {
            odds*= oddsRottenTomatoes[3] / oddsNotLikedRottenTomatoes[3];
        } else if (testingData[6] >rottenTomatoesQuartiles[1]) {
            odds *= oddsRottenTomatoes[2] / oddsNotLikedRottenTomatoes[2];
        } else if (testingData[6] >rottenTomatoesQuartiles[0]) {
            odds *= oddsRottenTomatoes[1] / oddsNotLikedRottenTomatoes[1];
        } else {
            odds *= oddsRunTime[0]/oddsNotLikedRottenTomatoes[0];
        }

        if (odds==0) System.out.println("There's an issue");

        if (testingData[7] > imdbQuartiles[2]) {
            odds*= oddsImdb[3] / oddsNotLikedImdb[3];
        } else if (testingData[7] >imdbQuartiles[1]) {
            odds *= oddsImdb[2] / oddsNotLikedImdb[2];
        } else if (testingData[7] >imdbQuartiles[0]) {
            odds *= oddsImdb[1] / oddsNotLikedImdb[1];
        } else {
            odds *= oddsImdb[0] / oddsNotLikedImdb[0];
        }

        if (odds==0) System.out.println("There's an issue");

        if (testingData[9] > budgetQuartiles[2]) {
            odds*= oddsBudget[3] / oddsNotLikedBudget[3];
        } else if (testingData[9] >budgetQuartiles[1]) {
            odds *= oddsBudget[2] / oddsNotLikedBudget[2];
        } else if (testingData[9] >budgetQuartiles[0]) {
            odds *= oddsBudget[1] / oddsNotLikedBudget[1];
        } else {
            odds *= oddsBudget[0] / oddsNotLikedBudget[0];
        }

        if (odds==0) System.out.println("There's an issue");

        if (testingData[8] > boxOfficeQuartiles[2]) {
            odds*= oddsBoxOffice[3] / oddsNotLikedBoxOffice[3];
        } else if (testingData[8] >boxOfficeQuartiles[1]) {
            odds *= oddsBoxOffice[2] / oddsNotLikedBoxOffice[2];
        } else if (testingData[8] >boxOfficeQuartiles[0]) {
            odds *= oddsBoxOffice[1] / oddsNotLikedBoxOffice[1];
        } else {
            odds *= oddsBoxOffice[0] / oddsNotLikedBoxOffice[0];
        }

        if (odds==0) System.out.println("There's an issue");

        return odds;
    }

    static void array_utility(int[] best_matches_index, double[] best_matches_num, double similarity, int index, int k) {
        if (similarity == best_matches_num[0]) {
            double a = Math.random();
            if (a>.5) {
                best_matches_num[0] = similarity;
                best_matches_index[0] = index;
                array_sorter(best_matches_index,best_matches_num);
            }
        }

        if (similarity > best_matches_num[0]) {
            best_matches_num[0] = similarity;
            best_matches_index[0] = index;
            array_sorter(best_matches_index, best_matches_num);
        }
    }

    static void array_sorter(int[] best_matches_index, double[] best_matches_num) {
        int n = best_matches_num.length;
        for (int i = 0; i < n - 1; i++) {

            int min_idx = i;

            for (int j = i + 1; j < n; j++) {
                if (best_matches_num[j] < best_matches_num[min_idx]) {
                    min_idx = j;
                }
            }

            double temp = best_matches_num[i];
            best_matches_num[i] = best_matches_num[min_idx];
            best_matches_num[min_idx] = temp;

            int temp2 = best_matches_index[i];
            best_matches_index[i] = best_matches_index[min_idx];
            best_matches_index[min_idx] = temp2;
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

    static void oddsUtility(String typeOfValue, double[] unsorted, int[] labels) {

        double[] oddsLiked = new double[10];
        double[] oddsNotLiked = new double[10];
        double[] quartiles = new double[10];
        double[] sorted = new double[100];

        for (int i =0; i<100; i++) {
            sorted[i] = unsorted[i];
        }

        arraySorter(sorted);


        int[] totalPerQuartile = new int[4];
        switch(typeOfValue){
            case "Runtime":
                oddsLiked = oddsRunTime;
                oddsNotLiked = oddsNotLikedRunTime;
                quartiles = runtimeQuartiles;
                break;
            case "IMDB":
                oddsLiked = oddsImdb;
                oddsNotLiked = oddsNotLikedImdb;
                quartiles = imdbQuartiles;
                break;
            case "Rotten Tomatoes":
                oddsLiked = oddsRottenTomatoes;
                oddsNotLiked = oddsNotLikedRottenTomatoes;
                quartiles = rottenTomatoesQuartiles;
                break;
            case "Budget":
                oddsLiked = oddsBudget;
                oddsNotLiked = oddsNotLikedBudget;
                quartiles = budgetQuartiles;
                break;
            case "Box Office":
                oddsLiked = oddsBoxOffice;
                oddsNotLiked = oddsNotLikedBoxOffice;
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

        // find the odds associated with each bin
        for (int i = 0; i < 100; i++) {
            if (unsorted[i] >= quartiles[2]) {
                if (labels[i] ==1) {
                    oddsLiked[3]++;
                }
                totalPerQuartile[3]++;
            } else if (unsorted[i] >= quartiles[1]) {
                if (labels[i] ==1) {
                    oddsLiked[2]++;
                }
                totalPerQuartile[2]++;
            } else if (unsorted[i] >= quartiles[0]) {
                if (labels[i]==1) {
                    oddsLiked[1]++;
                }
                totalPerQuartile[1]++;
            } else {
                if (labels[i] ==1) {
                    oddsLiked[0]++;
                }
                totalPerQuartile[0]++;
            }
        }

        for (int i =0; i<4; i++) {
            oddsNotLiked[i] = ( totalPerQuartile[i] - oddsLiked[i] ) / totalPerQuartile[i];
            oddsLiked[i] /= totalPerQuartile[i];
        }

    }




    /*
       Takes the filepath of the training data.
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
}