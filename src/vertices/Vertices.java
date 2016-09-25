package vertices;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import themidibus.MidiBus;


public class Vertices extends PApplet {

	MidiBus midi_beatstep;
	MidiBus midi_kontrol;
	
	boolean entracte =  true;

	int channel;
	int pitch;
	int velocity;
	int note;
	int value;

	boolean intro = false;
	boolean intro_pixels = false;
	boolean outro = false;
	PVector intro_pos;
	float intro_height = height*0.275f;
	float intro_width = width*0.225f;

	static ArrayList<Block> blocks;
	float blocks_start = 10*1000f;
	float blocks_timer = 1;
	World world;
	
	ArrayList<Dash> dashes;
	float dash_start = 0;
	float dash_timer = 1;
	int dash_seed;
	
	Grid grid;

	static float bpm;
	float bpm_coeff;

	static ArrayList<Partition> partitions;
	static ArrayList<Cube> cubes;
	static public Cube cube;



	static int p_h = 50;
	static int p_s = 10;
	static int p_b = 50;

	static float p_sw = 1;
	static float p_sw_coeff;

	static float p_spacing;
	static float p_spacing_coeff;

	static float p_alpha = 255;

	static float p_lerp_inc_h;
	static float p_lerp_inc_v;

	static int p_num_lines = 1;
	static float p_offset = 10;

	//---- CUBE
	static float c_thetaX_coeff;
	static float c_thetaY_coeff;
	static float c_thetaZ_coeff;

	static float c_depth;
	static float c_height;
	
	//---- TIME
	static float ts_intro_open_filter = 42*1000f; //0'40"
	static float ts_open_canvas =  77*1000f; // 1'17"
	static float ts_show_line = 79*1000f; //1'19"
	static float ts_rotate_1D = 81*1000f;
	
	static float ts_show_square = 85*1000f;
	static float ts_rotate_2D = 86*1000f;
	static float ts_drums_in_1 = 86*1000f;
	
	static float ts_drums_out_1 = 94*1000f;
//	static float ts_rotate_3D = 94*1000f;
	static float ts_rotate_3D = 0f;
	
	static float ts_drums_in_2 = 102*1000f;
	static float ts_drums_out_2 = 110*1000f;
	
	static float ts_drums_in_3 = 118*1000f;
	static float ts_drums_out_3 = 126*1000f;
	
	static float ts_arpeggios_in = 126*1000f;
	static float ts_arpeggios_out = 198*1000f;
	
	//break
	static float ts_break_cut_kick = 134*1000f;
	static float ts_break_cut_drums = 142*1000f;
	static float ts_break_full_cut = 158*1000f;
	static float ts_break_swishes = 166*1000f;
	
	static float ts_kick_back = 171*1000f;
	
	static float ts_drums_in_4 = 181*1000f;
	static float ts_drums_out_4 = 188*1000f;
	
	static float ts_pads_post_cut = 196*1000f;
	
	static float ts_start_fizzle = 212*1000f;
	static float ts_drums_in_5 = 212*1000f;
	static float ts_drums_out_5 = 218*1000f;
	
	static float ts_start_fizzle_more = 226*1000f;
	
	static float ts_drums_in_6 = 236*1000f;
	static float ts_drums_out_6 = 244*1000f;
	
	static float ts_congas_vertices = 244*1000f;
	
	static float ts_outro_kick_cut = 260*1000f;
	static float ts_outro_fade_out = 276*1000f;
	
	
	float bpm_start_0 = 0;
	float bpm_timer_0 = 1000;
	
	float bpm_start_1 = 0;
	float bpm_timer_1 = 500;
	
	float bpm_start_2 = 0;
	float bpm_timer_2 = 250;
	
	float bpm_start_4 = 0;
	float bpm_timer_4 = 125;
	

