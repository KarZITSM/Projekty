module random_generator2(
    input rst,
    input clk, 
    input ena,
	 input start,
    output [7:0] valuea,
	 output rdy_random
);

reg    [7:0] lfsr_reg;
wire [7:0] lfsr_next;
wire    feedback;
reg done;
reg [3:0] count;


always@(posedge clk, posedge rst) begin
    if(rst) begin
        lfsr_reg<=8'b10010111;
		  count <= 10;
		  done <= 0;
		end else if(ena && start) begin
        lfsr_reg <= lfsr_next;
		  count <= count - 1;
			if(count) done <= 0;
				else done <=1;
		  end
	
end

assign feedback = lfsr_reg[7] ^ lfsr_reg[5] ^ lfsr_reg[4] ^ lfsr_reg[3];
assign lfsr_next = {lfsr_reg[6:0],feedback};

//always @(posedge clk or posedge rst) begin
//    if (rst)
//        done <= 1'b0;
//    else if (lfsr_reg != 8'b10101101) // Sprawdzamy, czy lfsr_reg osiągnął wartość 0
//        done <= 1'b1;
//    else
//        done <= 1'b0;
//end

assign valuea = lfsr_reg;
assign rdy_random = done;

endmodule 