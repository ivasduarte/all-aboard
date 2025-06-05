# Combined Performance Comparison - Simplified
set terminal pngcairo enhanced font 'Arial,12' size 1400,1000
set output 'combined_performance.png'

set multiplot layout 2,1 title 'Performance Comparison: US13 vs US14' font 'Arial,18'

# US13 subplot
set title 'US13: Performance vs Graph Size' font 'Arial,14'
set xlabel 'Number of Edges'
set ylabel 'Avg Time (ms)'
set grid
set key top left
set datafile separator ','

plot 'us13_results.csv' using 4:5 every ::1 title 'US13' with linespoints lw 2

# US14 subplot
set title 'US14: Performance vs Graph Size' font 'Arial,14'
set xlabel 'Number of Edges'
set ylabel 'Avg Time (ms)'

plot 'us14_results.csv' using 3:4 every ::1 title 'US14' with linespoints lw 2

unset multiplot