	public void setup() {
		noCursor();
//		MidiBus.list();
//		midi_beatstep = new MidiBus(this, "Arturia BeatStep", 1);
//		midi_kontrol = new MidiBus(this, "SLIDER/KNOB", 2);
//
//		midi_beatstep.setBusName("beatstep");
//		midi_kontrol.setBusName("kontrol");

		bpm = 0.037f;
		
		bpm_start_0 = millis();
		bpm_start_1 = millis();
		bpm_start_2 = millis();
		bpm_start_4 = millis();
		
		grid = new Grid(this);

		intro_pos = new PVector(-intro_width, 0);
		blocks = new ArrayList<Block>();
		int i = 0;
		for(float x = 0.5f; x < grid.cols; x++){
			for(float y = 0.5f; y < grid.rows; y++){
				blocks.add(new Block(new PVector(x*grid.xstep, y*grid.xstep), grid.xstep, grid.xstep, 0, i, this));
				i++;
			}
		}
		
		dash_seed = (int)random(grid.points.length-grid.cols);
			
		dashes = new ArrayList<Dash>();

		partitions = new ArrayList<Partition>();

		cube = new Cube(this);
		cubes = new ArrayList<Cube>();

		
		ellipseMode(CENTER);
		
	}

	public void settings(){
		fullScreen(P3D);
	}

	void introPixels(){
		if(intro_pos.x < width && noise(frameCount*mouseX*1f) > map(mouseY, 0, height, 1, 0)){
			blocks.add(new Block(intro_pos.copy(), intro_width, intro_height, 0, 0, this));
		}else if(intro_pos.x > width){
			intro_pos.y += intro_height;
			intro_pos.x = -intro_width;
			if(intro_pos.y > height){
				intro_pos.y = 0;
				intro_height *= 2f;
			}
		}

		intro_pos.x += intro_width;
	}

	void outroPixels(){
		if(intro_pos.x < width && noise(frameCount*mouseX*1f) > map(mouseY, 0, height, 1, 0)){
			blocks.add(new Block(intro_pos.copy(), intro_width, intro_height, 1, 0, this));
		}else if(intro_pos.x > width){
			intro_pos.y += intro_height;
			intro_pos.x = -intro_width;
			if(intro_pos.y > height){
				intro_pos.y = 0;
				intro_height *= 2f;
			}
		}

		intro_pos.x += intro_width;
	}

	public void debug(){
		textAlign(LEFT);
		textSize(14);
		fill(0, 255, 0);
		text("framerate: "+frameRate, 10, 10);
		text("ch: "+channel, 10, 20);
		text("pitch: "+pitch, 10, 30);
		text("vel: "+velocity, 10, 40);
		text("note: "+note, 10, 50);
		text("val: "+value, 10, 60);
		text("partitions: "+partitions.size(), 10, 70);
		text("hsb part:" +p_h+"/"+p_s+"/"+p_b, 10, 80);
		text("rad.z: "+cube.rad.x, 10, 90);
		text("rad.y: "+cube.rad.y, 10, 100);
		text("max_width: "+cube.max_width, 10, 110);
		text("max_depth: "+cube.max_depth, 10, 120);
		text("blocks: "+blocks.size(), 10, 130);
	}

	public void update(){
//		timer_events();
		behavior();
		
		
		
		for(int i = 0; i < partitions.size(); i++){
			if(partitions.get(i) != null)
				partitions.get(i).update();
		}

		if(cube != null)
			cube.update();
		
		grid.update();
		
		for(int i = 0; i < blocks.size(); i++){
			if(blocks.get(i).alpha < 0)
				blocks.remove(i);
			else
				blocks.get(i).update();
		}

//		if(intro && intro_pixels)
//			introPixels();
//
//		if(outro)
//			outroPixels();

		if(world != null)
			world.update();
		
		for(int i = 0; i < dashes.size(); i++){
			Dash d = dashes.get(i);
			if(millis() - d.birth > d.life){
				dashes.remove(i);
			}
		}

		
		
		if(millis() - dash_start > dash_timer){
			addDash();
			dash_start = millis();
		}
			
		
		if(millis() - blocks_start > blocks_timer){
			removeBlocks();
			blocks_start = millis();
		}
	}
	
