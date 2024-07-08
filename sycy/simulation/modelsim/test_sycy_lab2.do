restart -nowave -force
add wave -radix unsigned *
force clk 0 0, 1 10 -r 20
force rst 1 0, 0 1
force ena 1
force start 1
force message_from_drone 0010010100100110001001110010000000100001001000100010001100101100

run 5000
