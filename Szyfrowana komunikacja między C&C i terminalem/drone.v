module drone (
input initiate,  //Sygnał z inicjalizacją kontatku 
//output cc_contact,
input rst,
input clk,
input ena,
input [63:0]message,
input rdy_cc,
input [7:0] A_part_key, // Komponent klucza ze strony drona
input [7:0] g,
input [7:0] p,
output [7:0] B_part_key_output, //Komponent klucza ze strony cc
output [63:0] messageEncrypted_output, // wiadomość zaszyfrowana 
output [7:0] confirmation_of_encryption
);
	//Stany automatu
	localparam SIZE = 3;
	localparam [SIZE-1:0] idle = 3'h0,  // oczekiwanie
									get_random_b   = 3'h1, //Wygenerowania liczby b
									wait_for_cc = 3'h2,
									get_B_get_key = 3'h3,//Czekanie aż cc odeśle dane 
									key_creation = 3'h4,
									encryption   =  3'h5,//Potęgowanie modularne B
									send_to_cc = 3'h6;
								
	//Rejestr stanów
	reg[SIZE-1:0] state_reg, state_next;
	
	//Zmienne do potęgowania 
	wire [7:0] b, B_part_key, key;
	wire [63:0] messageEncrypted;
	wire rdy_random_b, rdy_pow_key, rdy_message_C, rdy_pow_B;
	reg rdy_cc_reg, start_key_creation, start_pow_B, start_encription, start_rand;
	reg [63:0] messageEncrypted_reg;
	reg [7:0] b_reg;
	reg [7:0] B_reg;
	reg [7:0] K_reg;
	
	
	//Used modules
random_generator2 
	dronb (
		.rst	 (rst),
		.clk	 (clk),
		.start (start_rand),
		.ena	 (ena),
		.valuea(b),
		.rdy_random(rdy_random_b)
);


powermod B_dron (
		.rst	 (rst),
		.clk	 (clk),
		.ena	 (ena),
		.start (start_pow_B),
		.a		 (g),
		.b		 (b_reg),
		.m		 (p),
		.res   (B_part_key),
		.rdy   (rdy_pow_B)
);


powermod key_creation_mod (
		.rst	 (rst),
		.clk	 (clk),
		.ena	 (ena),
		.start (start_key_creation),
		.a		 (A_part_key),
		.b		 (b_reg),
		.m		 (p),
		.res   (key),
		.rdy   (rdy_pow_key)
);

codeMessage  code_dron(
      .clk (clk),
      .ena (ena),
	   .letter (message),
	   .key (K_reg),
		.confirm (start_encription),
	   .codedMessage (messageEncrypted),
		.done (confirmation_of_encryption)
);


// State register
  always@(posedge clk, posedge rst) begin
    if (rst) begin
      state_reg <= idle;
      
    end
    else begin
      state_reg <= state_next;
     
    end
  end

  // Registers
  always@(posedge clk, posedge rst) begin
    if (rst) begin
//		  b_reg<=8'b00000000;
//		  B_reg<=8'b00000000;
//		  K_reg<=8'b00000000;
//		  Message_reg<=8'b00000000;

    end
    else begin
//        b_reg<=b_next;
//        B_reg<= B_next;
//        K_reg <= K_next;
//        Message_reg<= Message_next;
    end
  end
  
  // Next state logic
  always@(*) 
    case(state_reg)
      idle  : if (initiate) state_next = get_random_b;
              else        state_next = idle;
		get_random_b : if(rdy_random_b) state_next = wait_for_cc;
				  else        state_next = get_random_b;
	   wait_for_cc  : if(rdy_cc_reg) state_next = get_B_get_key;
								else state_next = wait_for_cc;
      get_B_get_key: if(rdy_pow_B) state_next = key_creation;
								else state_next = get_B_get_key;
		key_creation : if(rdy_pow_key) state_next = encryption;
								else state_next = key_creation;
      encryption   : if(messageEncrypted) state_next = send_to_cc;
								else state_next = encryption;
		send_to_cc   : //if(message_sent)
								state_next = send_to_cc;
								//else state_next = send_to_cc;
      default      : state_next = idle;
    endcase
	 

  // Microoperation logic
  always@(*) begin
//    a_next     = a_reg;
//    g_next     = g_reg;
//    p_next     = p_reg;
//    A_part_key_next   = A_part_key_reg;
    
  
    case(state_reg)
      idle:
			begin
			
			end
		get_random_b : begin
							start_rand = 1;
							if(rdy_random_b) begin
								b_reg <= b;
								end
					   end
		wait_for_cc  : begin
							start_rand = 0;
							if(rdy_cc) begin
								rdy_cc_reg =1;
							end
						end
		get_B_get_key: begin
							rdy_cc_reg = 0;
							start_pow_B = 1;
							if(rdy_pow_B) begin
								B_reg <= B_part_key;
							end
						end
		key_creation : begin
								start_pow_B = 0;
								start_key_creation = 1;
								if(rdy_pow_key) begin
									K_reg <= key;
								end
						end 
		encryption   : begin
							start_key_creation = 0;
							start_encription = 1;
							if(confirmation_of_encryption) begin
								messageEncrypted_reg <= messageEncrypted;
							end
						end
		send_to_cc   : begin 
		               
					   end
      default      : ; 
    endcase
  end

  assign B_part_key_output = B_reg;
  assign messageEncrypted_output = messageEncrypted_reg;
endmodule