	public boolean timer(float timer, float start){
		if(millis() - start > timer){
			start = millis();
			return true;
		}else{
			return false;
		}
	}

	public void drawBackground(){
//		pushMatrix();
//		fill(0);
//		rectMode(CENTER);
//		rect(width*0.5f, height*0.5f, -1000, width*2, height*2);
//		popMatrix();
		background(0);
	}
	
	public void draw() {
		noCursor();
		update();

		drawBackground();


		for(int i = 0; i < partitions.size(); i++){
			partitions.get(i).display();
		}
		
		grid.display();
		
		if(cube != null && grid.backdrop_expand)
			cube.display();

		
		if(entracte){
			fill(0);
			noStroke();
			rect(0, 0, width*2, height*2);
		}
		
//		displayBlocks();
//		debug();
	}
	
	void displayBlocks(){
		for(int i = 0; i < blocks.size(); i++){
			Block b = blocks.get(i);
			
			//after a given moment, if not fading
			if(millis() > ts_intro_open_filter && !b.fading){
				//slow noise, increased by a mapped version of i so that we don't get the noise of reducing blocks.size()
				//and the threshold increased as time goes by
				if(noise((millis()-ts_intro_open_filter)*0.0000001f*map(i, 0, blocks.size(), 0, grid.cols*grid.rows*0.825f)) < map(millis(), ts_intro_open_filter, ts_open_canvas, 0.0f, 0.8f)){
					//do not display
					//or do something interesting?
				}else{
					b.display();
				}
			}else{
				b.display();
			}
			
			float start_modulo_wave = 6*1000f;
			float end_modulo_wave = 110*1000f;
			
			if(i % (int)(millis()*0.01f) < 3)//gradually accentuating the modulo pattern
				b.alpha *= map(constrain(millis(), start_modulo_wave, end_modulo_wave), start_modulo_wave, end_modulo_wave, 1.0f, 0.05f);
		}
	}
	
	void timer_events(){
		
		if(millis() > ts_open_canvas)
			grid.backdrop_expand = true;
		
		
		//show cube
		if(millis() > ts_show_line && cube.canExpand.x != 1){
			cube.canRotateStep = true;
			cube.canShowEdges = true;
			cube.canExpand.x = 1;
		}
		
		if(millis() > ts_rotate_2D)
			cube.canExpand.y = 1;
		
		if(millis() > ts_rotate_3D)
			cube.canExpand.z = 1;
		
		//drums
		if(millis() > ts_drums_in_1 && millis() < ts_drums_out_1
				|| millis() > ts_drums_in_2 && millis() < ts_drums_out_2
				|| millis() > ts_drums_in_3 && millis() < ts_drums_out_3
				|| millis() > ts_drums_in_4 && millis() < ts_drums_out_4
				|| millis() > ts_drums_in_5 && millis() < ts_drums_out_5
				|| millis() > ts_drums_in_6 && millis() < ts_drums_out_6){
			Cube.isDrumming = true;
		}else{
			Cube.isDrumming = false;
		}
		
		if(millis() > ts_arpeggios_in && millis() < ts_arpeggios_out){
			grid.canDisplayParticles = true;
			grid.canModuloParticles = true;
		}
		
		if(millis() > ts_break_cut_kick && millis() < ts_kick_back){
//			cube.canRotateStep = false;
			cube.canShowCircles = true;
			cube.canShowEdges = false;
		}
		
		if(millis() > ts_break_cut_drums){
			cube.canRotateStep = false; 
			cube.constant_rotateX = true;
		}
		
		if(millis() > ts_break_full_cut){
			cube.constant_rotateY = true;
			cube.constant_rotateZ = true;
		}
		
		if(millis() > ts_break_swishes && cube.canShowCircles){
			cube.canFizzleSphere = true;
		}
		
		if(millis() > ts_kick_back){
			
			cube.canShowCircles = false;
			cube.canShowEdges = true;
			cube.canShowDiagonals = true;
		}
		
		if(millis() > ts_pads_post_cut){
			cube.canRotateStep = true;
			cube.distortVertices = 1;
		}
		
		if(millis() > ts_start_fizzle){
			cube.canFizzleCube = true;
		}
		
		if(millis() > ts_start_fizzle_more){
			cube.canFizzleMoreCube = true;
		}
		
		if(millis() > ts_congas_vertices){
			cube.distortVertices = 2;
		}
		
		if(millis() > ts_outro_kick_cut){
			cube.canRotateStep = false;
			cube.canShowCircles = false;
			cube.canShowDiagonals = false;
			cube.canShowEdges = false;
		}
		
		if(millis() > ts_outro_fade_out){
			grid.backdrop_expand_outro = true;
		}
		
	}
	
