//
//  main.c
//  srv
//
//  Created by Yifu Zhou on 2017/3/5.
//  Copyright © 2017年 Yifu. All rights reserved.
//

#include <stdio.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <stdlib.h>

#include <pthread.h>
#include <time.h>

int receive_one_byte(int client_socket, char *cur_char);
int receiveFully(int client_socket, char *buffer, int length);
void *GetLocalTime(void * v);
void *GetLocalOS(void * v);
void int2byte(char a[], int time);

char *buffer;
int finish = 0;// 0 means unfinish, 1 means finish;

int main()
{
    // get a socket descriptor
   	int server_socket = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
   	printf("server_socket = %d\n", server_socket);
    
    // bind to a port
    struct sockaddr_in sin;
    memset(&sin, 0, sizeof(sin));
    sin.sin_len = sizeof(sin);  // comment this line out if running on pyrite (linux)
    sin.sin_family = AF_INET; // or AF_INET6 (address family)
    sin.sin_port = htons(5537);
    sin.sin_addr.s_addr= INADDR_ANY;
    
    if (bind(server_socket, (struct sockaddr *)&sin, sizeof(sin)) < 0)
    {
        // Handle the error.
        printf("bind error\n");
    }
    
    listen(server_socket, 5); /* maximum 5 connections will be queued */
    int counter = 0;
    while (1)
    {
        struct sockaddr client_addr;
        unsigned int client_len;
        
        printf("accepting ....\n");
        int client_socket = accept(server_socket, &client_addr, &client_len);
        printf("request %d comes ...\n", counter++);
        /* launch a new thread to take care of this client connection */
        /* client_addr contains the address of the connecting client */
        /* client_len is the buffer length that is valid in client_addr */
        /* both client_addr and client_len are optional */
        
        // processing this request
        
        // get the command length
        char packet_length_bytes[4];
        receiveFully(client_socket, packet_length_bytes, 4);
        
        //convert char array into int arry
        int c2i[4];
        for (int i = 0; i < 4; i++) {
            c2i[i] = packet_length_bytes[i];
        }
        
        //get the packet length
        int packet_length = bytesToInt(c2i);
        printf("packet_length_bytes = %d \n", packet_length);
        
        // allocate buffer to receive the data
        buffer = (char *)malloc(packet_length);
        receiveFully(client_socket, buffer, packet_length);
        
        printf("buffer start name is : %s!\n", buffer);

        //create a new thread to assign value in buffer
        finish = 0;
        if (strcmp(buffer, "GetLocalTime") == 0) {
            pthread_t th1;
            pthread_create(&th1, NULL, GetLocalTime, buffer);
            
        }
        if (strcmp(buffer, "GetLocalOS") == 0) {
            pthread_t th1;
            pthread_create(&th1, NULL, GetLocalOS, buffer);
        }
        
        // send back
        while (finish == 0);
        send(client_socket, packet_length_bytes, 4, 0); // 4 bytes first
        send(client_socket, buffer, packet_length, 0);
        
        // release buffer
        free(buffer);
    }
}

void *GetLocalTime(void * v)
{
    int length = 0;
    int len_b2i[4];
    for (int i = 0; i < 4; i++) {
        len_b2i[i] = buffer[100 + i];
    }
    length = bytesToInt(len_b2i);
    printf("The length is %d\n", length);
    
    time_t t_t;
    t_t =time(NULL);
    int t_i = t_t;
    printf("The time is %d\n", t_i);
    
    char time_byte[4];
    int2byte(time_byte, t_i);
    
    for (int i = 0; i < 4; i++) {
        buffer[104 + i] = time_byte[i];
    }
    
    buffer[108] = 1;
    finish = 1;
    
    
    return 0;
}

void *GetLocalOS(void * v)
{
    int length = 0;
    int len_b2i[4];
    for (int i = 0; i < 4; i++) {
        len_b2i[i] = buffer[100 + i];
    }
    length = bytesToInt(len_b2i);
    printf("The length is %d\n", length);
    
    char os[] = "Linux";
    
    
    for (int i = 0; i < sizeof(os); i++) {
        buffer[104 + i] = os[i];
        printf("%c", os[i]);
    }
    printf("\n");
    
    //buffer[108] = 1;
    finish = 1;
    
    
    return 0;
}


int receive_one_byte(int client_socket, char *cur_char)
{
    ssize_t bytes_received = 0;
    while (bytes_received != 1)
    {
        bytes_received = recv(client_socket, cur_char, 1, 0);
    }
    
    return 1;
}

int receiveFully(int client_socket, char *buffer, int length)
{
    char *cur_char = buffer;
    ssize_t bytes_received = 0;
    while (bytes_received != length)
    {
        receive_one_byte(client_socket, cur_char);
        cur_char++;
        bytes_received++;
    }
    
    return 1;
}

//Assume the length of buffer will not exceed 65,536 = 256^2
int bytesToInt(int *b)
{
    int result = 0;
    for (int i = 3; i > 1; i --) {
        if (b[i] < 0)
            result += (128 + b[i] - (-128)) * pow(256, 3-i);
        else
            result += b[i] * pow(256, 3-i);
    }

    return result;
}

void int2byte(char array[], int time)
{

    int temp = time;
    int value_i;
    for (int i = 0; i < 4; i++) {
        value_i = temp % 256;
        if (value_i > 127) {
            array[i] = value_i - 128 - 128;
        }
        else
            array[i] = value_i;
        temp = temp / 256;
    }

}


