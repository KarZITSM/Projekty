module cc (
input initiate,  //Sygnał z inicjalizacją kontatku 
input rst,
input clk,
input ena,
input [7:0] B_part_key, // Komponent klucza ze strony drona
input rdy_drone,
input [63:0] c, //Zaszyfrowana wiadomość 
output [7:0] A_part_key_output, //Komponent klucza ze strony cc
output cc_rdy,
output [7:0] g_output,
output [7:0] p_output,
output [63:0] decrypted
);
	//Stany automatu
	localparam SIZE = 3;
	localparam [SIZE-1:0] idle = 3'h0,  // oczekiwanie
									lfsr_generate1   = 3'h1, //Wygenerowania liczby a
									lfsr_generate2   = 3'h2,//Wygenerowanie liczby g
									prime_number   =   3'h3,//Wybranie liczby pierwszej
									powmod_A   =  3'h4,//Potęgowanie modularne A
									wait_for_drone = 3'h5,//Czekanie aż dron odeśle dane 
									get_key = 3'h6,//Wygenerowanie klucza 
									message_decription = 3'h7; //Odzyfrowanie wiadomośći 
									
	//Rejestr stanów
	reg[SIZE-1:0] state_reg, state_next;
	
	//Zmienne do potęgowania 
	wire [7:0] g, a, p, key, A_part_key;
	wire rdy_random_a, rdy_random_g, rdy_prime, rdy_pow_A, rdy_pow_key, rdy_decrypt;
	reg  start, start_random_g, start_prime, start_pow_A, start_pow_key, start_decryption, cc_ready_reg, drone_reg;
	reg [7:0] a_reg, g_reg, p_reg, A_part_key_reg, key_reg;
	
	
	//Used modules
random_generator g_1 (
		.rst	 (rst),
		.clk	 (clk),
		.start (start),
		.ena	 (ena),
		.valuea(a),
		.rdy_random(rdy_random_a)
);
random_generator1 g_2 (
		.rst	 (rst),
		.clk	 (clk),
		.start (start_random_g),
		.ena	 (ena),
		.valuea(g),
		.rdy_random(rdy_random_g)
);

primenumber p_1 (
		.clk	   (clk),
		.index   (g_reg),
		.start (start_prime),
		.data_out(p),
		.rdy_prime (rdy_prime)
);

powermod A_1 (
		.rst	 (rst),
		.clk	 (clk),
		.ena	 (ena),
		.start (start_pow_A),
		.a		 (g_reg),
		.b		 (a_reg),
		.m		 (p_reg),
		.res   (A_part_key),
		.rdy   (rdy_pow_A)
);
powermod key_creation (
		.rst	 (rst),
		.clk	 (clk),
		.ena	 (ena),
		.start (start_pow_key),
		.a		 (B_part_key),
		.b		 (a_reg),
		.m		 (p_reg),
		.res   (key),
		.rdy   (rdy_pow_key)
);
codeMessage decryption (
		.clk   (clk),
		.ena   (ena),
		.letter(c),
		.key   (key_reg),
		.confirm (start_decryption),
		.codedMessage (decrypted),
		.done  (rdy_decrypt)
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
//    a_reg <=8'b00000000;
//	 g_reg <=8'b00000000;
//	 p_reg <=8'b00000000;
//	 A_part_key_reg <=8'b00000000;
//	 key_reg <=8'b00000000;
    end
    else begin
//      a_reg   <= a_next;
//      g_reg   <= g_next;
//      p_reg   <= p_next;
//     A_part_key_reg <= A_part_key_next;
    end
  end
  
  // Next state logic
  always@(*) 
    case(state_reg)
      idle  : if (initiate) state_next = lfsr_generate1;
              else        state_next = idle;
      lfsr_generate1  : if(rdy_random_a) state_next = lfsr_generate2;
								else state_next = lfsr_generate1;
		lfsr_generate2  : if(rdy_random_g) state_next = prime_number;
								else state_next = lfsr_generate2;
      prime_number  : if(rdy_prime) state_next = powmod_A;
								else state_next = prime_number;
      powmod_A : if(rdy_pow_A) state_next = wait_for_drone;
						else state_next = powmod_A;
		wait_for_drone: if(drone_reg) state_next = get_key;
								else state_next = wait_for_drone;
		get_key : if(rdy_pow_key) state_next = message_decription;
		message_decription : state_next = message_decription;
      default: state_next = idle;
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
		lfsr_generate1: begin
							 start = 1;
							 if(rdy_random_a) begin
								a_reg <= a;
								end
							end
		lfsr_generate2: begin
								//start = 0;
								start_random_g <= 1;
								if(rdy_random_g)begin
										g_reg <= g; 
										end
								end
		prime_number : begin
								//start_random_g = 0;
								start_prime = 1;
								if(rdy_prime) begin
										p_reg <= p;
										end
							end
		powmod_A : begin
								start_prime = 0;
								start_pow_A = 1;
								if(rdy_pow_A) begin
										A_part_key_reg <= A_part_key;
										cc_ready_reg <= 1;
										end
						end 
		wait_for_drone: begin
									start_pow_A = 0;
									if(rdy_drone) begin
										drone_reg <= 1;
									end
								end
		get_key : begin 
						drone_reg = 0;
						start_pow_key =1;
						if(rdy_pow_key) begin
								key_reg <= key;
						end
					end
		message_decription : begin
									start_pow_key = 0;
									start_decryption = 1;
									if(rdy_decrypt)begin
										start_pow_key = 0;
									end
								end
      default: ; 
    endcase
  end
  
  assign A_part_key_output = A_part_key_reg;
  assign g_output = g_reg;
  assign p_output = p_reg;
  assign cc_rdy = cc_ready_reg;


endmodule