package vertices;

import processing.core.PApplet;
import processing.core.PVector;

public class Partition {
	PApplet p;

	PVector[] start_pos;
	PVector[] current_pos;
	PVector[] end_pos;

	float[] lerp_pos_val;
	float[] lerp_pos_inc;

	float start_h;
	float offset_h;
	float start_v;
	float offset_v;

	int num_lines;
	float spacing;

	float scaling;
	float sw;

	int var_h;
	int var_s;
	int var_b;

	int type;
	int orientation;

	boolean canRemove = false;

	float beat;
	
	float unifier = 1;

	static boolean unicolor = true;

	static boolean oscillating_space_cos = true;
	static boolean oscillating_weight_cos = true;

	Partition(){}

	Partition(int _num, float _offset, int _type, int _orientation, PApplet _p){
		this.p = _p;
		this.type = _type;
		this.orientation = _orientation;

		this.num_lines = _num;
		spacing = 10;

		start_pos = new PVector[num_lines];
		current_pos = new PVector[num_lines];
		end_pos = new PVector[num_lines];

		lerp_pos_val = new float[num_lines];
		lerp_pos_inc = new float[num_lines];

		if(type == 0){
			initializeHorizontal();
		}else if(type == 1){
			initializeVertical();
		}else{
			initializeDiagonal();
		}



		var_h = 0;
		var_s = 0;
		var_b = (int)p.random(-20, 20);

		scaling = 0;

		beat = 0;
	}

	void initializeHorizontal(){
		start_h = p.random(p.height*0.95f);
		offset_h = p.random(-p.height*0.1f, p.height*0.1f);

		if(orientation == 0){//left to right
			for(int i = 0; i < num_lines; i++){
				start_pos[i] = new PVector(0, start_h + i*spacing);
				current_pos[i] = new PVector(0, start_pos[i].y);
				end_pos[i] = new PVector(p.width, start_h + i*spacing + offset_h);
				lerp_pos_val[i] = 0f;
				lerp_pos_inc[i] = 0.01f + p.random(0.05f);
			}
		}else{//right to left
			for(int i = 0; i < num_lines; i++){
				start_pos[i] = new PVector(p.width, start_h + i*spacing);
				current_pos[i] = new PVector(p.width, start_pos[i].y);
				end_pos[i] = new PVector(0, start_h + i*spacing + offset_h);
				lerp_pos_val[i] = 0f;
				lerp_pos_inc[i] = 0.01f + p.random(0.05f);
			}
		}

	}

	void initializeVertical(){
		start_v = p.random(p.width);
		offset_v = p.random(-p.width*0.1f, p.width*0.1f);

		if(orientation == 0){//left to right
			for(int i = 0; i < num_lines; i++){
				start_pos[i] = new PVector(start_v + i*spacing, 0);
				current_pos[i] = new PVector(start_pos[i].x, 0);
				end_pos[i] = new PVector(start_v + i*spacing + offset_v, p.height);
				lerp_pos_val[i] = 0f;
				lerp_pos_inc[i] = 0.01f + p.random(0.05f);
			}
		}else{//right to left
			for(int i = 0; i < num_lines; i++){
				start_pos[i] = new PVector(start_v + i*spacing, p.height);
				current_pos[i] = new PVector(start_pos[i].x, p.height);
				end_pos[i] = new PVector(start_v + i*spacing + offset_v, 0);
				lerp_pos_val[i] = 0f;
				lerp_pos_inc[i] = 0.01f + p.random(0.05f);
			}
		}
	}