	void behavior(){
		if(beat(1) && cube.canRotateStep){
			if(millis () > ts_rotate_3D){
				float r = random(1);
				if(r < 0.33f){
					cube.rotateCube("X", 1);
				}else if(r < 0.66f){
					cube.rotateCube("Y", 1);
				}else{
					cube.rotateCube("Z", 1);
				}
			}else if(millis () > ts_rotate_2D){
				if(random(1) < 0.5f){
					cube.rotateCube("Z", 1);
				}else{
					cube.rotateCube("Y", 1);
				}
			}else if(millis () >  ts_rotate_1D){
				cube.rotateCube("Y", 1);
			}
			
		}
		
		//drumming
		if(Cube.isDrumming){
			if(beat(0)){
				cube.changeOuterCube();
				
				if(cube.radO.x == cube.rad.x || cube.radO.x == 0){
					cube.radO.set(width*0.4f, width*0.4f);
				}
			}
			if(beat(2)){
				
				cube.changeInnerCube();
				
				if(cube.radI.x == cube.rad.x || cube.radI.x == 0){
//					cube.innerTheta = random(0, TWO_PI);
					
					cube.radI.set(0, 0, 0);
				}

			}
		}
		
		
		//constant rotation
		if(cube.constant_rotateX){
			if(cube.thetaX_coeff < cube.max_theta)
				cube.thetaX_coeff += cube.theta_inc;
		}
		
		if(cube.constant_rotateY){
			if(cube.thetaY_coeff < cube.max_theta)
				cube.thetaY_coeff += cube.theta_inc;
		}
		
		if(cube.constant_rotateZ){
			if(cube.thetaZ_coeff < cube.max_theta)
				cube.thetaZ_coeff += cube.theta_inc;
		}
		
		if(cube.distortVertices != 0){
			if(beat(cube.distortVertices)){
				moveVertex(0);
			}
			
			if(beat(0))
				cube.resetCube();
		}
	}
	
	boolean beat(int division){
		if(division == 0){
			if(millis() - bpm_start_0 > bpm_timer_0){
				bpm_start_0 = millis();
				return true;
			}else{
				return false;
			}
		}else if(division == 1){
			if(millis() - bpm_start_1 > bpm_timer_1){
				bpm_start_1 = millis();
				return true;
			}else{
				return false;
			}
		}else if(division == 2){
			if(millis() - bpm_start_2 > bpm_timer_2){
				bpm_start_2 = millis();
				return true;
			}else{
				return false;
			}
		}else if(division == 4){
			if(millis() - bpm_start_4 > bpm_timer_4){
				bpm_start_4 = millis();
				return true;
			}else{
				return false;
			}
		}else{//if wrong input
			println("invalid sub-division");
			return false;
		}
	}
	
