import java.util.*;

public class FCFS_Non_Preemptive {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of processes: ");
        int n = sc.nextInt();
        
        int Busr_time[] = new int[n];
        
        int Process_id[] = new String[n];
        int Arrival_time[] = new int[n];
        int Burst_time[] = new int[n];
        int Compilation_time[] = new int[n];
        int Turn_arround_time[] = new int[n];
        int Wating_time[] = new int[n];
        int Response_time[] = new int[n];
        
        for (int i = 0; i < n; i++) {
            Process_id[i] = i + 1;
            System.out.print("Enter arrival time of P" + Process_id[i] + ": ");
            Arrival_time[i] = sc.nextInt();
            System.out.print("Enter burst time of P" + Process_id[i] + ": ");
            Busr_time[i] = sc.nextInt();
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (Arrival_time[i] > Arrival_time[j]) {
                    swap(Arrival_time, i, j);
                    swap(Burst_time, i, j);
                    swap(Process_id, i, j);
                }
            }
        }

        int currentTime = 0;
        for (int i = 0; i < n + 1; i++) {
            Compilation_time[i] = currentTime + Burst_time[i];
            currentTime = Compilation_time[i];
        }

        for (int i = 0; i < n; i++) {
            System.out.println("P" + Process_id[i] + "\t" + Arrival_time[i] + "\t" + Burst_time[i] + "\t" + Compilation_time[i]);
        }
        sc.close();
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}