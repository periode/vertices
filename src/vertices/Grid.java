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
	float backdrop_rate = 0.025f;
	float backdrop_val = 0;
	boolean backdrop_expand = false;
	
	boolean backdrop_expand_outro = false;
	float backdrop_rate_outro = 0.0025f;
	
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
	
	float lfo_rate = 0.00001f;
	
	float particle_scale = 40;
	
	boolean canDisplayParticles = false;
	boolean canModuloParticles = false;
	
	
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
			if(backdrop_expand_outro)
				backdrop_w = PApplet.lerp(0, p.width*0.75f, p.pow(backdrop_val, 2));
			backdrop_h = PApplet.lerp(0, p.height*0.85f, PApplet.pow(backdrop_val, 2));
			top_border = p.height*PApplet.lerp(0.5f, lower_limit, PApplet.pow(backdrop_val, 2));
			bottom_border = p.height*PApplet.lerp(0.5f, upper_limit, PApplet.pow(backdrop_val, 2));
			
			if(backdrop_expand_outro){
				backdrop_val += backdrop_rate_outro;
				
				backdrop_w += backdrop_rate_outro*2f;
					
			}else{
				if(backdrop_val < 1)
					backdrop_val += backdrop_rate;
				
				if(border_alpha < 255)
					border_alpha+=5f;
			}

		}
		
		threshold_h_r = PApplet.map(PApplet.cos(p.millis()*lfo_rate), -1f, 1f, 0.1f, 0.85f);
		threshold_w_r = PApplet.map(PApplet.sin(p.millis()*lfo_rate), -1f, 1f, 0.1f, 0.85f);
		
		particle_scale = PApplet.map(p.mouseX, 0, p.width, 0, 200);
	}
	
	void display(){
//		p.textAlign(PApplet.CENTER);
//		p.textSize(24);
		

		
		p.fill(0);;
		p.noStroke();
		p.pushMatrix();
		p.translate(p.width*0.5f, p.height*0.5f, 10);
		p.rectMode(PApplet.CENTER);
		p.rect(0, 0, backdrop_w, backdrop_h);
		p.popMatrix();
		
		
		p.strokeWeight(2);
		p.stroke(255, 255);
		
		displayStrokes();
		
		
		if(canDisplayParticles)
			displayParticles();
		

		
		if(backdrop_expand)
			displayFrame();
		
	}
	
	void displayParticles(){
		p.stroke(255);
		p.strokeWeight(2);
		PVector v = new PVector(p.width*0.5f, p.height*0.5f);
		for(int i = 0; i < 1000; i++){
			int m = i % 8;
			
			if(Vertices.cube != null){
				v = Vertices.cube.pos_abs[m].copy();
			}
				
			if(canModuloParticles){
				p.stroke(PApplet.abs(((i - p.millis()*0.1f) % 255)));
			}
			
			
			float l_val = PApplet.constrain(((i+p.millis()*0.001f)%1000f)*0.001f, 0f, 0.95f);
			switch (m) {
			case 0://bottom right
				p.point(PApplet.lerp(left_border, v.x, l_val), PApplet.min(PApplet.lerp(top_border, v.y, l_val)+p.noise(p.millis()*0.025f, i*0.1f)*particle_scale*PApplet.sin(p.millis()*0.001f)*PApplet.map(i, 0, 1000, 0f, 2f)*PApplet.map(i, 1000, 0, 0f, 2f), top_border), z_depth);
				break;
			case 1:
				p.point(PApplet.lerp(left_border, v.x, l_val), PApplet.min(PApplet.lerp(top_border, v.y, l_val)+p.noise(p.millis()*0.025f, i*0.1f)*particle_scale*PApplet.sin(p.millis()*0.001f)*PApplet.map(i, 0, 1000, 0f, 2f)*PApplet.map(i, 1000, 0, 0f, 2f), top_border), z_depth);
				break;
			case 2://bottom left
				p.point(PApplet.lerp(right_border, v.x, l_val), PApplet.min(PApplet.lerp(top_border, v.y, l_val)+p.noise(p.millis()*0.025f, i*0.1f)*particle_scale*PApplet.sin(p.millis()*0.001f)*PApplet.map(i, 0, 1000, 0f, 2f)*PApplet.map(i, 1000, 0, 0f, 2f), top_border), z_depth);
				break;
			case 3:
				p.point(PApplet.lerp(right_border, v.x, l_val), PApplet.min(PApplet.lerp(top_border, v.y, l_val)+p.noise(p.millis()*0.025f, i*0.1f)*particle_scale*PApplet.sin(p.millis()*0.001f)*PApplet.map(i, 0, 1000, 0f, 2f)*PApplet.map(i, 1000, 0, 0f, 2f), top_border), z_depth);
				break;
			case 4://top right
				p.point(PApplet.lerp(left_border, v.x, l_val), PApplet.max(PApplet.lerp(bottom_border, v.y, l_val)+p.noise(p.millis()*0.025f, i*0.1f)*particle_scale*PApplet.sin(p.millis()*0.001f+PApplet.PI)*PApplet.map(i, 0, 1000, 0f, 2f)*PApplet.map(i, 1000, 0, 0f, 2f), bottom_border), z_depth);
				break;
			case 5:
				p.point(PApplet.lerp(left_border, v.x, l_val), PApplet.max(PApplet.lerp(bottom_border, v.y, l_val)+p.noise(p.millis()*0.025f, i*0.1f)*particle_scale*PApplet.sin(p.millis()*0.001f+PApplet.PI)*PApplet.map(i, 0, 1000, 0f, 2f)*PApplet.map(i, 1000, 0, 0f, 2f), bottom_border), z_depth);
				break;
			case 6://top left
				p.point(PApplet.lerp(right_border, v.x, l_val), PApplet.max(PApplet.lerp(bottom_border, v.y,l_val)+p.noise(p.millis()*0.025f, i*0.1f)*particle_scale*PApplet.sin(p.millis()*0.001f+PApplet.PI)*PApplet.map(i, 0, 1000, 0f, 2f)*PApplet.map(i, 1000, 0, 0f, 2f), bottom_border), z_depth);
				break;
			case 7:
				p.point(PApplet.lerp(right_border, v.x, l_val), PApplet.max(PApplet.lerp(bottom_border, v.y,l_val)+p.noise(p.millis()*0.025f, i*0.1f)*particle_scale*PApplet.sin(p.millis()*0.001f+PApplet.PI)*PApplet.map(i, 0, 1000, 0f, 2f)*PApplet.map(i, 1000, 0, 0f, 2f), bottom_border), z_depth);
				break;
			default:
				break;
			}
		}
	}
	
	
	void displayStrokes(){
		p.strokeWeight(1);
		for(int i = 1; i < points.length-1; i++){
			if(p.noise(i, p.millis()*0.003f) > threshold_h_r && points[i].y != p.height){
				
				p.line(points[i].x, points[i].y, points[i+1].x, points[i+1].y);
			}
			
			if(p.noise(i-1, p.millis()*0.0003f) > threshold_w_r && points[i].x != 0 && i > rows+1){
				
				p.line(points[i].x, points[i].y, points[i-rows-1].x, points[i-rows-1].y);
			}
		}
	}
	
	void displayFrame(){
		float lower_lerp_val;
		float upper_lerp_val;
		
		//top/bottom frame
		for(float i = 0 ; i < 1; i+=0.0075f){
			lower_lerp_val = ((i+(p.millis()*0.0001f))%1f);
			upper_lerp_val = ((i+0.005f+(p.millis()*0.0001f))%1f);
			
			if(p.noise(p.millis()*0.01f, i*1000f) > PApplet.min(threshold_h_r, 0.6f))
				p.stroke(border_alpha);
			else
				p.stroke(0);
			
			if(upper_lerp_val > lower_lerp_val){
				p.line(PApplet.lerp(p.width*lower_limit, p.width*upper_limit, lower_lerp_val), top_border+1, z_depth, PApplet.lerp(p.width*lower_limit, p.width*upper_limit, upper_lerp_val), top_border+1, z_depth);
				p.line(PApplet.lerp(p.width*upper_limit, p.width*lower_limit, lower_lerp_val), bottom_border+1, z_depth, PApplet.lerp(p.width*upper_limit, p.width*lower_limit, upper_lerp_val), bottom_border+1, z_depth);
			}
		}
		
		//left/right frame
		for(float i = 1 ; i > 0; i-=0.0075f){
			lower_lerp_val = ((i+(p.millis()*0.0001f))%1f);
			upper_lerp_val = ((i+0.005f+(p.millis()*0.0001f))%1f);
			
			if(p.noise(p.millis()*0.01f, i*1000f) > PApplet.min(threshold_w_r, 0.6f))
				p.stroke(border_alpha);
			else
				p.stroke(0);
			
			if(upper_lerp_val > lower_lerp_val){
				p.line(left_border, PApplet.lerp(bottom_border, top_border, lower_lerp_val), z_depth, left_border, PApplet.lerp(bottom_border, top_border, upper_lerp_val), z_depth);
				p.line(right_border, PApplet.lerp(top_border, bottom_border, lower_lerp_val), z_depth, right_border, PApplet.lerp(top_border, bottom_border, upper_lerp_val), z_depth);
			}
				
		}
	}
}
