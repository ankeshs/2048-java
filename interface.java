import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.Math;
import java.util.*;

class State {
	int board[][];
        
        static boolean gameState = true;
        
	State(){
		board = new int[4][4];	
	}
	
	static String getHtml(int val){
            String s = "", bg="#ffffff";
            if(val>0) {s = ""+val;
            int c = 0, v = 2;
            while(v<val) {c++; v*=2;}
            c=c%8;            
            String col[]=new String[]{"#e67e22","#e74c3c", "#d35400", "#9b59b6",
            "#2980b9", "#27ae60", "#16a085", "#34495e"};
            bg = col[c];
            }
            return "<html><body style='background:"+bg+"; height:60px; width: 60px; font-size: 22px; color: #ffffff; text-align:center; padding-top:15px; '>"+s+"</body></html>";
        }
	
}

class Interface extends JFrame implements KeyListener{
	State state;
	int num_row, num_col;
	JLabel tile[][]; 
        JLabel status;
        Random rand;
        Player player;
	Interface(State s) {
		state = s;
		num_row = s.board.length;
		num_col = s.board[0].length;
		tile = new JLabel[num_row][num_col];
                setLayout(new BorderLayout());
                JPanel pGrid=new JPanel();                
		pGrid.setLayout(new GridLayout(num_row, num_col));
		for(int i=0; i<num_row; i++){
			for(int j=0; j<num_col; j++){
				pGrid.add(tile[i][j]=new JLabel(State.getHtml(0)));
			}
		}
		add(pGrid, BorderLayout.CENTER);
                add(status=new JLabel(" "), BorderLayout.SOUTH);
		addKeyListener(this);
                rand = new Random();
                placeRandom();
                placeRandom();
                player=new Player(state);
                renderCells();                
	}
	
        public void keyPressed(KeyEvent evt){
        }
        public void keyReleased(KeyEvent evt){
            int ch = evt.getKeyCode();
            switch(ch){
                case KeyEvent.VK_UP: System.out.println("up"); move('U'); break;
                case KeyEvent.VK_DOWN: System.out.println("down"); move('D'); break;
                case KeyEvent.VK_LEFT: System.out.println("left"); move('L'); break;
                case KeyEvent.VK_RIGHT: System.out.println("right"); move('R'); break;
                case KeyEvent.VK_A: move(player.getNext()); break;
            }
        }
        
        public void autoplay(){
            move(player.getNext());
            try{
            Thread.sleep(300);
            }catch(Exception e){}
            if(State.gameState) autoplay();
        }
        
        public void keyTyped(KeyEvent evt) {
        }
	
	void renderCells() {
            for(int i=0; i<num_row; i++){
                    for(int j=0; j<num_col; j++){
                            tile[i][j].setText(State.getHtml(state.board[i][j]));
                    }
            }
        }
        
                
        boolean placeRandom(){
            int opts[]=new int[]{2,2,2,2,2,2,4,4,4,8};
            ArrayList<Point> pos= new ArrayList<Point> ();
            for(int i=0; i<num_row; i++){
                    for(int j=0; j<num_col; j++){
                            if(state.board[i][j]==0) pos.add(new Point(i,j));
                    }
            }
            if(pos.size()==0)return false;
            Point p = pos.get(rand.nextInt(pos.size()));
            state.board[(int)p.getX()][(int)p.getY()] = opts[rand.nextInt(opts.length)];            
            return true;
        }
        
        void move(char flag){
            int[][] b = state.board;
            int s=0;
            boolean valid = false;
            switch(flag){
                case 'L':
                    s = 0;
                    while(s<num_col-1){
                        for(int i=0;i<num_row;i++){
                            if(b[i][s]!=0) {
                                int sc = s+1;
                                while(sc<num_col && b[i][sc]==0) sc++;
                                if(sc<num_col && b[i][sc]==b[i][s]){ b[i][s]*=2; b[i][sc]=0; valid=true;}
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
                                if(sc>=0 && b[i][sc]==b[i][s]){ b[i][s]*=2; b[i][sc]=0; valid=true;}
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
                                if(sc<num_row && b[sc][i]==b[s][i]){ b[s][i]*=2; b[sc][i]=0; valid=true;}
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
                                if(sc<num_row && b[sc][i]!=0){ b[s][i]=b[sc][i]; b[sc][i]=0; valid=true;}
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
                                if(sc>=0 && b[sc][i]==b[s][i]){ b[s][i]*=2; b[sc][i]=0; valid=true;}
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
                                if(sc>=0 && b[sc][i]!=0){ b[s][i]=b[sc][i]; b[sc][i]=0; valid=true;}
                            }                    
                        }
                        s--;
                    }
                break;
            }
            status.setText("Move : "+flag);
            if(valid){
                if(placeRandom()) {
                    renderCells();
                    player.update(state);
                } else {
                    removeKeyListener(this);
                    JOptionPane.showMessageDialog(null, "Game over!");
                }
            }            
        }
}

class Game{
	public static void main(String args[])throws Exception {            
            State s = new State();
            Interface f = new Interface(s);
            f.setSize(400,400);
            f.setVisible(true);
            f.autoplay();
	}
}
