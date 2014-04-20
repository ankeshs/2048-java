class Player
{
    int board[][];
    int num_row, num_col;
    State state;
    Player(State s){
        board = new int[num_row=s.board.length][num_col=s.board[0].length];
        update(s);
        state = s;
    }
    
    void update(State s){
        state = s;
        for(int i=0;i<num_row;i++)
            for(int j=0;j<num_col;j++)
                board[i][j]=s.board[i][j];
    }
    
    char getNext(){
        int step=4;
        int max=moveGain('L',step), tmp;
        System.out.print(":"+max+",");
        char mv='L';   
        if(max<(tmp=moveGain('R',step))) { mv='R'; max=tmp;} System.out.print(":"+tmp+",");
        if(max==tmp && Math.random()<0.5) mv='R';
        if(max<(tmp=moveGain('U',step))) {mv='U'; max=tmp;} System.out.print(":"+tmp+",");
        if(max==tmp && Math.random()<0.5) mv='U';
        if(max<(tmp=moveGain('D',step))) {mv='D'; max=tmp;} System.out.println(":"+tmp);
        if(max==tmp && Math.random()<0.5) mv='D';
        if(max==-1) State.gameState=false;
        return mv;
    }
    
    int moveGain(char flag, int step){
            //System.out.print(flag);
            update(state);
            int[][] b = board;
            int s=0;
            int gain=0;
            boolean valid = false;
            switch(flag){
                case 'L':
                    s = 0;
                    while(s<num_col-1){
                        for(int i=0;i<num_row;i++){
                            if(b[i][s]!=0) {
                                int sc = s+1;
                                while(sc<num_col && b[i][sc]==0) sc++;
                                if(sc<num_col && b[i][sc]==b[i][s]){ b[i][s]*=2; gain+=b[i][sc]; b[i][sc]=0; }
                            }                    
                        }
                        s++;
                    }                    
                    s = 0;
                    while(s<num_col-1){
                        for(int i=0;i<num_row;i++){
                            if(b[i][s]==0) {
                                int sc = s+1;
                                while(sc<num_col && b[i][sc]==0) sc++;
                                if(sc<num_col && b[i][sc]!=0){ b[i][s]=b[i][sc]; b[i][sc]=0; valid=true;}
                            }                    
                        }
                        s++;
                    }
                break;
                
                case 'R':
                    s = num_col-1;
                    while(s>0){
                        for(int i=0;i<num_row;i++){
                            if(b[i][s]!=0) {
                                int sc = s-1;
                                while(sc>=0 && b[i][sc]==0) sc--;
                                if(sc>=0 && b[i][sc]==b[i][s]){ b[i][s]*=2; gain+=b[i][sc]; b[i][sc]=0; }
                            }                    
                        }
                        s--;
                    }                    
                    s = num_col-1;
                    while(s>0){
                        for(int i=0;i<num_row;i++){
                            if(b[i][s]==0) {
                                int sc = s-1;
                                while(sc>=0 && b[i][sc]==0) sc--;
                                if(sc>=0 && b[i][sc]!=0){ b[i][s]=b[i][sc]; b[i][sc]=0; valid=true;}
                            }                    
                        }
                        s--;
                    }
                break;
                
                case 'U':
                    s = 0;
                    while(s<num_row-1){
                        for(int i=0;i<num_col;i++){
                            if(b[s][i]!=0) {
                                int sc = s+1;
                                while(sc<num_row && b[sc][i]==0) sc++;
                                if(sc<num_row && b[sc][i]==b[s][i]){ b[s][i]*=2; gain+=b[sc][i]; b[sc][i]=0; }
                            }                    
                        }
                        s++;
                    }                    
                    s = 0;
                    while(s<num_row-1){
                        for(int i=0;i<num_col;i++){
                            if(b[s][i]==0) {
                                int sc = s+1;
                                while(sc<num_row && b[sc][i]==0) sc++;
                                if(sc<num_row && b[sc][i]!=0){ b[s][i]=b[sc][i]; b[sc][i]=0;  valid=true;}
                            }                    
                        }
                        s++;
                    }
                break;
                
                case 'D':
                    s = num_row-1;
                    while(s>0){
                        for(int i=0;i<num_col;i++){
                            if(b[s][i]!=0) {
                                int sc = s-1;
                                while(sc>=0 && b[sc][i]==0) sc--;
                                if(sc>=0 && b[sc][i]==b[s][i]){ b[s][i]*=2; gain+=b[sc][i]; b[sc][i]=0; }
                            }                    
                        }
                        s--;
                    }                    
                    s = num_row-1;
                    while(s>0){
                        for(int i=0;i<num_col;i++){
                            if(b[s][i]==0) {
                                int sc = s-1;
                                while(sc>=0 && b[sc][i]==0) sc--;
                                if(sc>=0 && b[sc][i]!=0){ b[s][i]=b[sc][i]; b[sc][i]=0;  valid=true;}
                            }                    
                        }
                        s--;
                    }
                break;
            }
            if(gain>0) valid = true;
            if(!valid) gain = -1;
            else if(step>0){
                State s2 = new State();
                s2.board = b;
                int maxP = Integer.MAX_VALUE;
                for(int i=0; i<num_row; i++){
                    for(int j=0; j<num_col; j++){
                        if(b[i][j]==0) {
                            b[i][j]=2;
                            Player p = new Player(s2);
                            int max=p.moveGain('L',step-1), tmp;                  
                            if(max<(tmp=p.moveGain('R',step-1))) max=tmp;
                            if(max<(tmp=p.moveGain('U',step-1))) max=tmp;
                            if(max<(tmp=p.moveGain('D',step-1))) max=tmp;
                            if(maxP>max) maxP=max;
                            b[i][j]=0;
                        }
                    }
                }
                if(maxP>0 && maxP<1000000) gain +=maxP;
            }
            return gain;
    }
}