package vertices;

import processing.core.PApplet;
import processing.core.PVector;

public class Block {
	PApplet p;
	PVector pos;
	float w;
	float h;
	
	int col;
	
	Block(){}
	
	Block(PVector _pos, float _w, float _h, int _col, PApplet _p){
		this.p = _p;
		this.pos = _pos;
		this.w = _w;
		this.h = _h;
		
		if(_col == 0)
			col = p.color(0, 0, 0);
		else
			col = p.color(0, 0, 100);
	}
	
	void display(){
		p.noStroke();
		p.fill(col);
		p.rect(this.pos.x, this.pos.y, this.w, this.h);
	}
}
