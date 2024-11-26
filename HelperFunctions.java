import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
    // has the odds of each quartile of runtime [0]=Q1, [1]=Q2, [2]=Q3, [3]=Q4
    static double[] oddsRunTime = new double[4];
    // Quartiles store the number where the quartile kicks in so if the number is greater than the number in [2] then it's in the 4th Quartile
    // if the number is greater than the number in [1] then it's in the 3rd quartile
    // if the number is greater than the number in [0] then it's in the 2nd quartile
    // else it's in the 1st quartile
    static double[] runtimeQuartiles = new double[3];
    static double[] oddsRottenTomatoes = new double[4];
    static double[] rottenTomatoesQuartiles = new double[3];
    static double[] oddsImdb = new double[4];
    static double[] imdbQuartiles = new double[3];
    static double[] oddsBudget = new double[4];
    static double[] budgetQuartiles = new double[3];
    static double[] oddsBoxOffice = new double[4];
    static double[] boxOfficeQuartiles = new double[3];


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


    static void loadOddsRuntime(String filePath) throws IOException {


        double[] runTimeArraySorted = new double[100];
        double[] rottenTomatoesSorted = new double[100];
        double[] imdbSorted = new double[100];
        double[] budgetSorted = new double[100];
        double[] boxOfficeSorted = new double[100];

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
                runTimeArraySorted[index] = runtime;
                runTimeArray[index] = runtime;

                double rottenTomatoes = Double.parseDouble(values[6]);
                rottenTomatoesSorted[index] = rottenTomatoes;
                rottenTomatoesArray[index] = rottenTomatoes;

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
        arraySorter(imdbSorted);
        arraySorter(budgetSorted);
        arraySorter(boxOfficeSorted);

        int[] temp = new int[4];


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
            if (runTimeArray[i] >= runtimeQuartiles[2]) {
                if (labels[i] ==1) {
                    oddsRunTime[3]++;
                } else {
                    temp[3]++;
                }
            } else if (runTimeArray[i] >= runtimeQuartiles[1]) {
                if (labels[i] ==1) {
                    oddsRunTime[2]++;
                } else {
                    temp[2]++;
                }
            } else if (runTimeArray[i] >= runtimeQuartiles[0]) {
                if (labels[i]==1) {
                    oddsRunTime[1]++;
                } else {
                    temp[1]++;
                }
            } else {
                if (labels[i] ==1) {
                    oddsRunTime[0]++;
                } else {
                    temp[0]++;
                }

            }
        }

        for (int i =0; i<4; i++) {
            temp[i] += oddsRunTime[i];
        }

        for (int i= 0; i<4; i++) {
            oddsRunTime[i] /= temp[i];
            temp[i] = 0;
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
            if (rottenTomatoesArray[i] >= rottenTomatoesQuartiles[2]) {
                if (labels[i]==1) {
                    oddsRottenTomatoes[3]++;
                } else {
                    temp[3]++;
                }

            } else if (rottenTomatoesArray[i] >= rottenTomatoesQuartiles[1]) {
                if (labels[i]==1) {
                    oddsRottenTomatoes[2]++;
                } else {
                    temp[2]++;
                }


            } else if (rottenTomatoesArray[i] >= rottenTomatoesQuartiles[0]) {
                if (labels[i]==1) {
                    oddsRottenTomatoes[1]++;
                } else {
                    temp[1]++;
                }
            } else if (labels[i] == 1) {
                if (labels[i]==1) {
                    oddsRottenTomatoes[0]++;
                } else {
                    temp[0]++;
                }
            }
        }

        for (int i =0; i<4; i++) {
            temp[i] += oddsRottenTomatoes[i];
        }

        for (int i= 0; i<4; i++) {
            oddsRottenTomatoes[i] /= temp[i];
            temp[i] = 0;
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
            if (imdbArray[i] >= imdbQuartiles[2]) {
                if (labels[i]==1) {
                    oddsImdb[3]++;
                } else {
                    temp[3]++;
                }

            } else if (imdbArray[i] >= imdbQuartiles[1]) {
                if (labels[i]==1) {
                    oddsImdb[2]++;
                } else {
                    temp[2]++;
                }
            } else if (imdbArray[i] >= imdbQuartiles[0]) {
                if (labels[i]==1) {
                    oddsImdb[1]++;
                } else {
                    temp[1]++;
                }

            } else {
                if (labels[i]==1) {
                    oddsImdb[0]++;
                } else {
                    temp[0]++;
                }

            }
        }

        for (int i =0; i<4; i++) {
            temp[i] += oddsImdb[i];
        }

        for (int i= 0; i<4; i++) {
            oddsImdb[i] /= temp[i];
            temp[i] = 0;
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
            if (budgetArray[i] >= budgetQuartiles[2]) {
                if (labels[i]==1) {
                    oddsBudget[3]++;
                } else {
                    temp[3]++;
                }

            } else if (budgetArray[i] >= budgetQuartiles[1]) {
                if (labels[i]==1) {
                    oddsBudget[2]++;
                } else {
                    temp[2]++;
                }

            } else if (budgetArray[i] >= budgetQuartiles[0]) {
                if (labels[i]==1) {
                    oddsBudget[1]++;
                } else {
                    temp[1]++;
                }

            } else {
                if (labels[i]==1) {
                    oddsBudget[0]++;
                } else {
                    temp[0]++;
                }

            }
        }

        for (int i =0; i<4; i++) {
            temp[i] += oddsBudget[i];
        }

        for (int i= 0; i<4; i++) {
            oddsBudget[i] /= temp[i];
            temp[i] = 0;
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
            if (boxOfficeArray[i] >= boxOfficeQuartiles[2]) {
                if (labels[i]==1) {
                    oddsBoxOffice[3]++;
                } else {
                    temp[3]++;
                }

            } else if (boxOfficeArray[i] >= boxOfficeQuartiles[1]) {
                if (labels[i]==1) {
                    oddsBoxOffice[2]++;
                } else {
                    temp[2]++;
                }

            } else if (boxOfficeArray[i] >= boxOfficeQuartiles[0]) {
                if (labels[i]==1) {
                    oddsBoxOffice[1]++;
                } else {
                    temp[1]++;
                }

            } else {
                if (labels[i]==1) {
                    oddsBoxOffice[0]++;
                } else {
                    temp[0]++;
                }

            }
        }
        for (int i =0; i<4; i++) {
            temp[i] += oddsBoxOffice[i];
        }

        for (int i= 0; i<4; i++) {
            oddsBoxOffice[i] /= temp[i];
            temp[i] = 0;
        }

    }


}
