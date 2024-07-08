module cc (
input initiate,
input rst,
input clk,
input ena,
output [7:0] g,
output [7:0] p,
output [7:0] A,
);

random_generator g_1 (
		.rst	 (rst),
		.clk	 (clk),
		.ena	 (1'b1),
		.valuea(a)	
);
random_generator g_2 (
		.rst	 (rst),
		.clk	 (clk),
		.ena	 (1'b1),
		.valuea(g)	
);

primenumber p_1 (
		.clk	   (clk),
		.index   (a),
		.data_out(p),
);

powermod A_1 (
		.rst	 (rst),
		.clk	 (clk),
		.ena	 (1'b1),
		.start (initiate),
		.a		 (g),
		.b		 (a),
		.m		 (p),
		.res   (A_part_key),
);


endmodule