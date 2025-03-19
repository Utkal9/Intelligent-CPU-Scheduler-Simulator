import java.util.*;

public class IntelligentCpuScheduleSimulator {
    static double bestAWT = Double.MAX_VALUE;
    static String bestAlgo = "";
    static int choice;
    
    // Global variables to store process information
    static int n; // Number of processes
    static int[] Process_id, Arrival_time, Burst_time, Priority, Completion_time, Turnaround_time, Waiting_time, Response_time;
    static int[] Remaining_burst_time;
    static boolean[] isFirstResponse, isCompleted;
    static int[] bestProcess_id, bestArrival_time, bestBurst_time, bestCompletion_time, bestTurnaround_time, bestWaiting_time, bestResponse_time;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // Step 1: Take all input data once
        takeInputs(sc);

        // Step 2: Run selected scheduling algorithms
        while (true) {
            System.out.println("\nSelect Scheduling Algorithm to calculate Average Waiting Time:");
            System.out.println("1. FCFS");
            System.out.println("2. SJF (Non-Preemptive)");
            System.out.println("3. SJF (Preemptive)");
            System.out.println("4. Round Robin");
            System.out.println("5. Priority Scheduling");
            System.out.println("0. Exit & Show Best Algorithm");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();

            if (choice == 0) break;

            switch (choice) {
                case 1: fcfs(); break;
                case 2: sjfNonPreemptive(); break;
                case 3: sjfPreemptive(); break;
                case 4: roundRobin(sc); break;
                case 5: priorityScheduling(sc); break;
                default: System.out.println("Invalid choice! Try again.");
            }
        }

        // Display the best algorithm
        if (!bestAlgo.isEmpty()) {
            System.out.println("\nBest Scheduling Algorithm: " + bestAlgo);
            displayResults(bestProcess_id.length, choice, bestProcess_id, bestArrival_time, bestBurst_time, bestCompletion_time, bestTurnaround_time, bestWaiting_time, bestResponse_time);
        }