	void addDash(){
		int s = (int)random(grid.points.length-grid.cols);
		int e = dash_seed;
		
		float r = random(1);
		if(r < 0.25f){
			e = s - 1;
		}else if(r < 0.5f){
			e = s + 1;
		}else if(r < 0.75f){
			e = s - grid.rows - 1;
		}else{
			e = s + grid.rows + 1;
		}
		
		dash_seed = constrain(dash_seed, 0, grid.points.length);
		e = constrain(e, 0, grid.points.length-1);
		
		PVector start = grid.points[s];
		PVector end = grid.points[e];
		
		if(start.y != 0 && end.y != 0 && start.y != height && end.y != height)
			dashes.add(new Dash(start, end, this));
		
		dash_seed = e;
	}

	void move(String dir){

		if(dir == "up"){
			if(cube.trans.y > height*0.25f)
				cube.end.y -= height*0.25f;
		}

		if(dir == "down"){
			if(cube.trans.y < height*0.75f)
				cube.end.y += height*0.25f;
		}

		if(dir == "right"){
			if(cube.trans.x < width*0.75f)
				cube.end.x += width*0.25f;
		}

		if(dir == "left"){
			if(cube.trans.x > width*0.25f)
				cube.end.x -= width*0.25f;
		}

		cube.move_val = 0;
	}

	void removePartition(int type, char t){
		if(t == 's'){
			for(int i = 0; i < partitions.size(); i++){
				if(partitions.get(i).type == type){
					partitions.get(i).canRemove = true;
					return;
				}
			}
		}else if(t == 'a'){
			for(int i = 0; i < partitions.size(); i++){
				if(partitions.get(i).type == type){
					partitions.get(i).canRemove = true;
				}
			}
		}
	}

	void reset(int orient, int dir){
		for(int i = 0; i < partitions.size(); i++){
			Partition pa = partitions.get(i);
			if(pa.type == orient && pa.orientation == dir)
				pa.resetLerp();
			else if(orient == 2 && pa.type == 2)
				pa.resetLerp();
		}
	}

	void toggleOscillation(String s){
		if(s == "spacing"){
			Partition.oscillating_space_cos = !Partition.oscillating_space_cos;
		}else{
			Partition.oscillating_weight_cos = !Partition.oscillating_weight_cos;
		}
	}
	
	void unify(float value, int type){
		float v = map(value, 0, 127, 0, 1);
		println(v);
		for(int i = 0; i < partitions.size(); i++){
			if(partitions.get(i).type == type){
				partitions.get(i).unifier = v;
			}
		}
	}

	public void keyPressed(){
			
		if(key == 'e')
			entracte = !entracte;
		
//		if(key == 'i'){
//			intro = false;
//		}
//		
//		if(key == 'o'){
//			outro = true;
//			intro_pos = new PVector(0, 0);
//		}
//		
//		if(key == 'p')
//			intro_pixels = !intro_pixels;

		if(key =='w'){
			world = new World(this);
		}

		if(key == 'p')
			moveVertex(0);
		
		if(key == 'l')
			moveVertex(1);
		
		if(key == 'r')
			resetCube();
		
		//in/out radius
		if(key == 'i')
			cube.radI.set(0, 0, 0);
		
		if(key == 'o')
			cube.radO.set(width*0.5f, width*0.5f);
			
		
		//rotate cube
		if(key == 'x'){
			rotateCube("X", 1);
		}
		
		if(key == 'y'){
			rotateCube("Y", 1);
		}
		
		if(key == 'z'){
			rotateCube("Z", 1);
		}
		
		
		//unfold cube
		if(key == '1')
			cube.canExpand.x = 1;
		
		if(key == '2')
			cube.canExpand.y = 1;
		
		if(key == '3')
			cube.canExpand.z = 1;
	
		if(key == ' ')
			cube.canScale = true;

			
	}
	
