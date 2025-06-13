# Combined Performance Comparison
set terminal pngcairo enhanced font 'Arial,12' size 1400,1000
set output 'combined_performance.png'

set multiplot layout 2,1 title 'Performance Comparison: US13 vs US14' font 'Arial,18'

# US13 subplot
set title 'US13: Performance vs Graph Size' font 'Arial,14'
set xlabel 'Number of Edges'
set ylabel 'Avg Time (ms)'
set grid
set key top left
set autoscale
set datafile separator ','

plot 'us13_results.csv' every ::1 using 4:5 with points pt 7 ps 1

# US14 subplot
set title 'US14: Performance vs Graph Size' font 'Arial,14'
set xlabel 'Number of Edges'
set ylabel 'Avg Time (ms)'
set autoscale

plot 'us14_results.csv' every ::1 using 3:4 with points pt 5 ps 1

unset multiplot
