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
	
	float backdrop_w = 0;
	float backdrop_h = 0;
	float backdrop_rate = 0.008f;
	float backdrop_val = 0;
	boolean backdrop_expand = true;
	
	float threshold_h_r = 0.8f;
	float threshold_w_r = 0.8f;
	
	
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
		backdrop_w=p.width*0.85f;
	}
	
	void update(){
		if(backdrop_expand){
//			backdrop_w = p.lerp(0, p.width*0.75f, p.pow(backdrop_val, 2));
			backdrop_h = p.lerp(0, p.height*0.85f, p.pow(backdrop_val, 2));
			
			if(backdrop_val < 1)
				backdrop_val += backdrop_rate;
		}
	}
	
	void display(){
		p.textAlign(p.CENTER);
		p.textSize(24);
		
		p.strokeWeight(2);
		p.stroke(255, 255);
		
//		displayStrokes();
		
		p.fill(55);
		p.noStroke();
		p.pushMatrix();
		p.translate(p.width*0.5f, p.height*0.5f, 10);
		p.rectMode(p.CENTER);
		p.rect(0, 0, backdrop_w, backdrop_h);
		p.popMatrix();
		
//		if(p.noise(p.millis()*0.1f) > 0.2f){
//			p.stroke(255);
//			p.line(p.lerp(p.width*0.15f, p.width*0.5f, p.random(1)), p.height*0.15f, p.lerp(p.width*0.5f, p.width*0.85f, p.random(1)), p.height*0.15f);
//		}
		
		p.stroke(255);
		for(float i = 0 ; i < 1; i+=0.0075f){
			float lower_lerp_val = ((i+(p.millis()*0.0001f))%1f);
//			float upper_lerp_val = p.constrain(((i+0.025f+(p.millis()*0.0001f))%1f), lower_lerp_val, p.width*0.925f);
			float upper_lerp_val = ((i+0.005f+(p.millis()*0.0001f))%1f);
			
			if(upper_lerp_val > lower_lerp_val)
				p.line(p.lerp(p.width*0.075f, p.width*0.925f, lower_lerp_val), p.height*0.075f+1, 11, p.lerp(p.width*0.075f, p.width*0.925f, upper_lerp_val), p.height*0.075f+1, 11);
		}
		
		for(float i = 1 ; i > 0; i-=0.0075f){
			float lower_lerp_val = ((i+(p.millis()*0.0001f))%1f);
//			float upper_lerp_val = p.constrain(((i+0.025f+(p.millis()*0.0001f))%1f), lower_lerp_val, p.width*0.925f);
			float upper_lerp_val = ((i+0.005f+(p.millis()*0.0001f))%1f);
			
			if(p.noise(p.millis()*0.01f, i*1000f) > 0.6f)
				p.stroke(255);
			else
				p.stroke(0);
			
			if(upper_lerp_val > lower_lerp_val)
				p.line(p.lerp(p.width*0.925f, p.width*0.075f, lower_lerp_val), p.height*0.925f+1, 11, p.lerp(p.width*0.925f, p.width*0.075f, upper_lerp_val), p.height*0.925f+1, 11);
		}
	}
	
	void displayStrokes(){
		for(int i = 1; i < points.length-1; i++){
			
			
			if(p.noise(i, p.millis()*0.003f) > threshold_h_r && points[i].y != p.height){
				
				p.line(points[i].x, points[i].y, points[i+1].x, points[i+1].y);
			}
//			if(p.noise(i, p.millis()*0.003f) > 0.7f && points[i].x != p.width && i < points.length-rows-1){
//				
//				p.line(points[i].x, points[i].y, points[i+rows+1].x, points[i+rows+1].y);
//			}
			if(p.noise(i-1, p.millis()*0.0003f) > threshold_w_r && points[i].x != 0 && i > rows+1){
				
				p.line(points[i].x, points[i].y, points[i-rows-1].x, points[i-rows-1].y);
			}
		}
	}
}