	void initializeDiagonal(){
		start_v = p.random(p.width);
		offset_v = p.random(-p.width*0.1f, p.width*0.1f);

		if(orientation == 0){//left to right
			for(int i = 0; i < num_lines; i++){
				start_pos[i] = new PVector(start_v + i*spacing, 0);
				current_pos[i] = new PVector(start_pos[i].x, 0);
				end_pos[i] = new PVector(start_h + i*spacing + offset_h, p.height);
				lerp_pos_val[i] = 0f;
				lerp_pos_inc[i] = 0.01f + p.random(0.05f);
			}
		}else if(orientation == 1){//right to left
			for(int i = 0; i < num_lines; i++){
				start_pos[i] = new PVector(start_v + i*spacing, p.height);
				current_pos[i] = new PVector(start_pos[i].x, p.height);
				end_pos[i] = new PVector(start_h + i*spacing, 0);
				lerp_pos_val[i] = 0f;
				lerp_pos_inc[i] = 0.01f + p.random(0.05f);
			}
		}else if(orientation == 2){
			for(int i = 0; i < num_lines; i++){
				start_pos[i] = new PVector(start_v + i*spacing, 0);
				current_pos[i] = new PVector(start_pos[i].x, 0);
				end_pos[i] = new PVector(p.width + i*spacing, p.height);
				lerp_pos_val[i] = 0f;
				lerp_pos_inc[i] = 0.01f + p.random(0.05f);
			}
		}else{
			for(int i = 0; i < num_lines; i++){
				start_pos[i] = new PVector(start_v + i*spacing, p.height);
				current_pos[i] = new PVector(start_pos[i].x, 0);
				end_pos[i] = new PVector(p.width+ i*spacing, 0);
				lerp_pos_val[i] = 0f;
				lerp_pos_inc[i] = 0.01f + p.random(0.05f);
			}
		}
	}

	void update(){
		beat += PApplet.TWO_PI * Vertices.bpm;
		//		col = p.color(Vertices.p_h+var_h, Vertices.p_s+var_s, Vertices.p_b+var_b);

		if(oscillating_space_cos)
			scaling = Vertices.p_spacing*unifier + PApplet.tan(beat)*Vertices.p_spacing_coeff;
		else
			scaling = Vertices.p_spacing*unifier + PApplet.cos(beat)*Vertices.p_spacing_coeff;

		if(oscillating_weight_cos)	
			sw = Vertices.p_sw+ PApplet.cos(beat)*Vertices.p_sw_coeff;
		else
			sw = Vertices.p_sw+ PApplet.tan(beat)*Vertices.p_sw_coeff;

		if(this.type == 0){
			for(int i =0; i < num_lines; i++){
				lerp_pos_inc[i] = Vertices.p_lerp_inc_h + p.random(0.05f);
			}
		}else if(type == 1){
			for(int i =0; i < num_lines; i++){
				lerp_pos_inc[i] = Vertices.p_lerp_inc_v + p.random(0.05f);
			}
		}

		if(!canRemove){
			for(int i = 0; i < num_lines; i++){
				if(lerp_pos_val[i] < 1)
					lerp_pos_val[i] += lerp_pos_inc[i];

				current_pos[i] = PVector.lerp(start_pos[i], end_pos[i], lerp_pos_val[i]);	
			}
		}else{
			for(int i = 0; i < num_lines; i++){
				if(lerp_pos_val[i] > -0.5f)
					lerp_pos_val[i] -= lerp_pos_inc[i];
				else
					Vertices.partitions.remove(this);

				current_pos[i] = PVector.lerp(start_pos[i], end_pos[i], lerp_pos_val[i]);	
			}
		}
	}

	void display(){
		p.stroke(255, Vertices.p_alpha);
		p.strokeWeight(sw);
		p.pushMatrix();
		for(int i = 0; i < num_lines; i++){
			if(type != 0)
				p.line(start_pos[i].x+(i-num_lines*0.5f)*scaling*unifier, start_pos[i].y, current_pos[i].x+(i-num_lines*0.5f)*scaling*unifier, current_pos[i].y);
			else
				p.line(start_pos[i].x, start_pos[i].y+((i-num_lines*0.5f)*scaling)*unifier, current_pos[i].x, current_pos[i].y+(i-num_lines*0.5f)*scaling*unifier);
		}
		p.popMatrix();
		
	}

	public void resetLerp(){
		for(int i = 0; i < lerp_pos_val.length; i++){
			lerp_pos_val[i] = 0;
		}
	}
}
