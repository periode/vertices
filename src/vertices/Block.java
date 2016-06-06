package vertices;

import processing.core.PApplet;
import processing.core.PVector;

public class Block {
	PApplet p;
	PVector pos;
	float w;
	float h;
	
	int col;
	int scol;
	float alpha = 255;
	
	boolean fading = false;
	int index = 0;
	
	Block(){}
	
	Block(PVector _pos, float _w, float _h, int _col, int _i, PApplet _p){
		this.index = _i;
		this.p = _p;
		this.pos = _pos;
		this.w = _w;
		this.h = _h;
		
		p.colorMode(p.HSB, 360, 100, 100);
		scol = p.color(0, 0, 100*_col);
		col = p.color(0, 0, p.abs(100-100*_col));
	}
	
	void update(){
		alpha-=0.25f;
		if(fading){
			if(alpha > 250)
				alpha-=60;
			else{
				alpha -= 0.1f;
			}
				if(this.w > 0){
					this.w-=1;
					this.h-=1;
				}
		}
	}
	
	void display(){
//		p.noStroke();
		p.strokeWeight(2);
		p.stroke(col, alpha);
//		p.fill(col, alpha);
//		p.rectMode(p.CENTER);
		p.noFill();
		p.rect(this.pos.x, this.pos.y, this.w, this.h);
	}
}