	private void removeBlocks() {
		int u = 0;
		while(u < (int)(map(millis(), 0, ts_open_canvas, 1, 5))){
			if(blocks.size() != 0){
				if(random(1) > 0.8){//pick at random most of the time
					blocks.get((int)random(blocks.size())).fading = true;
				}else{
					int min = (int)random(2, blocks.size());
//					int max = (int)random(blocks.size()*0.5f, blocks.size()-1);
					for(int i = min; i < blocks.size()-1; i++){//choose one in continuation
						if(blocks.get(i).fading){
							float r = random(1);
							if(r < 0.25f){
								blocks.get(i-1).fading = true;
							}else if(r < 0.5f){
								blocks.get(i+1).fading = true;
							}else if(r < 0.75f){
								blocks.get(min(i+grid.rows, blocks.size()-1)).fading = true;
							}else{
								blocks.get(max(i-grid.rows, 0)).fading = true;
							}
							
							break;
						}
					}
				}
				
			}
			u++;
		}
	}
	
	public void rotateCube(String axis, int dir){
		if(axis == "X"){
			cube.targetX += PI/2*dir;
			cube.valX = 0;
		}else if(axis == "Y"){
			cube.targetY += PI/2*dir;
			cube.valY = 0;
		}else if(axis == "Z"){
			cube.targetZ += PI/2*dir;
			cube.valZ = 0;
		}else{
			println("wrong axis, bae.");
		}
	}

	public void resetCube(){
//		cube.pulse = new PVector[8];
		
		for(int i = 0; i < cube.pulse_target.length; i++){
			cube.pulse_target[i] = new PVector(0, 0, 0);
			cube.canPulse[i] = true;
			cube.pulse_val[i] = 0;
		}
	}
	
	public void moveVertex(int t){
		if(t == 0){
			int index = (int)random(cube.pulse.length);
			PVector v = new PVector(random(-100, 100), random(-100, 100), random(-100, 100));
			cube.pulse_target[index] = v;
			cube.canPulse[index] = true;
			cube.pulse_val[index] = 0;
		}else if(t == 1){
			for(int i = 0; i < cube.pulse.length; i++){
				cube.pulse[i].add(new PVector(random(-100, 100), random(-100, 100), random(-100, 100)));
				cube.canPulse[i] = true;
				cube.pulse_val[i] = 0;
			}
		}else{
			println("reset all");
			resetCube();
		}
	}


