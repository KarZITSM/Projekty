module primenumber(
  input clk,
  input [7:0] index,
  input start,
  output reg [7:0] data_out,
  output rdy_prime
);

reg [7:0] array [0:52];
reg [5:0] randomindex;
reg done;

// Initialize the array
initial begin
		  randomindex = index % 53;
		  array[0] = 8'b00000010;
        array[1] = 8'b00000011;
        array[2] = 8'b00000101;
        array[3] = 8'b00000111;
        array[4] = 8'b00001011;
        array[5] = 8'b00001101;
        array[6] = 8'b00010001;
        array[7] = 8'b00010011;
        array[8] = 8'b00010111;
        array[9] = 8'b00011101;
        array[10] = 8'b00011111;
        array[11] = 8'b00100101;
        array[12] = 8'b00101001;
        array[13] = 8'b00101011;
        array[14] = 8'b00101111;
        array[15] = 8'b00110101;
        array[16] = 8'b00111011;
        array[17] = 8'b00111101;
        array[18] = 8'b01000011;
        array[19] = 8'b01000111;
        array[20] = 8'b01001001;
        array[21] = 8'b01001111;
        array[22] = 8'b01010011;
        array[23] = 8'b01011001;
        array[24] = 8'b01100001;
        array[25] = 8'b01100101;
        array[26] = 8'b01100111;
        array[27] = 8'b01101011;
        array[28] = 8'b01101101;
        array[29] = 8'b01110001;
        array[30] = 8'b01111111;
        array[31] = 8'b10000011;
        array[32] = 8'b10001001;
        array[33] = 8'b10001011;
        array[34] = 8'b10010101;
        array[35] = 8'b10010111;
        array[36] = 8'b10011101;
        array[37] = 8'b10100011;
        array[38] = 8'b10100111;
        array[39] = 8'b10101101;
        array[40] = 8'b10110011;
        array[41] = 8'b10110101;
        array[42] = 8'b10111111;
        array[43] = 8'b11000001;
        array[44] = 8'b11000101;
        array[45] = 8'b11000111;
        array[46] = 8'b11010011;
        array[47] = 8'b11100011;
        array[48] = 8'b11101001;
        array[49] = 8'b11101001;
        array[50] = 8'b11101111;
        array[51] = 8'b11110001;
        array[52] = 8'b11111011;
end

// Assign data_out based on the input index
always @(start) begin
randomindex = index % 53;
    case (randomindex)
        6'd0: array[0] = 8'b00000010;
        6'd1: array[1] = 8'b00000011;
        6'd2: array[2] = 8'b00000101;
        6'd3: array[3] = 8'b00000111;
        6'd4: array[4] = 8'b00001011;
        6'd5: array[5] = 8'b00001101;
        6'd6: array[6] = 8'b00010001;
        6'd7: array[7] = 8'b00010011;
        6'd8: array[8] = 8'b00010111;
        6'd9: array[9] = 8'b00011101;
        6'd10: array[10] = 8'b00011111;
        6'd11: array[11] = 8'b00100101;
        6'd12: array[12] = 8'b00101001;
        6'd13: array[13] = 8'b00101011;
        6'd14: array[14] = 8'b00101111;
        6'd15: array[15] = 8'b00110101;
        6'd16: array[16] = 8'b00111011;
        6'd17: array[17] = 8'b00111101;
        6'd18: array[18] = 8'b01000011;
        6'd19: array[19] = 8'b01000111;
        6'd20: array[20] = 8'b01001001;
        6'd21: array[21] = 8'b01001111;
        6'd22: array[22] = 8'b01010011;
        6'd23: array[23] = 8'b01011001;
        6'd24: array[24] = 8'b01100001;
        6'd25: array[25] = 8'b01100101;
        6'd26: array[26] = 8'b01100111;
        6'd27: array[27] = 8'b01101011;
        6'd28: array[28] = 8'b01101101;
        6'd29: array[29] = 8'b01110001;
        6'd30: array[30] = 8'b01111111;
        6'd31: array[31] = 8'b10000011;
        6'd32: array[32] = 8'b10001001;
        6'd33: array[33] = 8'b10001011;
        6'd34: array[34] = 8'b10010101;
        6'd35: array[35] = 8'b10010111;
        6'd36: array[36] = 8'b10011101;
        6'd37: array[37] = 8'b10100011;
        6'd38: array[38] = 8'b10100111;
        6'd39: array[39] = 8'b10101101;
        6'd40: array[40] = 8'b10110011;
        6'd41: array[41] = 8'b10110101;
        6'd42: array[42] = 8'b10111111;
        6'd43: array[43] = 8'b11000001;
        6'd44: array[44] = 8'b11000101;
        6'd45: array[45] = 8'b11000111;
        6'd46: array[46] = 8'b11010011;
        6'd47: array[47] = 8'b11100011;
        6'd48: array[48] = 8'b11101001;
        6'd49: array[49] = 8'b11101001;
        6'd50: array[50] = 8'b11101111;
        6'd51: array[51] = 8'b11110001;
        6'd52: array[52] = 8'b11111011;
        default: array[0] = 8'b00000000; // default case, if randomindex is out of range
    endcase
	data_out = array[randomindex];
	if(data_out != 0) begin
		 done = 1'b1;
	end

end

assign rdy_prime = done;


endmodule
