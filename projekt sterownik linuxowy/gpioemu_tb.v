`timescale 1ns/100ps
module gpioemu_tb;

	reg         n_reset;
	reg [15:0]  saddress;
	reg         srd;
	reg         swr;
	reg [31:0]  sdata_in;
	reg [31:0]  gpio_in;
	reg         gpio_latch;
	reg         clk = 1;
	wire [31:0] gpio_out;
	wire [31:0] sdata_out;
	wire [31:0] gpio_in_s_insp; 
initial begin
	$dumpfile("gpioemu.vcd");
	$dumpvars(0, gpioemu_tb);
end

initial begin
    forever begin
    # 5 clk = ~clk;
    end
end

initial begin
//testy czy poprawnie liczy
    # 5 n_reset = 1;
    # 5 n_reset = 0;
    # 5 saddress = 16'h288;
    # 5 sdata_in = 32'b00000000000000000000000000000100;
    # 5 swr = 1;
    # 5 swr = 0;
	# 4000
	//kolejny taki test
	# 5 saddress = 16'h288;
    # 5 sdata_in = 32'b00000000000000000000000000010100;
    # 5 swr = 1;
    # 5 swr = 0;
	# 4000
	// próba zapisania pod zły adres
	# 5 saddress = 16'h100;
	# 5 sdata_in = 32'b00000000000000000000000000010100;
	# 5 swr = 1;
	# 5 swr = 0;
	# 1000;
	//Odczytanie rejestru W
	# 50 saddress = 16'h298;
	# 50 srd = 1;
	# 5 srd = 0;
	//Odczytanie rejestruS
    # 5 saddress = 16'h288;
    # 5 sdata_in = 32'b00000000000000000000000000000100;
    # 5 swr = 1;
    # 5 swr = 0;
	# 50 saddress = 16'h2A0;
	# 50 srd = 1;
	# 5 srd = 0;
	# 4000
	//kolejny taki test
	# 5 saddress = 16'h288;
    # 5 sdata_in = 32'b00000000000000000000000000010100;
    # 5 swr = 1;
    # 5 swr = 0;
	//Odczytanie rejestru A
	# 50 saddress = 16'h288;
	# 50 srd = 1;
	# 5 srd = 0;
	# 4000
	//Próba odczytania ze złego rejestru
	# 50 saddress = 16'h124;
	# 50 srd = 1;
	# 5 srd = 0;
	
	
	
	
    # 2000 $finish;
end

gpioemu dut (n_reset, saddress, srd, swr, sdata_in, sdata_out, gpio_in, gpio_latch, gpio_out, clk, gpio_in_s_insp);

endmodule