	public void noteOn(int c, int p, int v, long t, String s){
		channel = c;
		pitch = p;
		velocity = v;

		if(s == "kontrol"){
			switch(p){
			case 0://horizontal left to right
				partitions.add(new Partition(p_num_lines, p_offset, 0, 0, this));
				break;
			case 1://horizontal right to left
				partitions.add(new Partition(p_num_lines, p_offset, 0, 1, this));
				break;
			case 2://vertical top to bottom
				partitions.add(new Partition(p_num_lines, p_offset, 1, 0, this));
				break;
			case 3://vertical bottom to top
				partitions.add(new Partition(p_num_lines, p_offset, 1, 1, this));
				break;
			case 4://diagonals
				partitions.add(new Partition(p_num_lines, p_offset, 2, 0, this));
				break;
			case 5://other diagonals
				partitions.add(new Partition(p_num_lines, p_offset, 2, 1, this));
				break;
			case 6://etc
				partitions.add(new Partition(p_num_lines, p_offset, 2, 2, this));
				break;
			case 7://...
				partitions.add(new Partition(p_num_lines, p_offset, 2, 3, this));
				break;
			case 8://horizontal left to right -------------------- SECOND ROW
				//reset lerp left to right
				reset(0, 0);
				break;
			case 9://horizontal right to left
				//reset right to left
				reset(0, 1);
				break;
			case 10://vertical top to bottom
				//reset lerp
				reset(1, 0);
				break;
			case 11://vertical bottom to top
				reset(1, 1);
				break;
			case 12://diagonals
				reset(2, 0);
				break;
			case 13:
				reset(2, 0);
				break;
			case 14:
				reset(2, 0);
				break;
			case 15:
				reset(2, 0);
				break;
			case 16://horizontal left to right -------------------- THIRD ROW
				removePartition(0, 's');
				break;
			case 17://horizontal right to left
				removePartition(0, 'a');
				break;
			case 18://vertical top to bottom
				removePartition(1, 's');
				break;
			case 19://vertical bottom to top
				removePartition(1, 'a');
				break;
			case 20://diagonals
				removePartition(2, 's');
				break;
			case 21:
				removePartition(2, 'a');
				break;
			case 22:

				break;
			case 23:

				break;
			case 41:
				intro_pixels = true;
				break;
			case 42:
				//toggle unicolor
				Partition.unicolor = !Partition.unicolor;
				break;
			case 43:
				//toggle tan/cos for spacing
				toggleOscillation("spacing");
				break;
			case 44:
				//toggle tan/cos for strokes
				toggleOscillation("weight");
				break;
			default:
				break;
			}
		}

		if(s == "beatstep"){
			if(c == 0){
				switch(p){
				case 44:
					rotateCube("X", 1);
					break;
				case 45:
					rotateCube("Y", 1);
					break;
				case 46:
					rotateCube("Z", 1);
					break;
				case 47:
					move("up");
					break;
				case 48:
					moveVertex(0);
					break;
				case 49:
					cube.radI = new PVector(0, 0, 0);
					break;
				case 50:
					break;
				case 51:
					break;
				case 36://----SECOND ROW
//					Cube.show = !Cube.show;
					resetCube();
					break;
				case 37:
//					move("left");
					cube.max_width = width*0.2f;
					break;
				case 38:
//					move("right");
					cube.max_depth = width*0.2f;
					break;
				case 39:
					move("down");
					break;
				case 40:
					moveVertex(1);
					break;
				case 41:
					cube.radO = new PVector(width, width, width);
					break;
				case 42:
					break;
				case 43:
					break;
				default:
					break;
				}
			}else if(c == 1){
				switch(p){
				case 44:
					break;
				case 45:
					break;
				case 46:
					break;
				case 47:
					break;
				case 48:
					break;
				case 49:
					break;
				case 50:
					break;
				case 51:
					break;
				case 36://----SECOND ROW
					break;
				case 37:
					break;
				case 38:
					break;
				case 39:
					break;
				case 40:
					break;
				case 41:
					break;
				case 42:
					break;
				case 43:

					break;
				default:
					break;
				}
			}
		}
	}

