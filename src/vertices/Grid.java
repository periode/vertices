package vertices;

import processing.core.PApplet;
import processing.core.PVector;

public class Grid {
	
	PApplet p;
	PVector[] points;
	
	int cols;
	int rows;
	
	float xstep;
	float ystep;
	
	float rad = 1;
	
	
	Grid(PApplet _p){
		p = _p;
		cols = 160;
		rows = 100;
		
		xstep = p.width/cols;
		ystep = p.height/rows;
		
		points = new PVector[(1+cols)*(1+rows)];
		
		
		
		int i = 0;
		for(int x = 0; x <= cols; x++){
			for(int y = 0; y <= rows; y++){
				points[i] = new PVector(x*xstep, y*xstep);
				i++;
			}
		}
	}
	
	void update(){
		
	}
	
	void display(){
		p.textAlign(p.CENTER);
		p.textSize(14);
		p.fill(255);
		for(int i = 0; i < points.length; i++){
//			p.println("so far so good: "+i);
			if(points[i] != null)
				p.text(i, points[i].x, points[i].y);
			else
				p.println("null");
		}
	}
}
