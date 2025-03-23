document
    .getElementById("generateProcesses")
    .addEventListener("click", generateProcessInputs);
document.getElementById("fcfsButton").addEventListener("click", runFCFS);
document
    .getElementById("sjfNonPreemptiveButton")
    .addEventListener("click", runSJFNonPreemptive);
document
    .getElementById("sjfPreemptiveButton")
    .addEventListener("click", runSJFPreemptive);
document
    .getElementById("roundRobinButton")
    .addEventListener("click", runRoundRobin);
document
    .getElementById("priorityButton")
    .addEventListener("click", runPriorityScheduling);
document
    .getElementById("compareAlgorithmsButton")
    .addEventListener("click", compareAlgorithms);
const navbar = document.querySelector(".navbar");

window.addEventListener("scroll", () => {
    if (window.scrollY > 0) {
        navbar.style.boxShadow = "0 4px 6px rgba(0, 0, 0, 0.1)";
    } else {
        navbar.style.boxShadow = "none";
    }
});

const toggleIcon = document.getElementById("darkModeToggle");
const body = document.body;

toggleIcon.addEventListener("click", () => {
    body.classList.toggle("dark-mode");
    const isDarkMode = body.classList.contains("dark-mode");
    toggleIcon.textContent = isDarkMode ? "☀️" : "🌙";
    localStorage.setItem("darkMode", isDarkMode ? "enabled" : "disabled");
});

window.addEventListener("DOMContentLoaded", () => {
    const darkModeSetting = localStorage.getItem("darkMode");
    if (darkModeSetting === "enabled") {
        body.classList.add("dark-mode");
        toggleIcon.textContent = "☀️";
    }
});
const teamMembers = document.querySelectorAll(".team-member");

const observer = new IntersectionObserver(
    (entries) => {
        entries.forEach((entry) => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = 1;
                entry.target.style.transform = "translateY(0)";
            }
        });
    },
    { threshold: 0.1 }
);

teamMembers.forEach((member) => {
    member.style.opacity = 0;
    member.style.transform = "translateY(20px)";
    member.style.transition = "opacity 0.5s ease, transform 0.5s ease";
    observer.observe(member);
});

let processes = [];

function generateProcessInputs() {
    const numProcesses = parseInt(
        document.getElementById("numProcesses").value
    );
    const processInputsDiv = document.getElementById("processInputs");

    if (isNaN(numProcesses) || numProcesses <= 0) {
        alert("Please enter a valid number of processes.");
        return;
    }

    processInputsDiv.innerHTML = "";
    processes = [];

    for (let i = 0; i < numProcesses; i++) {
        const processDiv = document.createElement("div");
        processDiv.classList.add("process");

        processDiv.innerHTML = `
                    <h3>Process ${i + 1}</h3>
                    <label>Arrival Time:</label>
                    <input type="number" class="arrivalTime" placeholder="Enter Arrival Time">
                    <label>Burst Time:</label>
                    <input type="number" class="burstTime" placeholder="Enter Burst Time">
                `;
        processInputsDiv.appendChild(processDiv);
    }
}

function extractProcessData() {
    const arrivalTimes = document.querySelectorAll(".arrivalTime");
    const burstTimes = document.querySelectorAll(".burstTime");

    processes = [];
    for (let i = 0; i < arrivalTimes.length; i++) {
        const arrivalTime = parseInt(arrivalTimes[i].value);
        const burstTime = parseInt(burstTimes[i].value);

        if (isNaN(arrivalTime) || isNaN(burstTime) || burstTime <= 0) {
            alert(`Invalid data for Process ${i + 1}.`);
            return null;
        }

        processes.push({
            id: i + 1,
            arrivalTime,
            burstTime,
            remainingTime: burstTime,
        });
    }
    return processes;
}

function runFCFS() {
    const data = extractProcessData();
    if (!data) return;

    processes.sort((a, b) => a.arrivalTime - b.arrivalTime);

    let currentTime = 0;
    const results = processes.map((p) => {
        if (currentTime < p.arrivalTime) {
            currentTime = p.arrivalTime;
        }
        const completionTime = currentTime + p.burstTime;
        const turnaroundTime = completionTime - p.arrivalTime;
        const waitingTime = turnaroundTime - p.burstTime;

        currentTime = completionTime;

        return {
            ...p,
            completionTime,
            turnaroundTime,
            waitingTime,
            responseTime: waitingTime,
        };
    });

    displayResults(results);
    return results;
}

