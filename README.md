Here is the updated **README.md** file tailored to your exact problem statement:

---

# ⚡ Intelligent CPU Scheduler Simulator

## Description

The **Intelligent CPU Scheduler Simulator** is a project aimed at simplifying and enhancing the understanding of CPU scheduling algorithms through real-time visualizations. This simulator allows users to input process details such as **arrival time**, **burst time**, and **priority**, and provides a dynamic interface to visualize Gantt charts and compute key performance metrics like **average waiting time** and **turnaround time**.

---

## Problem Statement

Efficient CPU scheduling is essential for optimizing system performance in operating systems. However, students, developers, and researchers often face challenges in comprehending how different algorithms allocate CPU time and how they affect system efficiency.

The **Intelligent CPU Scheduler Simulator** is designed to address these challenges by offering:

-   A user-friendly platform to experiment with scheduling algorithms.
-   Real-time visualizations to depict process execution.
-   Detailed performance metrics for comparison and analysis.

---

## Features

### 🔍 Key Functionalities

1. **Algorithms Supported**:

    - **First-Come, First-Served (FCFS)**
    - **Shortest Job First (SJF)**
    - **Priority Scheduling** (Preemptive and Non-Preemptive)
    - **Round Robin** (with customizable time quantum)

2. **Dynamic Input**:

    - Add, modify, or delete processes with parameters like **arrival time**, **burst time**, and **priority**.

3. **Real-Time Visualizations**:

    - Interactive **Gantt charts** to represent scheduling.
    - Performance metrics including **average waiting time**, **turnaround time**, and **throughput**.

4. **Interactive User Interface**:

    - Dropdown selection for scheduling algorithms.
    - Editable table for process details.
    - Responsive and accessible design.

5. **Performance Comparison**:
    - Compare scheduling algorithms based on computed metrics.
    - Real-time updates with each input change.

---

## Solution Architecture

### 🖥️ Frontend

-   **HTML5** and **CSS3**: For structuring and styling the user interface.
-   **JavaScript (ES6)**: For implementing scheduling logic and rendering real-time charts.
-   **Chart.js**: To display Gantt charts and performance metrics.

### 🔄 Algorithm Implementation

1. **FCFS**: Processes are executed in the order of their arrival.
2. **SJF**: Shortest burst time processes are executed first.
3. **Priority Scheduling**: Processes with higher priority are executed first.
4. **Round Robin**: Processes are executed in cyclic order with a fixed time quantum.

### Data Flow

1. User inputs process details through the interface.
2. Selected scheduling algorithm processes the data.
3. Results are displayed via Gantt charts and performance metric tables.

---

## Tech Stack

| **Component**        | **Technology**          |
| -------------------- | ----------------------- |
| **Frontend**         | HTML5, CSS3, JavaScript |
| **Charting Library** | app.js                  |
| **Algorithm Logic**  | JavaScript (ES6)        |
| **Code Editor**      | VS code                 |

---

## Installation & Setup

1. Clone the repository:

    ```bash
    git clone https://github.com/Utkal9/Intelligent-CPU-Scheduler-Simulator.git
    cd Intelligent-CPU-Scheduler-Simulator
    ```

2. Open the `index.html` file in your preferred browser.

3. Start simulating and analyzing CPU scheduling algorithms!

---

## How to Use

1. **Input Processes**:

    - Add process details such as **arrival time**, **burst time**, and **priority** using the input form.

2. **Select Scheduling Algorithm**:

    - Choose one of the supported algorithms from the dropdown menu.

3. **Visualize Results**:

    - View the Gantt chart and detailed performance metrics on the results panel.

4. **Experiment & Analyze**:
    - Modify process inputs or switch between algorithms to observe different outcomes.

---

## Screenshots

### 🔹 Input Form

![Input Form](/Graphical%20UI/assets/input-form.png)

### 🔹 Gantt Chart

## ![Gantt Chart](/Graphical%20UI/assets/gantt-chart.png)

## Future Enhancements

-   Add **Preemptive SJF** and **Round Robin** support.
-   Include export options for saving Gantt charts and metrics as PDF or Excel files.
-   Enable dark mode for accessibility.
-   Introduce a tutorial mode for beginners to learn scheduling algorithms step by step.

---

## License

This project is licensed under the **MIT License**.

---
