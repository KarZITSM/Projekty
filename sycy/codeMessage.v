module codeMessage (
    input clk, 
    input ena,
	 input [63:0] letter,
	 input [7:0] key,
	 input confirm,
	 output reg [63:0] codedMessage,
	 output done
);

reg [7:0] tempCode;
reg done_reg;
reg [63:0]long_key;
//reg [7:0] currentLetter;

initial begin 
	codedMessage <= 64'b0; // reset codedMessage to 0
	tempCode <= 8'b0; // reset tempCode to 0

end 


always @(posedge clk) begin
		long_key <= {key, key, key, key, key, key, key, key};
        if ( confirm == 1) begin
				//currentLetter <= letter; 
            codedMessage <= letter ^ long_key; // XOR the letter and key
            //codedMessage <= {codedMessage, tempCode}; // add the 8-bit code to codedMessage
				done_reg <= 1;
        end
    end

 assign done = done_reg;
endmodule