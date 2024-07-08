#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>


#define A_path "/proc/proj4zelkar/rejA"
#define S_path "/proc/proj4zelkar/rejS"
#define W_path "/proc/proj4zelkar/rejW"

int rd_file(char *file_path){
    int i;
    FILE *fptr;

    fptr = fopen(file_path,"r");
    if (fptr == NULL){
        printf("Error! Can not open the file.");
        exit(1);
    }

    fscanf(fptr,"%o", &i);
    fclose(fptr);

    return i;
}

void wrt_file(char *file_path, int input_number){
    FILE  *fptr;

    fptr = fopen(file_path, "w");
    if(fptr == NULL){
        printf("Error! Can not open the file.");
        exit(EXIT_FAILURE);
    }

    if(fprintf(fptr, "%o", input_number) < 0){
        printf("Error while trying to write the file.");
        exit(EXIT_FAILURE);
    }

    fclose(fptr);
}

int searching_prime_test(int argument,  float sleep_time){

    if (argument <= 0) {
        fprintf(stderr, "Invalid argument: %d. Argument must be a positive integer.\n", argument);
        return -1;
    }

    if (sleep_time < 0) {
        fprintf(stderr, "Invalid sleep time: %f. Sleep time must be non-negative.\n", sleep_time);
        return -1;
    }

    printf("\n");
    printf("Searching for %o prime number.\n", argument);
    wrt_file(A_path, argument);
    sleep(sleep_time);
    printf("Current state of device: %o\n seconds:%0.1lf.\n",rd_file(S_path), sleep_time);
    while(rd_file(S_path) != 0 &&  rd_file(S_path) != 2){
      sleep(0.1);
    }
    printf("Current state: %o - finished\n", rd_file(S_path));
    printf("Result: %o\n", rd_file(W_path));  
    return rd_file(W_path);
}


void inv_argument(int argument, float sleep_time1, float sleep_time2){
    if (sleep_time1 < 0 || sleep_time2 < 0) {
        fprintf(stderr, "Invalid sleep times: %f, %f. Sleep times must be non-negative.\n", sleep_time1, sleep_time2);
        return;
    }

    printf("\n");
    printf("Searching for %o prime number.\n", argument);
    wrt_file(A_path, argument); 
    sleep(sleep_time2);
    printf("Current state after %o seconds: %0.1lf \n", rd_file(S_path), sleep_time2);
    sleep(sleep_time1);
    printf("Current state: %o - finished1\n", rd_file(S_path));
    printf("Result: %o\n", rd_file(W_path));
}

int main(void){

    printf("\n");
    printf("Tests for correct values:");
   
   if( searching_prime_test(05, 0.5) == 11 ){
    printf("test passed\n");
   }else{
    printf("test not passed\n");
   }
   if( searching_prime_test(014, 4.0) == 37 ){
    printf("test passed\n");
   }else{
    printf("test not passed\n");
   }
   if( searching_prime_test(0100, 45.0) == 311 ){
    printf("test passed\n");
   }else{
    printf("test not passed\n");
   }

   inv_argument(0200000, 2.0, 2.0);
   inv_argument(-20, 2.0, 2.0);
}
