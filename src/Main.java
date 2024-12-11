import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("***Task 1***\nTwo-dimensional array operations\n");
        task1();

        System.out.println("\n***Task 2***\nSequence operations\n");
        task2();
    }

    private static void task1() throws ExecutionException, InterruptedException {
        long startTime = System.nanoTime();

        // 3x3 async generated matrix
        CompletableFuture<int[][]> matrixFuture = CompletableFuture.supplyAsync(() -> {
            int[][] matrix = new int[3][3];
            Random random = new Random();
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    matrix[i][j] = random.nextInt(100); // random numbers 0-99
                }
            }
            System.out.println("Generated matrix:\n" + Arrays.deepToString(matrix));
            return matrix;
        });

        // async print
        CompletableFuture<Void> printColumnsFuture = matrixFuture.thenAcceptAsync(matrix -> {
            for (int col = 0; col < 3; col++) {
                StringBuilder columnData = new StringBuilder("Column " + (col + 1) + ": ");
                for (int row = 0; row < 3; row++) {
                    columnData.append(matrix[row][col]).append(", ");
                }
                System.out.println(columnData.substring(0, columnData.length() - 2));
            }
        });

        // run async task
        CompletableFuture<Void> finalStep = printColumnsFuture.thenRunAsync(() -> {
            System.out.println("All tasks in Task 1 completed.");
        });

        finalStep.get(); // wait until done to get exec time

        long endTime = System.nanoTime();
        System.out.println("Task 1 Execution Time: " + (endTime - startTime) / 1_000_000 + " ms\n");
    }

    private static void task2() throws ExecutionException, InterruptedException {
        long startTime = System.nanoTime();

        // generate 20 values
        CompletableFuture<double[]> sequenceFuture = CompletableFuture.supplyAsync(() -> {
            double[] sequence = new double[20];
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                sequence[i] = random.nextDouble() * 100; // random doubles 0-100
            }
            System.out.println("Generated sequence: " + Arrays.toString(sequence));
            return sequence;
        });

        // async sum compute
        CompletableFuture<Double> sumFuture = sequenceFuture.thenApplyAsync(sequence -> {
            double sum = 0;
            for (double num : sequence) {
                sum += num;
            }
            System.out.println("Sum of sequence: " + sum);
            return sum;
        });

        // completion task
        CompletableFuture<Void> finalStep = sumFuture.thenRunAsync(() -> {
            System.out.println("All tasks in Task 2 completed.");
        });

        finalStep.get(); // wait until done to get exec time

        long endTime = System.nanoTime();
        System.out.println("Task 2 Execution Time: " + (endTime - startTime) / 1_000_000 + " ms\n");
    }
}
