/* verilator lint_off UNUSED */ 
/* verilator lint_off UNDRIVEN */ 
/* verilator lint_off MULTIDRIVEN */ 
/* verilator lint_off COMBDLY */
/* verilator lint_off WIDTH */
/* verilator lint_off CASEINCOMPLETE */
module gpioemu(n_reset,                 //magistrala z CPU
    saddress[15:0], srd, swr, 
    sdata_in[31:0], sdata_out[31:0], 
    gpio_in[31:0], gpio_latch,          //styk z GPIO - in
    gpio_out[31:0],                     //styk z GPIO = out
    clk,                                //sygnaly opcjonalne - zegar 1 KHz
    gpio_in_s_insp[31:0]);              //sygnaly testowe

    input           clk;
    input           n_reset;
    input [15:0]    saddress;       //magistrala - adres
    input           srd;            //odczyt przez CPU z mag. danych
    input           swr;            //zapis przez CPU do mag. danych 
    input [31:0]    sdata_in;       //magistrala wejsciowa CPU
    output[31:0]    sdata_out;      //magistrala wyjsciowa z CPU
    reg [31:0]        sdata_out_s;    //stan magistrali danych -wyjscie
    input[31:0]     gpio_in;          //dane z peryferii wejscie do modulu
    reg[31:0]         gpio_in_s;        //stan peryferii wyjsciowych
    input             gpio_latch;        //zapis danych na gpio_in
    output[31:0]    gpio_in_s_insp; //debuging
    output[31:0]    gpio_out;          //dane wyjsciowedo peryferii
    reg[31:0]       gpio_out_s;      //stan peryferii wejsciowych

    reg[31:0]        sdata_in_s;

   
    
    parameter MAX_PRIME = 1000;
    parameter IDLE = 2'b00;
    parameter CALCULATING = 2'b01;
    parameter CHECKING_PRIME = 2'b11;
    parameter FOUND = 2'b10;

    reg unsigned [9:0] A;
    reg unsigned [2:0] S;
    reg unsigned [31:0] W;
    reg unsigned [31:0] number_of_numbers;
    reg unsigned [31:0] current_prime;


    reg unsigned [31:0] prime_count;
    reg unsigned [31:0] i;
    reg prime_flag;

    always @(negedge n_reset)
    begin
        gpio_in_s <= 0;
        gpio_out_s <= 0;
        sdata_out_s <= 0;
        A <= 0;
        S <= IDLE;
        W <= 0;
        number_of_numbers <= 0;
        prime_count <= 0;
        current_prime <= 2;
        i <= 0;
        prime_flag <= 1;
    end
    
    always @(posedge gpio_latch) begin
		gpio_in_s[31:0] <= gpio_in[31:0];
    end


    always @(posedge swr)
    begin
        if (saddress == 16'h288)
        begin
            W <= 0;
            A <= sdata_in[9:0];
            S <= CALCULATING;
            current_prime <= 2;
            prime_count <= 0;
        end
    end

    always @(posedge srd)
    begin
        if (saddress == 16'h288)
        begin
            sdata_out_s <= {22'b0, A[9:0]};
        end
        else if (saddress == 16'h2A0)
        begin
            sdata_out_s <= {29'b0, S[2:0]};
        end
        else if (saddress == 16'h298)
        begin
            sdata_out_s <= W;
        end
        else
        begin
            sdata_out_s <= 0;
        end
    end

    always @(posedge clk)
    begin
        case(S)
            IDLE:
            begin
                if (A != 0)
                begin
                    S <= CALCULATING;
                    current_prime <= 2;
                    prime_count <= 0;
                    number_of_numbers <= 0;
                end
            end
            CALCULATING:
            begin
                S <= CHECKING_PRIME;
                i <= 2;
                prime_flag <= 1;
            end
            CHECKING_PRIME:
            begin
                if (i * i > current_prime)
                begin
                    if (prime_flag)
                    begin
                        if (prime_count == A - 1)
                        begin
							current_prime <= current_prime + 1;
                            prime_count <= prime_count + 1;
                            number_of_numbers <= number_of_numbers + 1;
                            S <= FOUND;
                            W <= current_prime;
                        end
                        else
                        begin
                            current_prime <= current_prime + 1;
                            prime_count <= prime_count + 1;
                            number_of_numbers <= number_of_numbers + 1;
                            S <= CALCULATING;
                        end
                    end
                    else
                    begin
                        current_prime <= current_prime + 1;
                        S <= CALCULATING;
                    end
                end
                else
                begin
                    if (current_prime % i == 0)
                        prime_flag <= 0;
                    i <= i + 1;
                end
            end
            FOUND:
            begin
                prime_count <= 0;
                A <= 0;
                S <= IDLE;
            end
        endcase
    end

    always @(posedge clk)
    begin
        gpio_out_s <= number_of_numbers;
    
    end

    assign gpio_out = gpio_out_s;
    assign gpio_in_s_insp = gpio_in_s;
    assign sdata_out = sdata_out_s;

endmodule
