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

	ArrayList<Block> blocks;
	World world;

	static float bpm;
	float bpm_coeff;

	static ArrayList<Partition> partitions;
	static ArrayList<Cube> cubes;
	public Cube cube;

	int bg_h;
	int bg_s;
	int bg_b;

	static int red;
	static int green;
	static int blue;

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

	public void setup() {
		noCursor();
		MidiBus.list();
		midi_beatstep = new MidiBus(this, "Arturia BeatStep", 1);
		midi_kontrol = new MidiBus(this, "SLIDER/KNOB", 2);

		midi_beatstep.setBusName("beatstep");
		midi_kontrol.setBusName("kontrol");

		bpm = 0.037f;

		intro_pos = new PVector(-intro_width, 0);
		blocks = new ArrayList<Block>();

		partitions = new ArrayList<Partition>();

		cube = new Cube(this);
		cubes = new ArrayList<Cube>();

		colorMode(HSB, 360, 100, 100);
		red = color(0, 75, 75);
		green = color(120, 75, 75);
		blue = color(240, 75, 75);

		background(0, 0, 100);
	}

	public void settings(){
		fullScreen(P3D);
	}

	void introPixels(){
		if(intro_pos.x < width && noise(frameCount*mouseX*1f) > map(mouseY, 0, height, 1, 0)){
			blocks.add(new Block(intro_pos.copy(), intro_width, intro_height, 0, this));
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
			blocks.add(new Block(intro_pos.copy(), intro_width, intro_height, 1, this));
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

	void introBackground(){
		noStroke();
		int col1 = color(0, 0, 80-cos(millis()*0.001f+PI/3)*20);
		int col2 = color(0, 0, 80-cos(millis()*0.001f+PI/3*2)*20);
		int col3 = color(0, 0, 80-cos(millis()*0.001f)*20);

		fill(col1);
		rect(0, 0, width, height*0.30f);

		fill(col2);
		rect(0, height*0.30f, width, height*0.40f);

		fill(col3);
		rect(0, height*0.70f, width, height*0.30f);
	}

	public void debug(){
		textAlign(LEFT);
		textSize(10);
		fill(100, 255, 100);
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
		for(int i = 0; i < partitions.size(); i++){
			if(partitions.get(i) != null)
				partitions.get(i).update();
		}

		if(cube != null)
			cube.update();

		if(intro && intro_pixels)
			introPixels();

		if(outro)
			outroPixels();

		if(world != null)
			world.update();
	}

	public void draw() {
		noCursor();
		update();

		background(bg_h, bg_s, bg_b);



		if(outro){
			for(int i = 0; i < blocks.size(); i++){
				blocks.get(i).display();
			}
		}

		for(int i = 0; i < partitions.size(); i++){
			partitions.get(i).display();
		}
		
		if(cube != null)
			cube.display();

		if(world != null){
			background(0, 0, 100);
			world.display();
		}

		
		if(entracte){
			fill(0);
			noStroke();
			rect(0, 0, width*2, height*2);
		}
		
		if(intro){
			introBackground();
			background(0, 0, 100);

			for(int i = 0; i < blocks.size(); i++){
				blocks.get(i).display();
			}
		}
		
//		debug();
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
		
		if(key == 'i'){
			intro = false;
			blocks.clear();
		}
		
		if(key == 'p')
			intro_pixels = !intro_pixels;

		if(key =='w'){
			world = new World(this);
		}

		if(key == 'o'){
			outro = true;
			intro_pos = new PVector(0, 0);
		}

		if(key == 'r'){
			fill(red);
			noStroke();
			rect(0, 0, width, height*0.30f);
		}

		if(key == 'g'){
			fill(green);
			noStroke();
			rect(0, height*0.30f, width, height*0.40f);
		}

		if(key == 'b'){
			fill(blue);
			noStroke();
			rect(0, height*0.70f, width, height*0.30f);
		}

		if(key == 's')
			world.det--;
		
		if(key == 'x')
			world.det++;
		
		if(key == 'c'){
			cube = new Cube(this);
			cubes = new ArrayList<Cube>();
		}
			
		
		if(key == ' ')
			moveVertex(2);
	}
	
	public void resetCube(){
		cube.pulse = new PVector[8];
		
		for(int i = 0; i < cube.pulse.length; i++){
			cube.pulse[i] = new PVector(0, 0, 0);
		}
	}
	
	public void moveVertex(int t){
		if(t == 0){
			cube.pulse[(int)random(cube.pulse.length)] = new PVector(random(-100, 100), random(-100, 100), random(-100, 100));
		}else if(t == 1){
			for(int i = 0; i < cube.pulse.length; i++){
				cube.pulse[i] = new PVector(random(-100, 100), random(-100, 100), random(-100, 100));
			}
		}else{
			println("reset");
			for(int i = 0; i < cube.pulse.length; i++){
				cube.pulse[i] = new PVector(0, 0, 0);
			}
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
					cube.targetX += PI/2;
					cube.valX = 0;
					break;
				case 45:
					cube.targetY += PI/2;
					cube.valY = 0;
					break;
				case 46:
					cube.targetY += PI/2;
					cube.valY = 0;
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
					bg_h = (int) map(v, 0, 127, 0, 360);
					break;
				case 9:
					bg_s = (int) map(v, 0, 127, 0, 100);
					break;
				case 10:
					bg_b = (int) map(v, 0, 127, 0, 100);
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
					bg_h = (int) map(v, 0, 127, 0, 360);
					break;
				case 9:
					bg_s = (int) map(v, 0, 127, 0, 100);
					break;
				case 10:
					bg_b = (int) map(v, 0, 127, 0, 100);
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
