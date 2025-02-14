module world (
	input 		clk,
	input       ena,
	input       start,
	input       rst,
	input [63:0] message_from_drone,
	output [7:0] key,
	output [63:0] decrypted_message
);

//Zmienne do cc
//wire start_cc;
wire [63:0] encrypted_message;
wire [7:0] B_part_key_from_drone;
wire message_is_encrypted;

//Zmienne do drona
wire [7:0] A_part_key_from_drone;
wire [7:0] g_from_cc;
wire [7:0] p_from_cc;
wire values_are_ready;

cc base(
	.initiate (start),
	.rst   (rst),
	.clk   (clk),
	.ena   (ena),
	.B_part_key (B_part_key_from_drone),
	.rdy_drone (message_is_encrypted),
	.c (encrypted_message),
	.A_part_key_output (A_part_key_from_drone),
	.cc_rdy (values_are_ready),
	.g_output  (g_from_cc),
	.p_output  (p_from_cc),
	.decrypted (decrypted_message)

);

drone sea_dron(
	.initiate  (start),
	//.cc_contact (start_cc),
	.rst (rst),
	.clk (clk),
	.ena (ena),
	.message (message_from_drone),
	.rdy_cc  (values_are_ready),
	.A_part_key (A_part_key_from_drone),
	.g (g_from_cc),
	.p (p_from_cc),
	.B_part_key_output (B_part_key_from_drone),
	.messageEncrypted_output(encrypted_message),
	.confirmation_of_encryption (message_is_encrypted)

);


endmodule 