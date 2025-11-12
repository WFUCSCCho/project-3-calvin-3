/*∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗*
  @file: Proj3.java
  @description: This file stores my sorting algorithm implementations
                runs the program for 3 different data sets
                records timing and comparison data
                prints formatted results to the console
                outputs the sorted lists and timing analysis to files
  @author: Calvin Malaney
  @date: November 11, 2025
∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗*/

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import static java.nio.file.StandardOpenOption.*;

public class Proj3 {
    // Sorting Method declarations
    // Merge Sort
    public static <T extends Comparable<? super T>> void mergeSort(ArrayList<T> a, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        mergeSort(a, left, mid);
        mergeSort(a, mid + 1, right);
        merge(a, left, mid, right);
    }

    public static <T extends Comparable<? super T>> void merge(ArrayList<T> a, int left, int mid, int right) {
        ArrayList<T> temp = new ArrayList<>(right - left + 1);

        int i = left;      // left run
        int j = mid + 1;   // right run

        while (i <= mid && j <= right) {
            if (a.get(i).compareTo(a.get(j)) <= 0) {
                temp.add(a.get(i++));
            } else {
                temp.add(a.get(j++));
            }
        }
        while (i <= mid) temp.add(a.get(i++));
        while (j <= right) temp.add(a.get(j++));

        // write back
        for (int k = 0; k < temp.size(); k++) {
            a.set(left + k, temp.get(k));
        }
    }