function runSJFNonPreemptive() {
    const data = extractProcessData();
    if (!data) return;

    let currentTime = 0;
    const remainingProcesses = [...processes];
    const results = [];

    while (remainingProcesses.length > 0) {
        const availableProcesses = remainingProcesses.filter(
            (p) => p.arrivalTime <= currentTime
        );
        if (availableProcesses.length === 0) {
            currentTime++;
            continue;
        }

        availableProcesses.sort((a, b) => a.burstTime - b.burstTime);
        const nextProcess = availableProcesses[0];

        const completionTime = currentTime + nextProcess.burstTime;
        const turnaroundTime = completionTime - nextProcess.arrivalTime;
        const waitingTime = turnaroundTime - nextProcess.burstTime;

        results.push({
            ...nextProcess,
            completionTime,
            turnaroundTime,
            waitingTime,
            responseTime: waitingTime,
        });

        currentTime = completionTime;
        remainingProcesses.splice(remainingProcesses.indexOf(nextProcess), 1);
    }

    displayResults(results);
    return results;
}

function runSJFPreemptive() {
    const data = extractProcessData();
    if (!data) return;

    processes.sort((a, b) => a.arrivalTime - b.arrivalTime);

    const n = processes.length;
    const remainingBurstTime = processes.map((p) => p.burstTime);
    const completionTime = Array(n).fill(0);
    const turnaroundTime = Array(n).fill(0);
    const waitingTime = Array(n).fill(0);
    const responseTime = Array(n).fill(-1);

    let currentTime = 0;
    let completed = 0;

    while (completed < n) {
        let idx = -1;
        let minBurstTime = Infinity;

        for (let i = 0; i < n; i++) {
            if (
                processes[i].arrivalTime <= currentTime &&
                remainingBurstTime[i] > 0 &&
                remainingBurstTime[i] < minBurstTime
            ) {
                minBurstTime = remainingBurstTime[i];
                idx = i;
            }
        }

        if (idx === -1) {
            currentTime++;
        } else {
            if (responseTime[idx] === -1) {
                responseTime[idx] = currentTime - processes[idx].arrivalTime;
            }

            remainingBurstTime[idx]--;
            currentTime++;

            if (remainingBurstTime[idx] === 0) {
                completed++;
                completionTime[idx] = currentTime;
                turnaroundTime[idx] =
                    completionTime[idx] - processes[idx].arrivalTime;
                waitingTime[idx] =
                    turnaroundTime[idx] - processes[idx].burstTime;
            }
        }
    }

    const results = processes.map((p, i) => ({
        id: p.id,
        arrivalTime: p.arrivalTime,
        burstTime: p.burstTime,
        completionTime: completionTime[i],
        turnaroundTime: turnaroundTime[i],
        waitingTime: waitingTime[i],
        responseTime: responseTime[i],
    }));

    displayResults(results);
    return results;
}

function runRoundRobin() {
    const data = extractProcessData();
    if (!data) return;

    const timeQuantum = parseInt(document.getElementById("timeQuantum").value);
    if (isNaN(timeQuantum) || timeQuantum <= 0) {
        alert("Please enter a valid time quantum.");
        return;
    }

    const processes = data.map((process, index) => ({
        id: process.id,
        arrivalTime: process.arrivalTime,
        burstTime: process.burstTime,
        remainingTime: process.burstTime,
        completionTime: 0,
        turnaroundTime: 0,
        waitingTime: 0,
        responseTime: -1,
    }));

    let currentTime = 0;
    const queue = [];
    const results = [];
    let completed = 0;

    processes.sort((a, b) => a.arrivalTime - b.arrivalTime);
    let lastAddedIndex = 0;

    while (completed < processes.length) {
        while (
            lastAddedIndex < processes.length &&
            processes[lastAddedIndex].arrivalTime <= currentTime
        ) {
            queue.push(processes[lastAddedIndex]);
            lastAddedIndex++;
        }

        if (queue.length === 0) {
            currentTime++;
            continue;
        }

        const currentProcess = queue.shift();

        if (currentProcess.responseTime === -1) {
            currentProcess.responseTime =
                currentTime - currentProcess.arrivalTime;
        }

        const executionTime = Math.min(
            timeQuantum,
            currentProcess.remainingTime
        );
        currentProcess.remainingTime -= executionTime;
        currentTime += executionTime;

        while (
            lastAddedIndex < processes.length &&
            processes[lastAddedIndex].arrivalTime <= currentTime
        ) {
            queue.push(processes[lastAddedIndex]);
            lastAddedIndex++;
        }

        if (currentProcess.remainingTime > 0) {
            queue.push(currentProcess);
        } else {
            completed++;
            currentProcess.completionTime = currentTime;
            currentProcess.turnaroundTime =
                currentProcess.completionTime - currentProcess.arrivalTime;
            currentProcess.waitingTime =
                currentProcess.turnaroundTime - currentProcess.burstTime;

            results.push({
                id: currentProcess.id,
                arrivalTime: currentProcess.arrivalTime,
                burstTime: currentProcess.burstTime,
                completionTime: currentProcess.completionTime,
                turnaroundTime: currentProcess.turnaroundTime,
                waitingTime: currentProcess.waitingTime,
                responseTime: currentProcess.responseTime,
            });
        }
    }

    displayResults(results);
    return results;
}

