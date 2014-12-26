// Philip Liberato (pnl8zp)
// CS 4102 Coding 4
// 3/3/2014

#include <iostream>
#include <fstream>
#include <string>
#include <stdio.h>
#include <cstdlib>
#include <cstring>
#include <math.h>
#include <vector>

using namespace std;

struct Node {
        int value;
        int maxPath;
        bool known;
    };

vector<vector<Node> > grid; 
int longestPath(int r, int c, int maxR, int maxC);
 
 int main(int argc, char* argv[]) {
    
    if (argc != 2) {
        cout << "Error: Specify an input file name as a command-line parameter!" << endl;
        exit(1);
    }
    
    FILE *fp = fopen(argv[1], "r");

    if (fp == NULL) {
        cout << "File '" << argv[1] << "' could not be found!" << endl;
        exit(2);
    }
    
    string line;
    ifstream input(argv[1]);
    getline(input,line);
    int cases = atoi(line.c_str());
    
    for(int a=0; a<cases; a++) {
        getline(input,line);
        char data[line.length()+1];
        strcpy(data, line.c_str());
        char * char_pointer(strtok (data, " "));
        
        string name = char_pointer;
        cout << name + ": ";
                
        line = strtok(NULL, " ");
        int rows = atoi(line.c_str());
        
        line = strtok(NULL, " ");
        int cols = atoi(line.c_str());
        
        //Resize the grid for each case
        grid.resize(rows);
        for (int z = 0; z < rows; ++z)
            grid[z].resize(cols);
        
        //Load the grid with data
        for(int j=0; j<rows; j++) {
            getline(input,line);
            char line2[line.length()+1];
            strcpy(line2, line.c_str());
            char *p = strtok(line2, " ");
            int k=0;
            while (p) {
                Node newNode;
                newNode.value = atoi(p);
                newNode.maxPath = 0;
                newNode.known = false;
                grid[j][k] = newNode;
                p = strtok(NULL, " ");
                k++;
            }
        }
        int max = 0;
        for(int x=0; x<rows; x++){
            for(int y=0; y<cols; y++) {
                int g = longestPath(x, y, rows, cols);
                if (g > max)
                    max = g;
            }
        }
        cout << max << endl;
    }
 }
 
 int longestPath(int r, int c, int maxR, int maxC) {
    int north =0;
    int south =0;
    int east =0;
    int west =0;

    //North
    if((r-1) >= 0) {
        if(grid[r][c].value > grid[r-1][c].value) {
            if(grid[r-1][c].known)
                north = grid[r-1][c].maxPath;
            else {
                north = longestPath(r-1,c,maxR,maxC);
            }
        }
    }

    //South
    if((r+1) < maxR) {
        if(grid[r][c].value > grid[r+1][c].value) {
            if(grid[r+1][c].known)
                south = grid[r+1][c].maxPath;
            else {
                south = longestPath(r+1,c,maxR,maxC);
            }
        }
    }

    //East
    if((c+1) < maxC) {
        if(grid[r][c].value > grid[r][c+1].value) {
            if(grid[r][c+1].known)
                east = grid[r][c+1].maxPath;
            else {
                east = longestPath(r,c+1,maxR,maxC);
            }
        }
    }
  
    //West
    if((c-1) >= 0) {
        if(grid[r][c].value > grid[r][c-1].value) {
            if(grid[r][c-1].known)
                west = grid[r][c-1].maxPath;
            else {
                west = longestPath(r,c-1,maxR,maxC);
            }
        }
    }
    
    grid[r][c].known = true;
    grid[r][c].maxPath = 1 + (max(max(north,east),max(south,west)));
    return grid[r][c].maxPath;
    
 }
