import java.util.*;

public class IntelligentCpuScheduleSimulator {

    static int n;  // Number of processes
    static int[] Process_id;
    static int[] Arrival_time;
    static int[] Burst_time;
    static int[] Priority;
    static int[] Completion_time;
    static int[] Turnaround_time;
    static int[] Waiting_time;
    static int[] Response_time;
    static int[] Remaining_burst_time;
    static boolean[] isFirstResponse;
    static boolean[] isCompleted;

    static List<String> bestAlgos = new ArrayList<>();
    static List<SchedulingResult> bestAlgoResults = new ArrayList<>();
    static double bestAWT = Double.MAX_VALUE;  // Best average waiting time

    // SchedulingResult class to hold the process information
    static class SchedulingResult {
        int[] Process_id;
        int[] Arrival_time;
        int[] Burst_time;
        int[] Completion_time;
        int[] Turnaround_time;
        int[] Waiting_time;
        int[] Response_time;

        SchedulingResult(int[] Process_id, int[] Arrival_time, int[] Burst_time, int[] Completion_time, int[] Turnaround_time, int[] Waiting_time, int[] Response_time) {
            this.Process_id = Process_id;
            this.Arrival_time = Arrival_time;
            this.Burst_time = Burst_time;
            this.Completion_time = Completion_time;
            this.Turnaround_time = Turnaround_time;
            this.Waiting_time = Waiting_time;
            this.Response_time = Response_time;
        }
    }

    // Method to calculate the scheduling algorithm results
    private static void calculate(int[] Process_id, int[] Arrival_time, int[] Burst_time, int[] Completion_time, int[] Turnaround_time, int[] Waiting_time, int[] Response_time, String algorithmName) {
        double totalWT = 0;
        double totalTAT = 0;
    
        for (int i = 0; i < n; i++) {
            totalWT += Waiting_time[i];
            totalTAT += Turnaround_time[i];
        }
    
        double avgWT = totalWT / n;
        double avgTAT = totalTAT / n;
    
        System.out.println("Average Waiting Time for " + algorithmName + ": " + avgWT);
        System.out.println("Average Turnaround Time for " + algorithmName + ": " + avgTAT + "\n");
    
        // If a new best AWT is found, update the best results
        if (avgWT < bestAWT) {
            bestAWT = avgWT;
            bestAlgos.clear();  // Clear previous best algorithms as we found a new best one
            bestAlgos.add(algorithmName);  // Add current algorithm to bestAlgos list
    
            // Store the current algorithm's process info
            bestAlgoResults.clear();
            bestAlgoResults.add(new SchedulingResult(Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time));
        } else if (avgWT == bestAWT) {
            bestAlgos.add(algorithmName);  // If AWT is the same, add this algorithm too
    
            // Store this algorithm's process info
            bestAlgoResults.add(new SchedulingResult(Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time));
        }
    }
    

    // Method to display the results from the best scheduling algorithm
    private static void displayResults(SchedulingResult result) {
        int[] Process_id = result.Process_id;
        int[] Arrival_time = result.Arrival_time;
        int[] Burst_time = result.Burst_time;
        int[] Completion_time = result.Completion_time;
        int[] Turnaround_time = result.Turnaround_time;
        int[] Waiting_time = result.Waiting_time;
        int[] Response_time = result.Response_time;

        System.out.print("---------------------------------------------------------");
        System.out.println("\nProcess\tAT\tBT\tCT\tTAT\tWT\tRT");
        System.out.println("---------------------------------------------------------");
        for (int i = 0; i < n; i++) {
            System.out.println("P" + Process_id[i] + "\t" + Arrival_time[i] + "\t" + Burst_time[i] + "\t" + Completion_time[i] + "\t" + Turnaround_time[i] + "\t" + Waiting_time[i] + "\t" + Response_time[i]);
        }
        System.out.println("---------------------------------------------------------");
    }

    // Method to take inputs from the user
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

    // Helper method to swap values in an array
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
        System.out.println("\nFCFS Scheduling Results:");
        System.out.println("---------------------------------------------------------");
        System.out.println("Process\tAT\tBT\tCT\tTAT\tWT\tRT");
        System.out.println("---------------------------------------------------------");
        