function runPriorityScheduling() {
    const data = extractProcessData();
    if (!data) return;

    // Prompt user for priorities and priority order
    processes.forEach((process) => {
        process.priority = parseInt(
            prompt(`Enter priority of P${process.id}:`),
            10
        );
        process.remainingBurstTime = process.burstTime; // Initialize remaining burst time
        process.isFirstResponse = true; // Track first response
    });

    const priorityOrder = parseInt(
        prompt(
            "Enter priority order (1 for higher number = higher priority, 2 for lower number = higher priority):"
        ),
        10
    );

    const n = processes.length;
    const completionTime = Array(n).fill(0);
    const turnaroundTime = Array(n).fill(0);
    const waitingTime = Array(n).fill(0);
    const responseTime = Array(n).fill(-1);
    let currentTime = 0;
    let completed = 0;

    // Sort processes by Arrival Time, and for same Arrival Time, by Priority
    processes.sort((a, b) => {
        if (a.arrivalTime === b.arrivalTime) {
            return priorityOrder === 1
                ? b.priority - a.priority
                : a.priority - b.priority; // Higher or lower priority
        }
        return a.arrivalTime - b.arrivalTime;
    });

    while (completed < n) {
        let idx = -1;
        let bestPriority = priorityOrder === 1 ? -Infinity : Infinity;

        // Find process with the best priority that has arrived
        for (let i = 0; i < n; i++) {
            if (
                processes[i].arrivalTime <= currentTime &&
                processes[i].remainingBurstTime > 0 &&
                ((priorityOrder === 1 &&
                    processes[i].priority > bestPriority) ||
                    (priorityOrder === 2 &&
                        processes[i].priority < bestPriority))
            ) {
                bestPriority = processes[i].priority;
                idx = i;
            }
        }

        if (idx === -1) {
            // No process is ready, jump to the next process arrival time
            const nextArrival = Math.min(
                ...processes
                    .filter(
                        (p) =>
                            p.remainingBurstTime > 0 &&
                            p.arrivalTime > currentTime
                    )
                    .map((p) => p.arrivalTime)
            );
            currentTime = nextArrival;
        } else {
            // Process is selected, calculate response time if first execution
            if (responseTime[idx] === -1) {
                responseTime[idx] = currentTime - processes[idx].arrivalTime;
            }

            // Process executes for one unit of time
            processes[idx].remainingBurstTime--;
            currentTime++;

            // Process completion
            if (processes[idx].remainingBurstTime === 0) {
                completionTime[idx] = currentTime;
                turnaroundTime[idx] =
                    completionTime[idx] - processes[idx].arrivalTime;
                waitingTime[idx] =
                    turnaroundTime[idx] - processes[idx].burstTime;
                completed++;
            }
        }
    }

    // Prepare results for display
    const results = processes.map((p, i) => ({
        id: p.id,
        arrivalTime: p.arrivalTime,
        burstTime: p.burstTime,
        priority: p.priority,
        completionTime: completionTime[i],
        turnaroundTime: turnaroundTime[i],
        waitingTime: waitingTime[i],
        responseTime: responseTime[i],
    }));

    displayResults(results);
    return results;
}

