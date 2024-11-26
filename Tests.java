//in progress creating template for the project (movie dataset reading, masking, KNN for K=1)
import java.io.*;
import java.util.SplittableRandom;

public class Tests {

    // Use we use 'static' for all methods to keep things simple, so we can call those methods main

    static void Assert (boolean res) // We use this to test our results - don't delete or modify!
    {
        if(!res)	{
            System.out.print("Something went wrong.");
            System.exit(0);
        }
    }

    // Copy your vector operations here:
    // ...


    static double [] mult(double a, double [] V) { // multiplying scalar and vector
        // add your code
        double [] Res = new double [V.length]; // create a vector of the same size, that we will return

        for (int i = 0; i<V.length; i++) {
            Res[i] = a*V[i];
        }

        return Res;
    }

    static double [][] mult(double a, double [][]b){
        double [][] ans = new double[b.length][b[0].length];
        for (int i = 0; i<b.length; i++) {
            ans[i] = mult(a, b[i]);           // treating every row as a vector
        }
        return ans;
    }



    static double [] add(double a, double [] V) { // adding scalar and vector
        // add your code
        double [] Res = new double [V.length];
        for (int i = 0; i < V.length; i++) {
            Res[i] = V[i] + a;
        }
        return Res;
    }
    static double [] sub(double a, double [] V) {  // subtracting a scalar from vector
        // add your code
        double [] Res = new double [V.length];
        for (int i = 0; i < V.length; i++) {
            Res[i] = V[i] - a;
        }
        return Res;
    }

    static double [] add(double [] U, double [] V) { // adding two vectors
        // add your code
        double [] Res = new double [V.length];
        for (int i = 0; i < V.length; i++) {
            Res[i] = V[i] + U[i];
        }
        return Res;
    }
    static double [] sub(double [] U, double [] V) { // subtracting vector from vector
        // add your code
        double [] Res = new double [V.length];
        for (int i = 0; i < V.length; i++) {
            Res[i] = U[i] - V[i];
        }
        return Res;
    }
    static double dot(double [] U, double [] V) { // dot product of two vectors
        // add your code
        double Res = 0;

        for (int i = 0; i < V.length; i++) {
            Res += V[i] * U[i];
        }
        return Res;
    }

    // Finish implementations of matrix operations:

    static double [] dot(double [][] U, double [] V) { // finish this function

        Assert(U[0].length == V.length);
        double[] ans = new double[U.length];
        // add some code here
        for (int i = 0; i < U.length; i++){
            ans[i] = dot(U[i], V);
        }
        return ans;
    }


    static double [][] add(double [][] a, double [][] b) {
        Assert(a.length == b.length);
        for (int i = 0;i < a.length; i++)
            Assert(a[i].length == b[i].length);
        double[][] ans = new double[a.length][a[0].length];
        // add some code here
        for (int i = 0; i<a.length; i++) {
            ans[i] = add(a[i], b[i]);


        }

        return ans;
    }


    static int NumberOfFeatures = 16;
    static double[] toFeatureVector(double id, String genre, double runtime, double year, double imdb,
                                    double rt, double budget, double boxOffice, String director, String actor) {

        double[] feature = new double[NumberOfFeatures];

        switch (genre) { // We also use represent each movie genre as an integer number:
            case "Action":  feature[0] = 1; break;
            case "Fantasy":   feature[0] = 0; break;
            case "Romance": feature[0] = 0; break;
            case "Sci-Fi": feature[0] = 0; break;
            case "Adventure": feature[0] = 1; break;
            case "Horror": feature[0] = 0; break;
            case "Comedy": feature[0] = 1; break;
            case "Thriller": feature[0] = 1; break;
            default: Assert(false);
        }
//
        feature[1] = (imdb>=7.8)? 1 : 0;
        feature[2] = (rt>=85)? 1 : 0;
  //      feature[3] = ( (boxOffice-budget) <= -3)? 1 : 0;
        feature[4] = ( runtime>=111 && runtime<=123)? 1 : 0;
        feature[5] = (boxOffice>=102)? 1 : 0;
        feature[6] = (budget >= 128)? 1 : 0;

//        feature[14] = director.matches("Christopher Nolan||" +
//                "George Miller||James Wan||John Woo||Jon Favreau||Jordan Peele||Kathryn Bigelow||" +
//                "Kenneth Branagh||Martin Scorsese||Ridley Scott||Spike Lee||Steven Spielberg")? 1 : 0;
//        feature[15] = actor.matches("Brad Pitt||Emma Stone||Jennifer Lawrence||Ryan Reynolds||Viola Davis")? 1 : 0;

        // That is all. We don't use any other attributes for prediction.


        return feature;
    }

    // We are using the dot product to determine similarity:
    static double similarity(double[] u, double[] v) {
        return dot(u, v);
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

    // We have implemented KNN classifier for the K=1 case only. You are welcome to modify it to support any K
    static int knnClassify(double[][] trainingData, int[] trainingLabels, double[] testFeature, int k) {

        // stores the k best matches and their corresponding index
        double best_matches_num[] = new double[k];
        int best_matches_index[] = new int[k];

        // populates the array with the first k similarities
        for (int i =0; i< k; i++) {
            best_matches_num[i] = dot(testFeature, trainingData[i]);
            best_matches_index[i] = i;
        }

        // sorts what we've added, so the largest value is at position [k]
        array_sorter(best_matches_index, best_matches_num);

        // goes through the rest of the training data
        for (int i = k; i < trainingData.length; i++) {
            double current_similarity = 0.0;
            current_similarity = dot(testFeature, trainingData[i]);

            // lets array_utility decide if it's worth keeping
            array_utility(best_matches_index,best_matches_num,current_similarity,i,k);
        }

        int num_pos = 0;
        int num_neg = 0;
        // goes through the k neighbours and checks their labels
        // if there are more positive labels than negative we return 1, else 0
        for (int i = 0; i < best_matches_index.length; i++) {
            int temp = trainingLabels[ best_matches_index[i] ];
            if (temp ==1) {num_pos++;}
            else {num_neg++;}
        }

        if (num_pos > num_neg) {return 1;}
        else {return 0;}
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

                String director = values[4];
                String actor = values[5];


                dataFeatures[idx] = toFeatureVector(id, genre, runtime, year, imdb, rt, budget, boxOffice, director, actor);
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

        // Compute accuracy on the testing set
        int correctPredictions = 0;

        int a;

        for (int k = 1; k<99; k+=2) {
            for (int i = 0; i<testingData.length; i++) {
                a = knnClassify(trainingData, trainingLabels, testingData[i],k);
                if (a == testingLabels[i]) {
                    correctPredictions++;
                }

            }
            double accuracy = (double) correctPredictions / testingData.length * 100;
            System.out.println("For k value: " + k + " accuracy was: " + accuracy);
            correctPredictions = 0;
        }
    }
}
