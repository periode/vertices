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
	float backdrop_rate = 0.05f;
	float backdrop_val = 0;
	boolean backdrop_expand = true;
	
	float threshold_h_r = 0.1f;
	float threshold_w_r = 0.1f;
	
	float z_depth = 11;
	
	float lower_limit;
	float upper_limit;
	
	float left_border;
	float top_border;
	float right_border;
	float bottom_border;
	
	float border_alpha = 0;
	
	
	Grid(PApplet _p){
		p = _p;
		cols = 160;
		rows = 100;
		
		xstep = p.width/cols;
		ystep = p.height/rows;
		
		points = new PVector[(1+cols)*(1+rows)];
		
		lower_limit = 0.925f;
		upper_limit = 0.075f;
		
		left_border = p.width*lower_limit;
		top_border = p.height*0.5f;
		right_border = p.width*upper_limit;
		bottom_border = p.height*0.5f;
		
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
			backdrop_h = PApplet.lerp(0, p.height*0.85f, PApplet.pow(backdrop_val, 2));
			top_border = p.height*PApplet.lerp(0.5f, lower_limit, PApplet.pow(backdrop_val, 2));
			bottom_border = p.height*PApplet.lerp(0.5f, upper_limit, PApplet.pow(backdrop_val, 2));
			
			if(backdrop_val < 1)
				backdrop_val += backdrop_rate;
			
			if(border_alpha < 255)
				border_alpha+=5f;
		}
		
//		threshold_w_r = PApplet.map(p.mouseX, 0, p.width, 0, 1);
//		threshold_h_r = PApplet.map(p.mouseY, 0, p.height, 0, 1);
	}
	
	void display(){
//		p.textAlign(PApplet.CENTER);
//		p.textSize(24);
		
		p.strokeWeight(2);
		p.stroke(255, 255);
		
		displayStrokes();
		
		
		displayParticles();
		
		p.fill(0);;
		p.noStroke();
		p.pushMatrix();
		p.translate(p.width*0.5f, p.height*0.5f, 10);
		p.rectMode(PApplet.CENTER);
		p.rect(0, 0, backdrop_w, backdrop_h);
		p.popMatrix();
		
		
		p.stroke(255);
		
		if(backdrop_expand)
			displayFrame();
		
	}
	
	void displayParticles(){
		p.stroke(255);
		p.strokeWeight(2);
		PVector v = new PVector(p.width*0.5f, p.height*0.5f);
		for(int i = 0; i < 1000; i++){
			int m = i % 4;
//			p.println(m);
			
			if(Vertices.cube != null){
				v = Vertices.cube.pos_abs[m].copy();
			}
				
			float l_val = PApplet.constrain(((i+p.millis()*0.001f)%1000f)*0.001f, 0f, 0.95f);
			if(m == 0){//top left
				p.point(PApplet.lerp(left_border, v.x, l_val), PApplet.lerp(top_border, v.y, l_val)+p.noise(p.millis()*0.025f, i*0.1f)*40*PApplet.sin(p.millis()*0.001f), z_depth);
			}else if (m == 1){//top right
				p.point(PApplet.lerp(right_border, v.x, l_val), PApplet.lerp(top_border, v.y, l_val)+p.noise(p.millis()*0.025f, i*0.1f)*40*PApplet.cos(p.millis()*0.001f), z_depth);
			}else if(m == 2){//bottom left
				p.point(PApplet.lerp(left_border, v.x, l_val), PApplet.lerp(bottom_border, v.y, l_val)+p.noise(p.millis()*0.025f, i*0.1f)*40*PApplet.cos(p.millis()*0.001f), z_depth);
			}else{//bottom right
				p.point(PApplet.lerp(right_border, v.x, l_val), PApplet.lerp(bottom_border, v.y,l_val)+p.noise(p.millis()*0.025f, i*0.1f)*40*PApplet.sin(p.millis()*0.001f), z_depth);
			}
		}
	}
	
	
	void displayStrokes(){
		p.strokeWeight(1);
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
	
	void displayFrame(){
		//top frame
				for(float i = 0 ; i < 1; i+=0.0075f){
					float lower_lerp_val = ((i+(p.millis()*0.0001f))%1f);
//					float upper_lerp_val = p.constrain(((i+0.025f+(p.millis()*0.0001f))%1f), lower_lerp_val, p.width*0.925f);
					float upper_lerp_val = ((i+0.005f+(p.millis()*0.0001f))%1f);
					
					if(p.noise(p.millis()*0.01f, i*1000f) > 0.1f)
						p.stroke(border_alpha);
					else
						p.stroke(0);
					
					if(upper_lerp_val > lower_lerp_val)
						p.line(PApplet.lerp(p.width*lower_limit, p.width*upper_limit, lower_lerp_val), top_border+1, z_depth, PApplet.lerp(p.width*lower_limit, p.width*upper_limit, upper_lerp_val), top_border+1, z_depth);
				}
				
				//lower frame
				for(float i = 1 ; i > 0; i-=0.0075f){
					float lower_lerp_val = ((i+(p.millis()*0.0001f))%1f);
//					float upper_lerp_val = p.constrain(((i+0.025f+(p.millis()*0.0001f))%1f), lower_lerp_val, p.width*0.925f);
					float upper_lerp_val = ((i+0.005f+(p.millis()*0.0001f))%1f);
					
					if(p.noise(p.millis()*0.01f, i*1000f) > 0.1f)
						p.stroke(border_alpha);
					else
						p.stroke(0);
					
					if(upper_lerp_val > lower_lerp_val)
						p.line(PApplet.lerp(p.width*upper_limit, p.width*lower_limit, lower_lerp_val), bottom_border+1, z_depth, PApplet.lerp(p.width*upper_limit, p.width*lower_limit, upper_lerp_val), bottom_border+1, z_depth);
				}
				
				//left frame
				for(float i = 1 ; i > 0; i-=0.0075f){
					float lower_lerp_val = ((i+(p.millis()*0.0001f))%1f);
//					float upper_lerp_val = p.constrain(((i+0.025f+(p.millis()*0.0001f))%1f), lower_lerp_val, p.width*0.925f);
					float upper_lerp_val = ((i+0.005f+(p.millis()*0.0001f))%1f);
					
					if(p.noise(p.millis()*0.01f, i*1000f) > 0.1f)
						p.stroke(border_alpha);
					else
						p.stroke(0);
					
					if(upper_lerp_val > lower_lerp_val)
						p.line(left_border, PApplet.lerp(bottom_border, top_border, lower_lerp_val), z_depth, left_border, PApplet.lerp(bottom_border, top_border, upper_lerp_val), z_depth);
				}
				
				//right frame
				for(float i = 1 ; i > 0; i-=0.0075f){
					float lower_lerp_val = ((i+(p.millis()*0.0001f))%1f);
//					float upper_lerp_val = p.constrain(((i+0.025f+(p.millis()*0.0001f))%1f), lower_lerp_val, p.width*0.925f);
					float upper_lerp_val = ((i+0.005f+(p.millis()*0.0001f))%1f);
					
					if(p.noise(p.millis()*0.01f, i*1000f) > 0.1f)
						p.stroke(border_alpha);
					else
						p.stroke(0);
					
					if(upper_lerp_val > lower_lerp_val)
						p.line(right_border, PApplet.lerp(top_border, bottom_border, lower_lerp_val), z_depth, right_border, PApplet.lerp(top_border, bottom_border, upper_lerp_val), z_depth);
				}
	}
}