function compareAlgorithms() {
    const fcfsResults = runFCFS();
    const sjfNonPreemptiveResults = runSJFNonPreemptive();
    const sjfPreemptiveResults = runSJFPreemptive();
    const roundRobinResults = runRoundRobin();
    const priorityResults = runPriorityScheduling();

    const allResults = [
        { algorithm: "FCFS", results: fcfsResults },
        { algorithm: "SJF Non-Preemptive", results: sjfNonPreemptiveResults },
        { algorithm: "SJF Preemptive", results: sjfPreemptiveResults },
        { algorithm: "Round Robin", results: roundRobinResults },
        { algorithm: "Priority Scheduling", results: priorityResults },
    ];

    const validResults = allResults.filter(
        (result) => result.results && result.results.length > 0
    );

    if (validResults.length === 0) {
        alert("No results to compare.");
        return;
    }

    validResults.forEach((algorithm) => {
        algorithm.avgWaitingTime =
            algorithm.results.reduce((sum, p) => sum + p.waitingTime, 0) /
            algorithm.results.length;

        algorithm.avgTurnaroundTime =
            algorithm.results.reduce((sum, p) => sum + p.turnaroundTime, 0) /
            algorithm.results.length;
    });

    const minAvgWaitingTime = Math.min(
        ...validResults.map((algo) => algo.avgWaitingTime)
    );

    const minAvgTurnaroundTime = Math.min(
        ...validResults.map((algo) => algo.avgTurnaroundTime)
    );

    const bestAlgorithms = validResults.filter(
        (algo) =>
            algo.avgWaitingTime === minAvgWaitingTime &&
            algo.avgTurnaroundTime === minAvgTurnaroundTime
    );

    displayBestAlgorithm(bestAlgorithms);
}

function displayBestAlgorithm(bestAlgorithms) {
    const resultsTable = document.getElementById("bestAlgorithmResults");
    let htmlContent = `
        <br><br>
        <h3>Best Algorithm(s):</h3>
    `;

    bestAlgorithms.forEach((bestAlgorithm) => {
        htmlContent += `
            <br>
            <h4>${bestAlgorithm.algorithm}</h4>
            <table class="results-table">
                <thead>
                    <tr>
                        <th>Process</th>
                        <th>Arrival Time</th>
                        <th>Burst Time</th>
                        <th>Completion Time</th>
                        <th>Turnaround Time</th>
                        <th>Waiting Time</th>
                        <th>Response Time</th>
                    </tr>
                </thead>
                <tbody>
                    ${bestAlgorithm.results
                        .map(
                            (p) =>
                                `<tr>
                                    <td>P${p.id}</td>
                                    <td>${p.arrivalTime}</td>
                                    <td>${p.burstTime}</td>
                                    <td>${p.completionTime}</td>
                                    <td>${p.turnaroundTime}</td>
                                    <td>${p.waitingTime}</td>
                                    <td>${p.responseTime}</td>
                                </tr>`
                        )
                        .join("")}
                </tbody>
            </table>
            <br><br>
            <h4>Average Waiting Time: ${bestAlgorithm.avgWaitingTime.toFixed(
                2
            )}</h4>
            <h4>Average Turnaround Time: ${bestAlgorithm.avgTurnaroundTime.toFixed(
                2
            )}</h4>
        `;
    });

    resultsTable.innerHTML = htmlContent;
}

function displayResults(results) {
    const totalWaitingTime = results.reduce((sum, p) => sum + p.waitingTime, 0);
    const avgWaitingTime = (totalWaitingTime / results.length).toFixed(2);

    const totalTurnaroundTime = results.reduce(
        (sum, p) => sum + p.turnaroundTime,
        0
    );
    const avgTurnaroundTime = (totalTurnaroundTime / results.length).toFixed(2);
    const resultsTable = document.getElementById("resultsTable");
    resultsTable.innerHTML = `
                <table class="results-table">
                    <thead>
                        <tr>
                            <th>Process</th>
                            <th>Arrival Time</th>
                            <th>Burst Time</th>
                            <th>Completion Time</th>
                            <th>Turnaround Time</th>
                            <th>Waiting Time</th>
                            <th>Response Time</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${results
                            .map(
                                (p) =>
                                    `<tr>
                                        <td>P${p.id}</td>
                                        <td>${p.arrivalTime}</td>
                                        <td>${p.burstTime}</td>
                                        <td>${p.completionTime}</td>
                                        <td>${p.turnaroundTime}</td>
                                        <td>${p.waitingTime}</td>
                                        <td>${p.responseTime}</td>
                                    </tr>`
                            )
                            .join("")}
                    </tbody>
                </table>
                <br><br>
                <h4>Average Waiting Time: ${avgWaitingTime}</h4>
                <h4>Average Turnaround Time: ${avgTurnaroundTime}</h4>
            `;
}
