package vertices;

import processing.core.PApplet;
import processing.core.PVector;

public class Dash {

	PApplet p;
	
	PVector start;
	PVector end;
	
	float birth;
	float life;
	
	float orientation;
	float sc;
	
	Dash(PVector _s, PVector _e, PApplet _p){
		p = _p;
		start = _s;
		end = _e;
		
		birth = p.millis();
		life = 1000;
		
		orientation = p.random(1);
		sc = 0;
	}
	
	void display(){
		p.stroke(sc);
		if(orientation < 0.5f){
			p.strokeWeight(2);
		}else{
			p.strokeWeight(3);
		}
		p.line(start.x, start.y, end.x, end.y);
		
		if(sc < 255)
			sc+=10;
	}
}
