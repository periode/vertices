package vertices;

import processing.core.PApplet;
import processing.core.PVector;

public class Block {
	PApplet p;
	PVector pos;
	float w;
	float h;
	int dirH;
	int dirW;
	
	int col;
	int scol;
	float alpha = 255;
	float saved_alpha = 255;
	
	boolean fading = false;
	int index = 0;
	
	Block(){}
	
	Block(PVector _pos, float _w, float _h, int _col, int _i, PApplet _p){
		this.index = _i;
		this.p = _p;
		this.pos = _pos;
		this.w = _w;
		this.h = _h;
		float r = p.random(1);
		if(r<0.25){
			this.dirH = 1;
			this.dirW = 1;
		}else if(r < 0.5){
			this.dirH = -1;
			this.dirW = -1;
		}else if(r < 0.75){
			this.dirH = -1;
			this.dirW = 1;
		}else{
			this.dirH = 1;
			this.dirW = -1;
		}
		
		scol = p.color(0, 0, 100*_col);
		col = p.color(255);
	}
	
	void update(){
		alpha-=0.1f;
		if(fading){
			if(alpha > 250)
				alpha-=60;
			else{
				alpha -= 0.4f;
			}
		}else{
			if(alpha < 255)
				alpha+=3;
		}
		
	}
	
	void display(){
		p.strokeWeight(6);
		p.stroke(col, alpha);
		p.noFill();
		p.rect(this.pos.x, this.pos.y, p.constrain(this.w, 0, 2), p.constrain(this.h, 0, 2));
	}
}