        sc.close();
    }

    private static void takeInputs(Scanner sc) {
        System.out.print("Enter the number of processes: ");
        n = sc.nextInt();

        Process_id = new int[n];
        Arrival_time = new int[n];
        Burst_time = new int[n];
        Priority = new int[n];
        Completion_time = new int[n];
        Turnaround_time = new int[n];
        Waiting_time = new int[n];
        Response_time = new int[n];
        Remaining_burst_time = new int[n];
        isFirstResponse = new boolean[n];
        isCompleted = new boolean[n];

        for (int i = 0; i < n; i++) {
            Process_id[i] = i + 1;
            System.out.print("Enter arrival time of P" + Process_id[i] + ": ");
            Arrival_time[i] = sc.nextInt();
            System.out.print("Enter burst time of P" + Process_id[i] + ": ");
            Burst_time[i] = sc.nextInt();
            Remaining_burst_time[i] = Burst_time[i];
            isFirstResponse[i] = true;
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
    }

    private static void displayResults(int n, int choice, int[] Process_id, int[] Arrival_time, int[] Burst_time, int[] Completion_time, int[] Turnaround_time, int[] Waiting_time, int[] Response_time) {
        double totalWT = 0;
        if(choice==0){
            System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT\tRT");
        }
        
        for (int i = 0; i < n; i++) {
            totalWT += Waiting_time[i];
            if(choice==0){
                System.out.println("P" + Process_id[i] + "\t" + Arrival_time[i] + "\t" + Burst_time[i] + "\t" + Completion_time[i] + "\t" + Turnaround_time[i] + "\t" + Waiting_time[i] + "\t" + Response_time[i]);
            }
        }

        double avgWT = totalWT / n;
        System.out.println("\nAverage Waiting Time: " + avgWT);

        if (avgWT < bestAWT) {
            bestAWT = avgWT;
            bestAlgo = Thread.currentThread().getStackTrace()[2].getMethodName().toUpperCase();
            bestProcess_id = Process_id.clone();
            bestArrival_time = Arrival_time.clone();
            bestBurst_time = Burst_time.clone();
            bestCompletion_time = Completion_time.clone();
            bestTurnaround_time = Turnaround_time.clone();
            bestWaiting_time = Waiting_time.clone();
            bestResponse_time = Response_time.clone();
        }
    }

    private static void fcfs() {
        // FCFS Scheduling Logic
        int[] Completion_time = new int[n];
        int[] Turnaround_time = new int[n];
        int[] Waiting_time = new int[n];
        int[] Response_time = new int[n];

        int currentTime = 0;

        for (int i = 0; i < n; i++) {
            if (currentTime < Arrival_time[i]) {
                currentTime = Arrival_time[i];
            }
            Completion_time[i] = currentTime + Burst_time[i];
            currentTime = Completion_time[i];
        }

        for (int i = 0; i < n; i++) {
            Turnaround_time[i] = Completion_time[i] - Arrival_time[i];
            Waiting_time[i] = Turnaround_time[i] - Burst_time[i];
            Response_time[i] = Waiting_time[i];
        }

        displayResults(n, choice, Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time);
    }

    private static void sjfNonPreemptive() {
        // SJF Non-Preemptive Scheduling Logic
        int[] Completion_time = new int[n];
        int[] Turnaround_time = new int[n];
        int[] Waiting_time = new int[n];
        int[] Response_time = new int[n];
        boolean[] isCompleted = new boolean[n];

        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(i -> Arrival_time[i]));

        int currentTime = 0, completed = 0;

        while (completed < n) {
            int idx = -1, minBurstTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!isCompleted[i] && Arrival_time[i] <= currentTime && Burst_time[i] < minBurstTime) {
                    minBurstTime = Burst_time[i];
                    idx = i;
                }
            }

            if (idx == -1) {
                currentTime++;
            } else {
                Completion_time[idx] = currentTime + Burst_time[idx];
                currentTime = Completion_time[idx];

                Turnaround_time[idx] = Completion_time[idx] - Arrival_time[idx];
                Waiting_time[idx] = Turnaround_time[idx] - Burst_time[idx];
                Response_time[idx] = Waiting_time[idx];

                isCompleted[idx] = true;
                completed++;
            }
        }

        displayResults(n, choice, Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time);
    }

    private static void sjfPreemptive() {
        // SJF Preemptive Scheduling Logic
        int[] Remaining_burst_time = new int[n];
        int[] Completion_time = new int[n];
        int[] Turnaround_time = new int[n];
        int[] Waiting_time = new int[n];
        int[] Response_time = new int[n];
        int[] firstExecution = new int[n];
        boolean[] isCompleted = new boolean[n];

        for (int i = 0; i < n; i++) {
            Remaining_burst_time[i] = Burst_time[i];
            firstExecution[i] = -1;
        }

        int currentTime = 0, completed = 0;

        while (completed < n) {
            int idx = -1, minBurstTime = Integer.MAX_VALUE;

            for (int i = 0; i < n; i++) {
                if (!isCompleted[i] && Arrival_time[i] <= currentTime && Remaining_burst_time[i] < minBurstTime) {
                    minBurstTime = Remaining_burst_time[i];
                    idx = i;
                }
            }

            if (idx == -1) {
                currentTime++;
            } else {
                if (firstExecution[idx] == -1) {
                    firstExecution[idx] = currentTime;
                }

                Remaining_burst_time[idx]--;
                currentTime++;

                if (Remaining_burst_time[idx] == 0) {
                    isCompleted[idx] = true;
                    completed++;
                    Completion_time[idx] = currentTime;
                    Turnaround_time[idx] = Completion_time[idx] - Arrival_time[idx];
                    Waiting_time[idx] = Turnaround_time[idx] - Burst_time[idx];
                    Response_time[idx] = firstExecution[idx] - Arrival_time[idx];
                }
            }
        }

        displayResults(n, choice, Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time);
    }

    private static void roundRobin(Scanner sc) {
        // Round Robin Scheduling Logic
        System.out.print("Enter time quantum: ");
        int timeQuantum = sc.nextInt();
    
        int[] Remaining_burst_time = new int[n];
        int[] Completion_time = new int[n];
        int[] Turnaround_time = new int[n];
        int[] Waiting_time = new int[n];
        int[] Response_time = new int[n];
        boolean[] isFirstResponse = new boolean[n];
    
        // Copy Burst Time to remaining burst time
        System.arraycopy(Burst_time, 0, Remaining_burst_time, 0, n);
    
        // Initialize all processes as unprocessed
        Arrays.fill(isFirstResponse, true);
    
        Queue<Integer> queue = new LinkedList<>();
        int currentTime = 0, completed = 0, lastAdded = 0;
        
        while (completed < n) {
            // Add all processes that have arrived by the current time to the queue
            while (lastAdded < n && Arrival_time[lastAdded] <= currentTime) {
                queue.add(lastAdded);
                lastAdded++;
            }
        
            // If no process is in the queue, jump to the next process arrival time
            if (queue.isEmpty()) {
                currentTime = Arrival_time[lastAdded]; 
                continue;
            }
        
            // Pick the next process from the queue
            int idx = queue.poll();
            
            // If this is the first time this process is being executed
            if (isFirstResponse[idx]) {
                Response_time[idx] = currentTime - Arrival_time[idx];
                isFirstResponse[idx] = false;
            }
        
            // Execute the process for a time quantum or until it finishes
            int execTime = Math.min(timeQuantum, Remaining_burst_time[idx]);
            currentTime += execTime;
            Remaining_burst_time[idx] -= execTime;
        
            // Re-add the process to the queue if it is not completed
            while (lastAdded < n && Arrival_time[lastAdded] <= currentTime) {
                queue.add(lastAdded);
                lastAdded++;
            }
        
            // If process is not completed, it goes back to the queue
            if (Remaining_burst_time[idx] > 0) {
                queue.add(idx);
            } else {
                // Process has completed
                Completion_time[idx] = currentTime;
                Turnaround_time[idx] = Completion_time[idx] - Arrival_time[idx];
                Waiting_time[idx] = Turnaround_time[idx] - Burst_time[idx];
                completed++;
            }
        }
    
        // Display the results for Round Robin
        displayResults(n, choice, Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time);
    }
    

    private static void priorityScheduling(Scanner sc) {
        for (int i = 0; i < n; i++) {
            System.out.print("Enter priority of P" + Process_id[i] + ": ");
            Priority[i] = sc.nextInt();
        }
        System.out.print("Enter priority order (1 for higher number = higher priority, 2 for lower number = higher priority): ");
        int priorityOrder = sc.nextInt();
        
        // Use global arrays (don't redefine)
        int[] Remaining_time = Arrays.copyOf(Remaining_burst_time, n); // Copy remaining burst time
        int[] Completion_time = new int[n];
        int[] Turnaround_time = new int[n];
        int[] Waiting_time = new int[n];
        int[] Response_time = new int[n];
        
        int currentTime = 0, completed = 0;

        while (completed < n) {
            int idx = -1;
            int bestPriority = (priorityOrder == 1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;

            // Select the process with the highest priority
            for (int i = 0; i < n; i++) {
                if (Arrival_time[i] <= currentTime && Remaining_time[i] > 0) {
                    if ((priorityOrder == 1 && Priority[i] > bestPriority) || 
                        (priorityOrder == 2 && Priority[i] < bestPriority) ||
                        (Priority[i] == bestPriority && Arrival_time[i] < Arrival_time[idx])) { 
                        bestPriority = Priority[i];
                        idx = i;
                    }
                }
            }

            if (idx == -1) {
                // If no process is ready, increment time to the next arrival
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (Remaining_time[i] > 0 && Arrival_time[i] > currentTime) {
                        nextArrival = Math.min(nextArrival, Arrival_time[i]);
                    }
                }
                currentTime = nextArrival;
            } else {
                if (isFirstResponse[idx]) {
                    Response_time[idx] = currentTime - Arrival_time[idx];
                    isFirstResponse[idx] = false;
                }

                Remaining_time[idx]--;
                currentTime++;

                if (Remaining_time[idx] == 0) {
                    Completion_time[idx] = currentTime;
                    Turnaround_time[idx] = Completion_time[idx] - Arrival_time[idx];
                    Waiting_time[idx] = Turnaround_time[idx] - Burst_time[idx];
                    completed++;
                }
            }
        }

        displayResults(n, choice, Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time);
    }
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}