    // Quick Sort
    // first/last pivot finding method
    public static <T extends Comparable<? super T>> void quickSort(ArrayList<T> a, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(a, left, right);
            quickSort(a, left, pivotIndex - 1);
            quickSort(a, pivotIndex + 1, right);
        }
    }

    public static <T extends Comparable<? super T>> int partition(ArrayList<T> a, int left, int right) {
        T pivot = a.get(right);  // last element pivot
        int i = left - 1;

        for (int j = left; j < right; j++) {
            if (a.get(j).compareTo(pivot) <= 0) {
                i++;
                swap(a, i, j);
            }
        }

        swap(a, i + 1, right); // place pivot in correct position
        return i + 1; // return pivot index
    }

    static <T> void swap(ArrayList<T> a, int i, int j) {
        T temp = a.get(i);
        a.set(i, a.get(j));
        a.set(j, temp);
    }

    // Heap Sort
    public static <T extends Comparable<? super T>> void heapSort(ArrayList<T> a, int left, int right) {
        heapify(a, left, right);                       // build max-heap
        for (int end = right; end > left; end--) {
            swap(a, left, end);                        // move max to its final spot
            siftDownMax(a, left, end - 1, left);       // restore heap on reduced range
        }
    }

    public static <T extends Comparable<? super T>> void heapify(ArrayList<T> a, int left, int right) {
        int lastParent = left + ((right - left + 1) / 2) - 1;
        for (int i = lastParent; i >= left; i--) {
            siftDownMax(a, left, right, i);
        }
    }

    private static <T extends Comparable<? super T>> void siftDownMax(ArrayList<T> a, int left, int right, int root) {
        int largest = root;
        int lc = left + (2 * (root - left) + 1);
        int rc = lc + 1;

        if (lc <= right && a.get(lc).compareTo(a.get(largest)) > 0) {
            largest = lc;
        }
        if (rc <= right && a.get(rc).compareTo(a.get(largest)) > 0) {
            largest = rc;
        }
        if (largest != root) {
            swap(a, root, largest);
            siftDownMax(a, left, right, largest);
        }
    }

    // Bubble Sort
    // returns number of element-to-element comparison
    public static <T extends Comparable<? super T>> int bubbleSort(ArrayList<T> a, int size) {
        int comparisons = 0;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                comparisons++;
                if (a.get(j).compareTo(a.get(j + 1)) > 0) {
                    swap(a, j, j + 1);
                }
            }
        }
        return comparisons;
    }

    // Odd-Even Transposition Sort
    public static <T extends Comparable<? super T>> int transpositionSort(ArrayList<T> a, int size) {
        if (size <= 1) return 0;

        boolean sorted = false;
        int phaseCount = 0;

        while (!sorted) {
            sorted = true;

            // odd phase
            boolean swappedThisPhase = false;
            for (int i = 1; i <= size - 2; i += 2) {
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    swappedThisPhase = true;
                }
            }
            phaseCount++; // count one parallel comparison phase
            if (swappedThisPhase) sorted = false;

            // even phase
            swappedThisPhase = false;
            for (int i = 0; i <= size - 2; i += 2) {
                if (a.get(i).compareTo(a.get(i + 1)) > 0) {
                    swap(a, i, i + 1);
                    swappedThisPhase = true;
                }
            }
            phaseCount++; // count the even phases
            if (swappedThisPhase) sorted = false;
        }
        return phaseCount;
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.out.println("Usage: java Proj3 <filename> <algorithm> <nLines>");
            System.out.println("Algorithms: bubble | transposition | merge | quick | heap");
            return;
        }

        String filename = args[0];
        String alg = args[1].toLowerCase(Locale.ROOT).trim();
        int nLines = Integer.parseInt(args[2]);

        // Read dataset as lines
        ArrayList<String> base = readFirstNLines(filename, nLines);

        // Build three arrays
        ArrayList<String> sorted = new ArrayList<>(base);
        Collections.sort(sorted);

        ArrayList<String> shuffled = new ArrayList<>(base);
        Collections.shuffle(shuffled);

        ArrayList<String> reversed = new ArrayList<>(base);
        Collections.sort(reversed, Collections.reverseOrder());

        // Run / report for each state
        Result r1 = runOnce("sorted", alg, sorted);
        Result r2 = runOnce("shuffled",       alg, shuffled);
        Result r3 = runOnce("reversed",       alg, reversed);

        // print
        print(Arrays.asList(r1, r2, r3), nLines);

        // Append CSV to analysis.txt
        appendCsv("analysis.txt", Arrays.asList(r1, r2, r3));

        // Overwrite sorted.txt with the fully sorted outputs (three sections)
        writeSortedOutputs("sorted.txt", r1, r2, r3);
    }

    public static class Result {
        String algorithm;
        String inputState;      // already-sorted | shuffled | reversed
        int n;
        long nanos;
        long comparisons;       // -1 if N/A
        ArrayList<String> sortedOutput;

        String csvHeader() {
            return "algorithm,input_state,n,time_nanos,comparisons";
        }
        String toCsv() {
            return String.join(",",
                    algorithm,
                    inputState,
                    Integer.toString(n),
                    Long.toString(nanos),
                    Long.toString(comparisons));
        }
    }

    private static ArrayList<String> readFirstNLines(String filename, int n) throws IOException {
        ArrayList<String> out = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8)) {
            String line;
            while ((line = br.readLine()) != null && out.size() < n) {
                if (!line.isEmpty()) out.add(line);
            }
        }
        return out;
    }

    private static Result runOnce(String inputState, String alg, ArrayList<String> data) {
        // copy so we never mutate the builder input by accident
        ArrayList<String> a = new ArrayList<>(data);
        int n = a.size();

        long comparisons = -1;
        long t0 = System.nanoTime();

        switch (alg) {
            case "bubble":
                comparisons = bubbleSort(a, n);
                break;
            case "transposition":
                comparisons = transpositionSort(a, n);
                break;
            case "merge":
                if (n > 0) mergeSort(a, 0, n - 1);
                break;
            case "quick":
                if (n > 0) quickSort(a, 0, n - 1);
                break;
            case "heap":
                if (n > 0) heapSort(a, 0, n - 1);
                break;
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + alg);
        }

        long t1 = System.nanoTime();

        Result r = new Result();
        r.algorithm = alg;
        r.inputState = inputState;
        r.n = n;
        r.nanos = (t1 - t0);
        r.comparisons = comparisons;
        r.sortedOutput = a;
        return r;
    }

    private static void print(List<Result> results, int nLines) {
        System.out.println("\n======= Sorting Performance =======");
        for (Result r : results) {
            System.out.printf(
                    "%s | %-14s | n=%d | time=%.3f ms%s%n",
                    r.algorithm,
                    r.inputState,
                    r.n,
                    r.nanos / 1_000_000.0,
                    (r.comparisons >= 0 ? String.format(" | comparisons=%d", r.comparisons) : "")
            );
        }
    }

    private static void appendCsv(String path, List<Result> results) throws RuntimeException {
        try {
            Path p = Paths.get(path);
            boolean newFile = !Files.exists(p);
            try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8,
                    CREATE, APPEND)) {
                if (newFile) {
                    bw.write(results.get(0).csvHeader());
                    bw.newLine();
                }
                for (Result r : results) {
                    bw.write(r.toCsv());
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write analysis.txt: " + e.getMessage(), e);
        }
    }

    private static void writeSortedOutputs(String path, Result... results) throws RuntimeException {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8,
                CREATE, TRUNCATE_EXISTING)) {
            for (Result r : results) {
                bw.write("==== " + r.algorithm + " | " + r.inputState + " ====");
                bw.newLine();
                for (String s : r.sortedOutput) {
                    bw.write(s);
                    bw.newLine();
                }
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write sorted.txt: " + e.getMessage(), e);
        }
    }
}
