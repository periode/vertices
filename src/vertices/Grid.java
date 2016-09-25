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
	boolean backdrop_expand = true;
	
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
	
	float[] tunnel_depth;
	float[] tunnel_accel;
	
	boolean canDisplayParticles = false;
	boolean canModuloParticles = false;
	boolean canDisplayTunnel = true;
	
	float tunnel_alpha_coeff = 0;
	float tunnel_edges_alpha_coeff = 0;
	float tunnel_perspective_alpha_coeff = 0;
	float tunnel_alpha_inc = 0.01f;
	
	float modulo_step = 0;
	int[] tunnel_step;
	float[] tunnel_lerp_val;
	float[] tunnel_lerp_inc;
	int[] tunnel_dir;
	PVector[] tunnel_pos;
	PVector[] tunnel_start;
	PVector[] tunnel_target;
	
	
	Grid(PApplet _p){
		p = _p;
		cols = 160;
		rows = 100;
		
		xstep = p.width/cols;
		ystep = p.height/rows;
		
		points = new PVector[(1+cols)*(1+rows)];
		
		lower_limit = 1f;
		upper_limit = 0.00f;
		
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
		
		tunnel_depth = new float[12];
		tunnel_accel = new float[12];
		
		tunnel_pos = new PVector[12];
		tunnel_start = new PVector[12];
		tunnel_target = new PVector[12];
		tunnel_lerp_val = new float[12];
		tunnel_lerp_inc = new float[12];
		tunnel_step = new int[12];
		tunnel_dir = new int[12];
		
		for(int j = 0;j<tunnel_depth.length;j++){
			tunnel_accel[j] = 0.0075f;
			tunnel_depth[j] = PApplet.map(j, 0, tunnel_depth.length, 0, 1);
			tunnel_pos[j] = new PVector(0, 0);
			tunnel_start[j] = new PVector(0, 0);
			tunnel_target[j] = new PVector(0, 0);
			tunnel_lerp_val[j] = 0;
			tunnel_step[j] = (int)p.random(4);
			tunnel_lerp_inc[j] = 0.075f;
			tunnel_dir[j] = 0;
		}
		

	}
	
	void update(){
		if(backdrop_expand){
			if(backdrop_expand_outro)
				backdrop_w = PApplet.lerp(0, p.width*0.75f, p.pow(backdrop_val, 2));
			
			backdrop_h = PApplet.lerp(0, p.height*0.95f, PApplet.pow(backdrop_val, 2));
			backdrop_w = PApplet.lerp(0, p.width*0.95f, PApplet.pow(backdrop_val, 2));
			
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
		
		if(canDisplayTunnel){
			if(tunnel_alpha_coeff < 1){
				tunnel_alpha_coeff += tunnel_alpha_inc;
				tunnel_edges_alpha_coeff += tunnel_alpha_inc;
				tunnel_perspective_alpha_coeff += tunnel_alpha_inc;	
			}
		}else{
			if(tunnel_alpha_coeff > 0){
				tunnel_alpha_coeff -= tunnel_alpha_inc;
				tunnel_edges_alpha_coeff -= tunnel_alpha_inc;
				tunnel_perspective_alpha_coeff -= tunnel_alpha_inc;	
			}
		}
		
		threshold_h_r = PApplet.map(PApplet.cos(p.millis()*lfo_rate), -1f, 1f, 0.1f, 0.85f);
		threshold_w_r = PApplet.map(PApplet.sin(p.millis()*lfo_rate), -1f, 1f, 0.1f, 0.85f);
		
		particle_scale = PApplet.map(p.mouseX, 0, p.width, 0, 200);
	}
	
	void display(){
//		p.textAlign(PApplet.CENTER);
//		p.textSize(24);
		

		
		p.fill(0);
		p.noStroke();
		p.pushMatrix();
		p.translate(p.width*0.5f, p.height*0.5f, 5);
		p.rectMode(PApplet.CENTER);
		p.rect(0, 0, backdrop_w, backdrop_h);
		p.popMatrix();
		
		
		p.strokeWeight(2);
		p.stroke(255);
		
		displayStrokes();
		
		
		if(canDisplayParticles)
			displayParticles();
		

		
		if(backdrop_expand)
			displayFrame();
		
		
		if(canDisplayTunnel)
			displayTunnel();
	}
	
	void displayTunnel(){
		for(int i = 0; i < tunnel_depth.length; i++){
			p.strokeWeight(1);
			p.pushMatrix();
			p.translate(p.width*0.5f, p.height*0.5f, 10);
			p.rectMode(PApplet.CENTER);

			float w = PApplet.map(tunnel_depth[i], 0, 1, 0, p.width);
			float h = PApplet.map(tunnel_depth[i], 0, 1, 0, p.height);
			float w2 = 0;
			float h2 = 0;
			if(i < tunnel_depth.length-1){
				w2 = PApplet.map(tunnel_depth[i+1], 0, 1, 0, p.width);
				h2 = PApplet.map(tunnel_depth[i+1], 0, 1, 0, p.height);
			}else{
				w2 = PApplet.map(tunnel_depth[0], 0, 1, 0, p.width);
				h2 = PApplet.map(tunnel_depth[0], 0, 1, 0, p.height);
			}
//			float r = p.noise(p.millis()*0.01f+i*0.5f)
//			float r = PApplet.map(PApplet.cos(p.millis()*0.0005f+PApplet.map(i, 0, tunnel_depth.length, 0, PApplet.PI/tunnel_depth.length)), -1f, 1f, 0f, 1f);
//			float r = PApplet.map(i, 0, tunnel_depth.length, 0, 1f);
			float c = PApplet.map(tunnel_depth[i], 0, 1, 0, 105)*tunnel_alpha_coeff;
			
			p.stroke(c);
			p.strokeWeight(1);
//			p.rect(0, 0, w, h);
			

			
			
			p.strokeWeight(3);
			p.stroke((int)(c*2f)*tunnel_perspective_alpha_coeff);
			
			if(w2 > w && p.noise(p.millis()*0.001f, i*0.75f) > 0.5f){
				p.line(-w*0.5f, h*0.5f, -w2*0.5f, h2*0.5f);
				p.line(-w*0.5f, -h*0.5f, -w2*0.5f, -h2*0.5f);
				p.line(w*0.5f, -h*0.5f, w2*0.5f, -h2*0.5f);
				p.line(w*0.5f, h*0.5f, w2*0.5f, h2*0.5f);
			}

			
			if(tunnel_step[i] == 0){
				tunnel_start[i] = new PVector(-w*0.5f*tunnel_dir[i], -h*0.5f);
				tunnel_target[i] = new PVector(w*0.5f*tunnel_dir[i], -h*0.5f);
			}else if(tunnel_step[i] == 1){
				tunnel_start[i] = new PVector(w*0.5f, -h*0.5f*tunnel_dir[i]);
				tunnel_target[i] = new PVector(w*0.5f, h*0.5f*tunnel_dir[i]);
			}else if(tunnel_step[i] == 2){
				tunnel_start[i] = new PVector(w*0.5f*tunnel_dir[i], h*0.5f);
				tunnel_target[i] = new PVector(-w*0.5f*tunnel_dir[i], h*0.5f);
			}else if(tunnel_step[i] == 3){
				tunnel_start[i] = new PVector(-w*0.5f, h*0.5f*tunnel_dir[i]);
				tunnel_target[i] = new PVector(-w*0.5f, -h*0.5f*tunnel_dir[i]);
			}
			
			
			p.stroke((int)(c*2f)*tunnel_edges_alpha_coeff);
			
			if(tunnel_lerp_val[i] < 1){
				tunnel_lerp_val[i] += tunnel_lerp_inc[i];
				tunnel_pos[i] = PVector.lerp(tunnel_start[i], tunnel_target[i], tunnel_lerp_val[i]);
				
				if(p.noise(tunnel_step[i], i+p.millis()*0.001f) > 0.5f)
					p.line(tunnel_start[i].x, tunnel_start[i].y, tunnel_pos[i].x, tunnel_pos[i].y);
			}else if(tunnel_lerp_val[i] < 2){
				tunnel_lerp_val[i] += tunnel_lerp_inc[i];
				tunnel_pos[i] = PVector.lerp(tunnel_start[i], tunnel_target[i], tunnel_lerp_val[i]-1);
				
				if(p.noise(tunnel_step[i], i+p.millis()*0.001f) > 0.5f)
					p.line(tunnel_target[i].x, tunnel_target[i].y, tunnel_pos[i].x, tunnel_pos[i].y);
			}else{
				tunnel_step[i] = (int)p.random(4);
				tunnel_lerp_inc[i] = p.random(0.05f, 0.1f);
				tunnel_dir[i] = (int)p.random(-2, 2);

				
				tunnel_lerp_val[i] = 0;
			}
			
			
			
			p.popMatrix();

			if(tunnel_depth[i] < 1){
				tunnel_depth[i] += tunnel_accel[i];
//				tunnel_accel[i] += 0.001f;
			}else{
				tunnel_depth[i] = 0;
//				tunnel_accel[i] = 0;
			}
		}
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
				p.stroke(255, border_alpha);
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
				p.stroke(255, border_alpha);
			else
				p.stroke(0);
			
			if(upper_lerp_val > lower_lerp_val){
				p.line(left_border, PApplet.lerp(bottom_border, top_border, lower_lerp_val), z_depth, left_border, PApplet.lerp(bottom_border, top_border, upper_lerp_val), z_depth);
				p.line(right_border, PApplet.lerp(top_border, bottom_border, lower_lerp_val), z_depth, right_border, PApplet.lerp(top_border, bottom_border, upper_lerp_val), z_depth);
			}
				
		}
	}
}
