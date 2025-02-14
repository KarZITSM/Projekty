module powermod (
  input         clk,
  input         rst,
  input         start,
  input 			 ena,
  input  [7:0] a,
  input  [7:0] b,
  input  [7:0] m,
  output [7:0] res,
  output reg 		    rdy
);

  localparam SIZE = 3;
  localparam [SIZE-1:0] idle   = 3'h0,
                         init   = 3'h1,
                         loop   = 3'h2,
                         store  = 3'h3;
                         
  reg [SIZE-1:0] state_reg, state_next;
  reg            rdy_next;
  
  reg  [7:0] a_reg, a_next;
  reg  [7:0] b_reg, b_next;
  reg  [7:0] m_reg, m_next;
  reg  [7:0] res_reg, res_next;
  reg  [19:0] reg_number1, reg_number2;

  // State register
  always@(posedge clk, posedge rst) begin
    if (rst) begin
      state_reg <= idle;
      rdy       <= 1'b0;
    end
    else begin
      state_reg <= state_next;
      rdy       <= rdy_next;
    end
  end

  // Registers
  always@(posedge clk, posedge rst) begin
    if (rst) begin
      a_reg <= 8'h0;
      b_reg <= 8'h0;
      m_reg <= 8'h0;
      res_reg <= 8'h0;
    end
    else begin
      a_reg   <= a_next;
      b_reg   <= b_next;
      m_reg   <= m_next;
      res_reg <= res_next;
    end
  end
  
  // Next state logic
  always@(*) 
    case(state_reg)
      idle  : if (start) state_next = init;
              else        state_next = idle;
      init  : state_next = loop;
      loop  : if (b_reg == 0) state_next = store;
              else            state_next = loop;
      store : state_next = idle;
      default: state_next = idle;
    endcase  

  // Microoperation logic
  always@(*) begin
    a_next     = a_reg;
    b_next     = b_reg;
    m_next     = m_reg;
    res_next   = res_reg;
    rdy_next   = 1'b0;
  
    case(state_reg)
      init  : begin
                a_next   = a % m;
                b_next   = b;
                m_next   = m;
                res_next = 1;
              end
      loop  : begin 
						if (b_reg[0] == 1)begin reg_number1 = (a_reg * res_reg);
														res_next = reg_number1 % m_reg;	
														end
				  reg_number2 = a_reg * a_reg;
				  a_next = reg_number2 % m_reg;
				  b_next = b_reg >> 1;
              end
              
      store : begin
                rdy_next   = 1'b1;
              end
      default: ; 
    endcase
  end

  assign res = res_reg;

endmodule