        for (int i = 0; i < n; i++) {
            System.out.println("P" + Process_id[i] + "\t" + Arrival_time[i] + "\t" + Burst_time[i] + "\t" + Completion_time[i] + "\t" + Turnaround_time[i] + "\t" + Waiting_time[i] + "\t" + Response_time[i]);
        }
    
        System.out.println("---------------------------------------------------------");
        calculate(Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time, "FCFS");
    }
    private static void sjfNonPreemptive() {
        // SJF Non-Preemptive Scheduling Logic
        int[] Completion_time = new int[n];
        int[] Turnaround_time = new int[n];
        int[] Waiting_time = new int[n];
        int[] Response_time = new int[n];
        boolean[] isCompleted = new boolean[n];
        int[] burstTimeCopy = Arrays.copyOf(Burst_time, n);  // Copy Burst time to preserve the original
        
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;
        Arrays.sort(order, Comparator.comparingInt(i -> Arrival_time[i]));
    
        int currentTime = 0, completed = 0;
    
        while (completed < n) {
            int idx = -1, minBurstTime = Integer.MAX_VALUE;
    
            for (int i = 0; i < n; i++) {
                if (!isCompleted[i] && Arrival_time[i] <= currentTime && burstTimeCopy[i] < minBurstTime) {
                    minBurstTime = burstTimeCopy[i];
                    idx = i;
                }
            }
    
            if (idx == -1) {
                currentTime++;
            } else {
                Completion_time[idx] = currentTime + burstTimeCopy[idx];
                currentTime = Completion_time[idx];
    
                Turnaround_time[idx] = Completion_time[idx] - Arrival_time[idx];
                Waiting_time[idx] = Turnaround_time[idx] - Burst_time[idx];
                Response_time[idx] = Waiting_time[idx];
    
                isCompleted[idx] = true;
                completed++;
            }
        }
    
        System.out.println("\nSJF(Non-Preemptive) Scheduling Results:");
        System.out.println("---------------------------------------------------------");
        System.out.println("Process\tAT\tBT\tCT\tTAT\tWT\tRT");
        System.out.println("---------------------------------------------------------");
        
        for (int i = 0; i < n; i++) {
            System.out.println("P" + Process_id[i] + "\t" + Arrival_time[i] + "\t" + Burst_time[i] + "\t" + Completion_time[i] + "\t" + Turnaround_time[i] + "\t" + Waiting_time[i] + "\t" + Response_time[i]);
        }
    
        System.out.println("---------------------------------------------------------");
        calculate(Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time, "SJF (Non-Preemptive)");
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
        int[] burstTimeCopy = Arrays.copyOf(Burst_time, n);  // Copy Burst time to preserve the original
    
        for (int i = 0; i < n; i++) {
            Remaining_burst_time[i] = burstTimeCopy[i];
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
    
        System.out.println("\nSJF(Preemptive) Scheduling Results:");
        System.out.println("---------------------------------------------------------");
        System.out.println("Process\tAT\tBT\tCT\tTAT\tWT\tRT");
        System.out.println("---------------------------------------------------------");
        
        for (int i = 0; i < n; i++) {
            System.out.println("P" + Process_id[i] + "\t" + Arrival_time[i] + "\t" + Burst_time[i] + "\t" + Completion_time[i] + "\t" + Turnaround_time[i] + "\t" + Waiting_time[i] + "\t" + Response_time[i]);
        }
    
        System.out.println("---------------------------------------------------------");
        calculate(Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time, "SJF (Preemptive)");
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
    
        System.arraycopy(Burst_time, 0, Remaining_burst_time, 0, n);
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
    
        System.out.println("\nRound Robin Scheduling Results:");
        System.out.println("---------------------------------------------------------");
        System.out.println("Process\tAT\tBT\tCT\tTAT\tWT\tRT");
        System.out.println("---------------------------------------------------------");
        
        for (int i = 0; i < n; i++) {
            System.out.println("P" + Process_id[i] + "\t" + Arrival_time[i] + "\t" + Burst_time[i] + "\t" + Completion_time[i] + "\t" + Turnaround_time[i] + "\t" + Waiting_time[i] + "\t" + Response_time[i]);
        }
    
        System.out.println("---------------------------------------------------------");
        calculate(Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time, "Round Robin");
    }
    private static void priorityScheduling(Scanner sc) {
        // Reset isFirstResponse for each process
        boolean[] isFirstResponse = new boolean[n];
        for (int i = 0; i < n; i++) {
            System.out.print("Enter priority of P" + Process_id[i] + ": ");
            Priority[i] = sc.nextInt();
            Remaining_burst_time[i] = Burst_time[i];  // Reset Remaining time to Burst time
            isFirstResponse[i] = true;  // Reset the response flag
        }
    
        System.out.print("Enter priority order (1 for higher number = higher priority, 2 for lower number = higher priority): ");
        int priorityOrder = sc.nextInt();
    
        // Arrays to store completion, turnaround, waiting, and response times
        int[] Completion_time = new int[n];
        int[] Turnaround_time = new int[n];
        int[] Waiting_time = new int[n];
        int[] Response_time = new int[n];
    
        int currentTime = 0, completed = 0;
    
        // Sorting by Arrival Time first and then by Priority for the same arrival time
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) order[i] = i;
        Arrays.sort(order, (i1, i2) -> {
            if (Arrival_time[i1] == Arrival_time[i2]) {
                return Integer.compare(Priority[i2], Priority[i1]);  // Higher priority first if same arrival time
            }
            return Integer.compare(Arrival_time[i1], Arrival_time[i2]);
        });
    
        while (completed < n) {
            int idx = -1;
            int bestPriority = (priorityOrder == 1) ? Integer.MIN_VALUE : Integer.MAX_VALUE;
    
            // Look for the process with the highest priority that is ready to execute
            for (int i = 0; i < n; i++) {
                int currentProcess = order[i];
                if (Arrival_time[currentProcess] <= currentTime && Remaining_burst_time[currentProcess] > 0) {
                    if ((priorityOrder == 1 && Priority[currentProcess] > bestPriority) || (priorityOrder == 2 && Priority[currentProcess] < bestPriority)) {
                        bestPriority = Priority[currentProcess];
                        idx = currentProcess;
                    }
                }
            }
    
            if (idx == -1) {
                // If no process is ready, jump to the next process arrival time
                int nextArrival = Integer.MAX_VALUE;
                for (int i = 0; i < n; i++) {
                    if (Arrival_time[i] > currentTime && Remaining_burst_time[i] > 0) {
                        nextArrival = Math.min(nextArrival, Arrival_time[i]);
                    }
                }
                currentTime = nextArrival;  // Jump to the next arrival time
            } else {
                // Process is selected, so calculate response time for the first execution
                if (isFirstResponse[idx]) {
                    Response_time[idx] = currentTime - Arrival_time[idx];
                    isFirstResponse[idx] = false;  // Mark as processed
                }
    
                // Process runs for one time unit
                Remaining_burst_time[idx]--;
                currentTime++;
    
                // Process completion
                if (Remaining_burst_time[idx] == 0) {
                    Completion_time[idx] = currentTime;
                    Turnaround_time[idx] = Completion_time[idx] - Arrival_time[idx];
                    Waiting_time[idx] = Turnaround_time[idx] - Burst_time[idx];
                    completed++;  // Increment completed processes count
                }
            }
        }
    
        System.out.println("\nPriority Scheduling Results:");
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("Process\tAT\tBT\tPriority\tCT\tTAT\tWT\tRT");
        System.out.println("-------------------------------------------------------------------------");
        
        for (int i = 0; i < n; i++) {
            System.out.println("P" + Process_id[i] + "\t" + Arrival_time[i] + "\t" + Burst_time[i] + "\t" + Priority[i] + "\t\t" + Completion_time[i] + "\t" + Turnaround_time[i] + "\t" + Waiting_time[i] + "\t" + Response_time[i]);
        }
    
        System.out.println("-------------------------------------------------------------------------");
        // Call function to display results
        calculate(Process_id, Arrival_time, Burst_time, Completion_time, Turnaround_time, Waiting_time, Response_time, "Priority Scheduling");
    }    
    
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Taking user input for processes
        takeInputs(sc);
        fcfs();
        sjfNonPreemptive();
        sjfPreemptive();
        roundRobin(sc);
        priorityScheduling(sc);
        System.out.println("\nBest Scheduling Algorithms (Lowest Average Waiting Time):");
        for (String algo : bestAlgos) {
            System.out.println(algo);
        }

        System.out.println("\nResults for the best algorithm (Average Waiting Time = " + bestAWT + "):");
        for (SchedulingResult result : bestAlgoResults) {
            displayResults(result);
        }

        sc.close();
    }
}