	public void controllerChange(int c, int n, int v, long t, String s){
		channel = c;
		note = n;
		value = v;

		if(s == "kontrol"){
			if(!outro){
				switch(n){
				case 0://--------------------------------FADERS
					unify(v, 0);
					break;
				case 1:
					unify(v, 1);
					break;
				case 2:
					unify(v, 2);
					break;
				case 3:
					p_sw = map(v, 0, 127, 1, 20f);
					break;
				case 4:
					p_spacing = map(v, 0, 127, 0, 100f);
					break;
				case 5:
					p_lerp_inc_h = map(v, 0, 127, 0.0001f, 0.1f);
					break;
				case 6:
					p_lerp_inc_v = map(v, 0, 127, 0.0001f, 0.1f);
					break;
				case 7:
					p_alpha = map(v, 0, 127, 0, 255);
					break;
				case 8://--------------------------------KNOBS
//					bg_h = (int) map(v, 0, 127, 0, 360);
					break;
				case 9:
//					bg_s = (int) map(v, 0, 127, 0, 100);
					break;
				case 10:
//					bg_b = (int) map(v, 0, 127, 0, 100);
					break;
				case 11:
					p_sw_coeff = map(v, 0, 127, 0, 20);
					break;
				case 12:
					p_spacing_coeff = map(v, 0, 127, 0, 20f);
					break;
				case 13:
					p_num_lines = (int)map(v, 0, 127, 1, 100);
					break;
				case 14:
					p_offset = map(v, 0, 127, 0, 50);
					break;
				case 15:
					bpm =  map(v, 0, 127, 0.02f, 0.04f);
					break;
				default:
					break;
				}
			}else{
				switch(n){
				case 0://--------------------------------FADERS
					world.alpha = map(v, 0, 127, 0, 255);
					break;
				case 1:
					world.rad = map(v, 0, 127, 0, width*2f);
					break;
				case 2:
					world.sw = map(v, 0, 127, 1, 5);
					break;
				case 3:
					world.det = (int)map(v, 0, 127, 100, 0);
					break;
				case 4:
					world.theta_inc.x = map(v, 0, 127, 0, 0.01f);
					break;
				case 5:
					world.theta_inc.y = map(v, 0, 127, 0, 0.01f);
					break;
				case 6:
					world.theta_inc.z = map(v, 0, 127, 0, 0.01f);
					break;
				case 7:
					world.closing = map(v, 0, 127, 0, height*0.5f);
					break;
				case 8://--------------------------------KNOBS
//					bg_h = (int) map(v, 0, 127, 0, 360);
					break;
				case 9:
//					bg_s = (int) map(v, 0, 127, 0, 100);
					break;
				case 10:
//					bg_b = (int) map(v, 0, 127, 0, 100);
					break;
				case 11:
					p_sw_coeff = map(v, 0, 127, 0, 20);
					break;
				case 12:
					p_spacing_coeff = map(v, 0, 127, 0, 20f);
					break;
				case 13:
					p_num_lines = (int)map(v, 0, 127, 1, 100);
					break;
				case 14:
					p_offset = map(v, 0, 127, 0, 50);
					break;
				case 15:
					bpm =  map(v, 0, 127, 0.02f, 0.04f);
					break;
				default:
					break;
				}
			}
		}else if(s == "beatstep"){
			v = v - 64;//normalize

			if(n == 7){//reset stuff
				cube.cube_scale += v*0.01f;
				cube.cube_scale = constrain(cube.cube_scale, 0, 1);
			}

			if(c == 0){
				switch(n){
				case 10:
					c_depth += v*1f;
					break;
				case 11:
					c_thetaX_coeff = map(v, 0, 127, 0, 0.001f);
					c_thetaX_coeff = constrain(c_thetaX_coeff, 0, 0.1f);
					break;
				case 12:
					c_thetaY_coeff = map(v, 0, 127, 0, 0.001f);
					c_thetaY_coeff = constrain(c_thetaY_coeff, 0, 0.1f);
					break;
				case 13:
					c_thetaZ_coeff = map(v, 0, 127, 0, 0.001f);
					c_thetaZ_coeff = constrain(c_thetaZ_coeff, 0, 0.1f);
					break;
				case 14:
					cube.radIncI += v*0.5f;
					break;
				case 15:
					cube.diagCoeffX += v*0.001f;
					break;
				case 16:
					cube.diagCoeffY += v*0.001f;
					break;
				case 17:
					cube.diagCoeffZ += v*0.001f;
					break;
				case 18://--------------------------------SECOND ROW
					c_height += v*1f;
					break;
				case 19:
					//??
					break;
				case 20:
					//??
					break;
				case 21:
					cube.sw += v*0.1f;
					break;
				case 22:
					cube.radIncO += v*5f;
					break;
				case 23:
					cube.diagSpeedX += v*0.001f;
					break;
				case 24:
					cube.diagSpeedY += v*0.001f;
					break;
				case 25:
					cube.diagSpeedZ += v*0.001f;
					break;
				default:
					break;
				}
			}else if(c == 1){ // ------------------ POTENTIALLY SOME WORLD STUFF
				switch(n){
				case 10:
					break;
				case 11:
					break;
				case 12:
					break;
				case 13:
					break;
				case 14:
					break;
				case 15:

					break;
				case 16:

					break;
				case 17:

					break;
				case 18://--------------------------------SECOND ROW

					break;
				case 19:

					break;
				case 20:

					break;
				case 21:

					break;
				case 22:

					break;
				case 23:

					break;
				case 24:

					break;
				case 25:

					break;
				default:
					break;
				}
			}
		}
	}

	public static void main(String _args[]) {
		PApplet.main(new String[] { vertices.Vertices.class.getName() });
	}
}
