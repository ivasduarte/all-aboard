# US13 Performance Visualization
set terminal pngcairo enhanced font 'Arial,12' size 1200,800
set output 'us13_performance.png'

set title 'US13 Performance: Execution Time vs Graph Size' font 'Arial,16'
set xlabel 'Number of Edges' font 'Arial,14'
set ylabel 'Average Execution Time (ms)' font 'Arial,14'
set grid
set key outside right
set autoscale

set datafile separator ','

plot 'us13_results.csv' every ::1 using 4:5 with points pt 7 ps 1
