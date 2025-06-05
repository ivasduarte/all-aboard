# US14 Performance Visualization
set terminal pngcairo enhanced font 'Arial,12' size 1200,800
set output 'us14_performance.png'

set title 'US14 Performance: Execution Time vs Graph Size' font 'Arial,16'
set xlabel 'Number of Edges' font 'Arial,14'
set ylabel 'Average Execution Time (ms)' font 'Arial,14'
set grid
set key outside right

set datafile separator ','
set style data linespoints

plot 'us14_results.csv' using 3:4 every ::1 title 'All Data' with linespoints pt 7 ps 1 lw 2
