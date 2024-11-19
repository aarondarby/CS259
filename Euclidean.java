public class Euclidean {

    public Euclidean(double[][] trainingData, int[] trainingLabels, double[][] testingData, int[] testingLabels) {
        // stores the best k value and the best prediction
        int best_k = 0;
        double best_prediction = 0.0;

        // tests for all possible (odd) k values
        for (int k = 1 ;k< testingData.length; k+=2) {
            // Compute accuracy on the testing set
            int correctPredictions = 0;
            for (int i =0; i<testingData.length; i++) {
                if (testingLabels[i] == knnClassify(trainingData,trainingLabels,testingData[i],k)) {
                    correctPredictions++;
                }
            }

            double accuracy = (double) correctPredictions / testingData.length * 100;
            if (accuracy> best_prediction) {

                best_prediction = accuracy;
                best_k = k;
            }
        }

        System.out.println("Using Euclidean distance");
        System.out.printf("A: %.2f%%\n", best_prediction);
        System.out.println("The best k value was " + best_k + "\n");
    }

    static int knnClassify(double[][] trainingData, int[] trainingLabels, double[] testFeature, int k) {

        // stores the k best matches and their corresponding index
        double best_matches_num[] = new double[k];
        int best_matches_index[] = new int[k];

        // populates the array with the first k distances
        for (int i =0; i< k; i++) {
            best_matches_num[i] = euclidean_distance(testFeature, trainingData[i]);
            best_matches_index[i] = i;
        }

        // sorts what we've added, so the largest value is at position [k]
        array_sorter(best_matches_index, best_matches_num);

        // goes through the rest of the training data
        for (int i = k; i < trainingData.length; i++) {
            double current_distance = 0.0;
            current_distance = euclidean_distance(testFeature, trainingData[i]);

            // lets array_utility decide if it's worth keeping
            array_utility(best_matches_index,best_matches_num,current_distance,i,k);
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

    /*
        This deals with the arrays automatically.
        The neighbour furthest away from the current test feature is stored at k-1.
        So we check if it's smaller than that value and if it is we replapce k-1 with the current distance.
     */
    static void array_utility(int[] best_matches_index, double[] best_matches_num, double current_distance, int index, int k) {
        if (current_distance == best_matches_num[k-1]) {
            System.out.println("we have a tie between " + best_matches_index[k-1] + " : " + index);
            double a = Math.random();
            if (a>.5) {
                best_matches_num[k-1] = current_distance;
                best_matches_index[k-1] = index;
                array_sorter(best_matches_index,best_matches_num);
            }
        }
        if (current_distance < best_matches_num[k - 1]) {
            best_matches_num[k - 1] = current_distance;
            best_matches_index[k - 1] = index;
            array_sorter(best_matches_index, best_matches_num);
        }
    }

    /*
        Sorts both the num array and index array.
        The num array is sorted ascending, and the index is sorted to match it's corresponding number
     */
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

    /*
    Using this as the distance formula should give a better result than what's in his slide.
     */
    static double euclidean_distance(double[] u, double[] v) {
        double sum = 0.0;
        for (int i = 0; i < u.length; i++) {
            sum+= Math.pow(u[i]-v[i], 2);
        }
        return Math.sqrt(sum);
    }

}
